package projects.etrkk5.employeer;

import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import projects.etrkk5.employeer.Adapters.employee_ad_adapter;
import projects.etrkk5.employeer.Adapters.item_adapter;
import projects.etrkk5.employeer.Profiles.Item;
import projects.etrkk5.employeer.Profiles.employeeAd;

public class EmployeeGetAdActivity extends AppCompatActivity implements View.OnClickListener{

    private RecyclerView recyclerView;
    private TextView textViewHint;
    private employee_ad_adapter adAdapter;
    private List<employeeAd> adList;
    private List list, list1;
    private Set hs;
    RecyclerView.LayoutManager mLayoutManager;
    public String search;
    public String title;
    public String location;
    public String companyName;
    public String docsRef;

    String skill1 = "";
    String skill2 = "";
    String skill3 = "";
    String skill4 = "";
    String skill5 = "";

    private static final String TAG = "EmployeeGetAdActivity: ";

    FirebaseAuth mAuth;
    FirebaseFirestore db;
    EditText editTextSearch;
    ImageView imageViewSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_get_ad);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        adList = new ArrayList<>();
        list = new ArrayList();
        list1 = new ArrayList();
        hs = new HashSet<>();

        editTextSearch =  findViewById(R.id.editTextSearch);
        imageViewSearch =  findViewById(R.id.imageViewSearch);
        recyclerView =  findViewById(R.id.recycler_view);
        adAdapter = new employee_ad_adapter(adList, this);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        imageViewSearch.setOnClickListener(this);
        getAds();
        textViewHint = findViewById(R.id.textViewHint);
    }

    public void getAds(){
        final String uid = mAuth.getCurrentUser().getUid();
        db.collection("skills").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Log.e(TAG, "inside");
                skill1 = task.getResult().get("skill1").toString().toLowerCase();
                skill2 = task.getResult().get("skill2").toString().toLowerCase();
                skill3 = task.getResult().get("skill3").toString().toLowerCase();
                skill4 = task.getResult().get("skill4").toString().toLowerCase();
                skill5 = task.getResult().get("skill5").toString().toLowerCase();
            }
        });

        db.collection("newAds").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(DocumentSnapshot doc : task.getResult()){
                    sleep();
                    if(doc.getData().toString().toLowerCase().contains(skill1) && !skill1.equals("")){
                        list.add(doc.getId());
                    }
                    if(doc.getData().toString().toLowerCase().contains(skill2) && !skill2.equals("")){
                        list.add(doc.getId());
                    }
                    if(doc.getData().toString().toLowerCase().contains(skill3) && !skill3.equals("")){
                        list.add(doc.getId());
                    }
                    if(doc.getData().toString().toLowerCase().contains(skill4) && !skill4.equals("")){
                        list.add(doc.getId());
                    }
                    if(doc.getData().toString().toLowerCase().contains(skill5) && !skill5.equals("")){
                        list.add(doc.getId());
                    }
                }

                hs.addAll(list);
                list.clear();
                list.addAll(hs);


                for(final Object x : list){
                    sleep();
                    db.collection("newAds").document(x.toString()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            Log.e(TAG, "icerde1");
                            docsRef = x.toString();
                            title = task.getResult().get("title").toString();
                            location = task.getResult().get("location").toString();
                            companyName = task.getResult().get("companyName").toString();
                            adList.add(new employeeAd(docsRef, title, location, companyName));
                            recyclerView.setAdapter(adAdapter);
                        }
                    });
                }
                list.clear();
            }
        });
    }

    public void searchAd(final String search){
        textViewHint.setVisibility(View.GONE);
        adList.clear();
        db.collection("newAds").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(DocumentSnapshot doc : task.getResult()){
                    if(doc.getData().toString().toLowerCase().contains(search)){
                        list1.add(doc.getId());
                    }
                }

                for(final Object y : list1){
                    db.collection("newAds").document(y.toString()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            docsRef = y.toString();
                            title = task.getResult().get("title").toString();
                            location = task.getResult().get("location").toString();
                            companyName = task.getResult().get("companyName").toString();
                            list1.clear();
                            adList.add(new employeeAd(docsRef, title, location, companyName));
                            recyclerView.setAdapter(adAdapter);
                        }
                    });
                }
            }
        });
    }

    public void sleep(){
        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == imageViewSearch.getId()){
            adList.clear();
            search = editTextSearch.getText().toString().toLowerCase();
            sleep();
            if(!search.equals("")){
                searchAd(search);
            }else{
                getAds();
            }
        }
    }
}
