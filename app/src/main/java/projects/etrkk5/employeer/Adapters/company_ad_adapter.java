package projects.etrkk5.employeer.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import projects.etrkk5.employeer.Profiles.Ad;
import projects.etrkk5.employeer.R;

/**
 * Created by EsrefTurkok on 5.05.2018.
 */

public class company_ad_adapter extends RecyclerView.Adapter<company_ad_adapter.ad_holder>{
    private List<Ad> adList;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    Context context;
    String companyName = "";

    public company_ad_adapter(List<Ad> adList, Context context){
        this.adList = adList;
        this.context = context;
    }

    @Override
    public ad_holder onCreateViewHolder(final ViewGroup parent, final int viewType){
        final View adView = LayoutInflater.from(parent.getContext()).inflate(R.layout.company_ad_layout, parent, false);
        final ad_holder holder = new ad_holder(adView);
        return new ad_holder(adView);
    }

    @Override
    public void onBindViewHolder(@NonNull ad_holder holder, int position) {
        Ad ad = adList.get(position);
        holder.cardView.setTag(position);
        holder.textViewTitle.setText(ad.getTitle());
        holder.textViewLocation.setText(ad.getLocation());
        holder.textViewUserId.setText(ad.getDocsRef());
    }

    @Override
    public int getItemCount() {
        return adList.size();
    }

    public class ad_holder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView textViewTitle;
        public TextView textViewLocation;
        public TextView textViewUserId;
        public CardView cardView;

        public ad_holder(View itemView) {
            super(itemView);
            textViewTitle = (TextView) itemView.findViewById(R.id.textViewTitle);
            textViewLocation = (TextView) itemView.findViewById(R.id.textViewLocation);
            textViewUserId = (TextView) itemView.findViewById(R.id.textViewDocsRef);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            final int pos = (int) v.getTag();
            int position = pos+1;
            final String posit = Integer.toString(position);
            // Log.e("company_ad_adapter: ", "" +position);
            db = FirebaseFirestore.getInstance();
            mAuth = FirebaseAuth.getInstance();
            final String uid = mAuth.getCurrentUser().getUid();

            LayoutInflater li = LayoutInflater.from(context);
            View dialogView = li.inflate(R.layout.input_dialog, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setView(dialogView);

            final EditText editTextTitle = dialogView.findViewById(R.id.editTextTitle);
            final EditText editTextDescription = dialogView.findViewById(R.id.editTextDescription);
            final EditText editTextLocation = dialogView.findViewById(R.id.editTextLocation);

            db.collection("company-ads").document(uid).collection("ads").document("ad" + posit).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    editTextTitle.setText(task.getResult().get("title").toString());
                    editTextDescription.setText(task.getResult().get("description").toString());
                    editTextLocation.setText(task.getResult().get("location").toString());
                    companyName = task.getResult().get("companyName").toString();
                }
            });

            alertDialogBuilder.setCancelable(false).setPositiveButton("Save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    final String mTitle = editTextTitle.getText().toString();
                    final String mLocation = editTextLocation.getText().toString();
                    final String mDescription = editTextDescription.getText().toString();

                    Map<String, String> map = new HashMap<>();
                    map.put("title", mTitle);
                    map.put("location", mLocation);
                    map.put("description", mDescription);
                    map.put("companyName", companyName);
                    db.collection("newAds").document(uid + "ad" + posit).set(map);
                    db.collection("company-ads").document(uid).collection("ads").document("ad"+ posit).set(map);
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {
                    dialogInterface.cancel();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }


}
