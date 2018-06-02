package projects.etrkk5.employeer;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import projects.etrkk5.employeer.Adapters.company_ad_adapter;
import projects.etrkk5.employeer.Profiles.Ad;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class CompanyPostAdActivity extends AppCompatActivity implements View.OnClickListener{
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    FloatingActionButton button;
    Context context = this;

    private company_ad_adapter adAdapter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<Ad> adList;
    private List list, list1, list2;
    RecyclerView.LayoutManager mLayoutManager;
    public String title;
    public String location;
    public String companyName;
    public String companyEmail;

    private static final String TAG = "CompanyPostAdActivity: ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_post_ad);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        adList = new ArrayList<>();
        list = new ArrayList();
        list1 = new ArrayList();
        list2 = new ArrayList();

        button = findViewById(R.id.buttonAdd);
        swipeRefreshLayout = findViewById(R.id.swipeToRefresh);
        recyclerView = findViewById(R.id.recycler_view);
        adAdapter = new company_ad_adapter(adList, this);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                startActivity(getIntent());
                finish();
            }
        });

        getAd();
        button.setOnClickListener(this);
    }

    public void addAd(){
        LayoutInflater li = LayoutInflater.from(context);
        View dialogView = li.inflate(R.layout.input_dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        alertDialogBuilder.setView(dialogView);

        final EditText editTextTitle = dialogView.findViewById(R.id.editTextTitle);
        final EditText editTextDescription = dialogView.findViewById(R.id.editTextDescription);
        final EditText editTextLocation = dialogView.findViewById(R.id.editTextLocation);

        alertDialogBuilder.setCancelable(false).setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final String uid = mAuth.getCurrentUser().getUid();
                final String mTitle = editTextTitle.getText().toString();
                final String mDescription = editTextDescription.getText().toString();
                final String mLocation = editTextLocation.getText().toString();
                db.collection("company").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        companyName = task.getResult().get("companyName").toString();
                        companyEmail = task.getResult().get("companyEmail").toString();
                    }
                });
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                db.collection("company-ads").document(uid).collection("ads").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for(DocumentSnapshot doc : task.getResult()){
                            list1.add(doc.getId().toString());
                        }

                            db.collection("company-ads").document(uid).collection("ads").document("ad1").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if(task.getResult().get("title").toString().equals("") ||
                                            task.getResult().get("description").toString().equals("") ||
                                            task.getResult().get("location").toString().equals("")){
                                        Map<String, String> map = new HashMap<>();
                                        map.put("title", mTitle);
                                        map.put("description", mDescription);
                                        map.put("location", mLocation);
                                        map.put("companyName", companyName);
                                        map.put("companyEmail", companyEmail);
                                        db.collection("newAds").document(uid + "ad1").set(map);
                                        db.collection("company-ads").document(uid).collection("ads").document("ad1").set(map);
                                    } else {
                                        Map<String, String> mMap = new HashMap<>();
                                        mMap.put("title", mTitle);
                                        mMap.put("description", mDescription);
                                        mMap.put("location", mLocation);
                                        mMap.put("companyName", companyName);
                                        mMap.put("companyEmail", companyEmail);
                                        int mListSize = list1.size()+1;
                                        String listSize = Integer.toString(mListSize);
                                        db.collection("newAds").document(uid + "ad" + listSize).set(mMap);
                                        db.collection("company-ads").document(uid).collection("ads").document("ad" + listSize).set(mMap);
                                    }
                                }
                            });
                    }
                });



            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                dialogInterface.cancel();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    public void getAd(){
        final String uid = mAuth.getCurrentUser().getUid();
        db.collection("company-ads").document(uid).collection("ads").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(DocumentSnapshot doc : task.getResult()){
                    list.add(doc.getId().toString());
                }
                for(int i=0; i<list.size(); i++){
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    db.collection("company-ads").document(uid).collection("ads").document(list.get(i).toString()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            title = task.getResult().get("title").toString();
                            location = task.getResult().get("location").toString();
                            recyclerView.setAdapter(adAdapter);
                            adList.add(new Ad(uid, title, location));
                        }
                    });
                }
                list.clear();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == button.getId()){
            addAd();
        }
    }
}
