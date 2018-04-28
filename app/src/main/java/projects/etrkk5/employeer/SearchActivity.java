package projects.etrkk5.employeer;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import projects.etrkk5.employeer.Adapters.item_adapter;
import projects.etrkk5.employeer.Profiles.Item;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener{

    private RecyclerView recyclerView;
    private item_adapter itemAdapter;
    private List<Item> itemList;
    private List list, list1;
    RecyclerView.LayoutManager mLayoutManager;
    public String search;
    public String name;
    public String location;

    FirebaseFirestore db;
    EditText editTextSearch;
    ImageView imageViewSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        db = FirebaseFirestore.getInstance();

        itemList = new ArrayList<>();
        list = new ArrayList();
        list1 = new ArrayList();

        editTextSearch = (EditText) findViewById(R.id.editTextSearch);
        imageViewSearch = (ImageView) findViewById(R.id.imageViewSearch);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        itemAdapter = new item_adapter(itemList);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        imageViewSearch.setOnClickListener(this);
    }

    public void getUser(final String search){
        db.collection("employee").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(DocumentSnapshot doc : task.getResult()){
                    if(doc.getData().toString().toLowerCase().contains(search)){
                        list.add(doc.getId());
                    }
                }

                for(int i=0; i<list.size(); i++){
                    db.collection("employee").document(list.get(i).toString()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            name = task.getResult().get("employeeName").toString() + " " + task.getResult().get("employeeSurname").toString();
                            location = task.getResult().get("employeeLocation").toString();
                            list.clear();
                            itemList.add(new Item(name, location));
                        }
                    });
                    recyclerView.setAdapter(itemAdapter);
                }
            }
        });

        db.collection("company").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(DocumentSnapshot doc : task.getResult()){
                    if(doc.getData().toString().toLowerCase().contains(search)){
                        list1.add(doc.getId());
                    }
                }

                for(int i=0; i<list1.size(); i++){
                    db.collection("company").document(list1.get(i).toString()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            name = task.getResult().get("companyName").toString();
                            location = task.getResult().get("companyLocation").toString();
                            list1.clear();
                            itemList.add(new Item(name, location));
                        }
                    });
                    recyclerView.setAdapter(itemAdapter);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == imageViewSearch.getId()){
            itemList.clear();
            search = editTextSearch.getText().toString().toLowerCase();
            getUser(search);
        }
    }
}
