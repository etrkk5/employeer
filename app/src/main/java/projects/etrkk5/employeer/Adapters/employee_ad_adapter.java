package projects.etrkk5.employeer.Adapters;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import projects.etrkk5.employeer.Profiles.employeeAd;
import projects.etrkk5.employeer.R;

/**
 * Created by EsrefTurkok on 5.05.2018.
 */

public class employee_ad_adapter extends RecyclerView.Adapter<employee_ad_adapter.ead_holder> {
    private List<employeeAd> adList;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    Context context;
    String message = "";
    String mailTo = "";
    String title = "";
    String companyEmail = "";

    public employee_ad_adapter(List<employeeAd> adList, Context context){
        this.adList = adList;
        this.context = context;
    }

    @Override
    public ead_holder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View adView = LayoutInflater.from(parent.getContext()).inflate(R.layout.employee_ad_layout, parent, false);
        final ead_holder holder = new ead_holder(adView);
        return new ead_holder(adView);
    }

    @Override
    public void onBindViewHolder(@NonNull ead_holder holder, int position) {
        employeeAd ead = adList.get(position);
        holder.cardView.setTag(position);
        holder.textViewTitle.setText(ead.getTitle());
        holder.textViewLocation.setText(ead.getLocation());
        holder.textViewCompanyName.setText(ead.getCompanyName());
        holder.textViewDocsRef.setText(ead.getDocsRef());
    }

    @Override
    public int getItemCount() {
        return adList.size();
    }

    public class ead_holder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView textViewTitle;
        public TextView textViewLocation;
        public TextView textViewCompanyName;
        public TextView textViewDocsRef;
        public CardView cardView;

        public ead_holder(View itemView) {
            super(itemView);
            textViewTitle = (TextView) itemView.findViewById(R.id.textViewTitle);
            textViewLocation = (TextView) itemView.findViewById(R.id.textViewLocation);
            textViewCompanyName = (TextView) itemView.findViewById(R.id.textViewCompanyName);
            textViewDocsRef = (TextView) itemView.findViewById(R.id.textViewDocsRef);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos = (int) v.getTag();
            int position = pos+1;
            final String posit = Integer.toString(position);
            // Log.e("TAG: ", textViewTitle.getText().toString());
            db = FirebaseFirestore.getInstance();

            LayoutInflater li = LayoutInflater.from(context);
            View dialogView = li.inflate(R.layout.input_dialog_employee, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setView(dialogView);

            final TextView mTextViewTitle = dialogView.findViewById(R.id.mTextViewTitle);
            final TextView mTextViewDesrciption = dialogView.findViewById(R.id.mTextViewDescription);
            final TextView mTextViewLocation = dialogView.findViewById(R.id.mTextViewLocation);

            final String docsRef = textViewDocsRef.getText().toString();

            db.collection("newAds").document(docsRef).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    mTextViewTitle.setText(task.getResult().get("title").toString());
                    mTextViewDesrciption.setText(task.getResult().get("description").toString());
                    mTextViewLocation.setText(task.getResult().get("location").toString());
                }
            });

            alertDialogBuilder.setCancelable(false).setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.e("tag: ", "applied to: " + docsRef);
                    db.collection("newAds").document(docsRef).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            companyEmail = task.getResult().get("companyEmail").toString();
                            title = task.getResult().get("title").toString();
                            mAuth = FirebaseAuth.getInstance();
                            String uid = mAuth.getCurrentUser().getUid();
                            db.collection("employee").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    String name = task.getResult().get("employeeName").toString();
                                    String surname = task.getResult().get("employeeSurname").toString();
                                    message = "Hi I'm "+name+" "+surname+",\n I'm interested in your " +title+ " advertisement.";
                                    mailTo = "mailto:" +companyEmail+
                                            "?cc=" + "etrkk5@gmail.com" +
                                            "&subject=" + Uri.encode(title+ " Advertisement, Job Application") +
                                            "&body=" + Uri.encode(message);

                                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                                    emailIntent.setData(Uri.parse(mailTo));

                                    try {
                                        context.startActivity(emailIntent);
                                    } catch (ActivityNotFoundException e) {
                                        Log.e("adapter : ", "HATA!!!");
                                    }

                                }
                            });

                        }
                    });
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
