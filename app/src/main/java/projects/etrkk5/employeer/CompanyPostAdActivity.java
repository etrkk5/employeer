package projects.etrkk5.employeer;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import projects.etrkk5.employeer.Adapters.AdAdapter;
import projects.etrkk5.employeer.Profiles.Ad;

public class CompanyPostAdActivity extends AppCompatActivity implements View.OnClickListener{
    FloatingActionButton buttonAdd;
    Map<String, String> map;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    ListView listViewAd;
    List<Ad> adList;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_post_ad);
        adList = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        map = new HashMap<>();

        buttonAdd = (FloatingActionButton)findViewById(R.id.buttonAdd);
        listViewAd = (ListView)findViewById(R.id.listViewAd);

        buttonAdd.setOnClickListener(this);
        final AdAdapter adAdapter = new AdAdapter(this, adList);

        String uid = mAuth.getCurrentUser().getUid();
        db.collection("company-ads").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    adList.add(new Ad(task.getResult().get("title").toString()));
                }
                listViewAd.setAdapter(adAdapter);
            }
        });
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == buttonAdd.getId()){
            showInputDialog();
        }
    }

    protected void showInputDialog() {

        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(CompanyPostAdActivity.this);
        View promptView = layoutInflater.inflate(R.layout.input_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CompanyPostAdActivity.this);
        alertDialogBuilder.setView(promptView);

        final EditText editTextTitle = (EditText) promptView.findViewById(R.id.editTextTitle);
        final EditText editTextDescription = (EditText) promptView.findViewById(R.id.editTextDescription);
        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("Post", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String title = editTextTitle.getText().toString();
                        String description = editTextDescription.getText().toString();
                        final String uid = mAuth.getCurrentUser().getUid();
                        map.put("title", title);
                        map.put("description", description);
                        db.collection("company-ads").document(uid).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(getApplicationContext(), "Succesfully added!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }
}
