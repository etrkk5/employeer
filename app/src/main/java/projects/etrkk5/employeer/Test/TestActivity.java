package projects.etrkk5.employeer.Test;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import projects.etrkk5.employeer.R;

public class TestActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    FirebaseFirestore db;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        getUserSkills("eIG74tscGVdggZwQ4urH");
//        updateSkills("java", "eIG74tscGVdggZwQ4urH");

    }



    public void createList(List<String> skills){
        if(skills == null) {
            skills = new ArrayList<>();
        }
        Map<String, List<String>> map = new HashMap<>();
        map.put("skills", skills);
        FirebaseFirestore.getInstance().collection("skills").document("tgxVBFJA7pbF3fNCbLBozDeLsMg2").set(map);
    }

    public void getUserHaveSkill(final String skill, final String ref){
        Query query = FirebaseFirestore.getInstance().collection("skills").whereEqualTo("java", true);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(DocumentSnapshot doc : task.getResult()){
                    Log.e(TAG, doc.getId() + " " + doc.getData());
                }
            }
        });
    }
    public void getUserSkills(final String ref) {
        FirebaseFirestore.getInstance().collection("skills").document(ref).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                List<String> skills = (List<String>) task.getResult().get("skills");
                for(int i=0; i<skills.size(); i++){
                    Log.e(TAG, skills.get(i).toString());
                }
            }
        });
    }
    public void updateSkills(final String skill, final String ref){
        FirebaseFirestore.getInstance().collection("skills").document(ref).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                List<String> skills = (List<String>) task.getResult().get("skills");
                if(!skills.contains(skill)) {
                    skills.add(skill);
                    Map<String, List<String>> map = new HashMap<>();
                    map.put("skills", skills);
                    FirebaseFirestore.getInstance().collection("skills").document(ref).set(map);
                }
                Log.e(TAG, skills.toString());
            }
        });
    }



    public void addSkill(final String s) {





        FirebaseFirestore.getInstance().collection("skills").whereEqualTo("skills.0", "javascript").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(DocumentSnapshot a :task.getResult()) {
                    Log.e(TAG, a.getId());
                }
            }
        });




        FirebaseFirestore.getInstance().collection("skills").document("tgxVBFJA7pbF3fNCbLBozDeLsMg2").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                List<String> skills = (List<String>) task.getResult().get("skills");
                Map<String, List> map = new HashMap<>();
                skills.add(s);
                map.put("skills", skills);

                FirebaseFirestore.getInstance().collection("skills").document("tgxVBFJA7pbF3fNCbLBozDeLsMg2").set(map);

            }
        });
    }

    public void getArray() {
        FirebaseFirestore.getInstance().collection("skills").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(DocumentSnapshot doc : task.getResult() ) {
                    Log.e(TAG, doc.getId() + " : " + doc.getData());
                }
            }
        });
    }



    class Skills {
        public List<String> skills;
        public Skills(){
            skills = new ArrayList<>();
        }
    }

}