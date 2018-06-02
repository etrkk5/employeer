package projects.etrkk5.employeer.Test;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import projects.etrkk5.employeer.R;

public class TestActivity extends AppCompatActivity {

    private static final String TAG = "TestActivity: ";
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    List list, list1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        list = new ArrayList();
        list1 = new ArrayList();

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword("tester@tester.com", "tester123").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.e(TAG, "hi");
                getAds();
            }
        });
    }

    public void getAds(){
        db.collection("company").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(DocumentSnapshot doc : task.getResult()){
                    list.add(doc.getId().toString());
                }

                for(Object x : list){
                    for(int i=0; i<10; i++){
                        db.collection("newAds").document(x.toString() + "ad" + Integer.toString(i)).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                DocumentSnapshot doc = task.getResult();
                                if(task.isSuccessful()){
                                    if(doc.exists()){
                                        Log.e(TAG, task.getResult().get("title").toString());
                                        sleep();
                                        Log.e(TAG, task.getResult().get("location").toString());
                                        sleep();
                                        Log.e(TAG, task.getResult().get("description").toString());
                                    }
                                }
                            }
                        });
                    }
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



}



























//    public void firstJob(){
//        db.collection("company").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                for(DocumentSnapshot doc : task.getResult()){
//                    sleep();
//                    Log.e(TAG, doc.getId());
//                    list.add(doc.getId());
//                }
//                secondJob();
//            }
//        });
//    }
//
//    public void secondJob(){
//        for(int i=0; i<list.size(); i++){
//            sleep();
//            db.collection("company-ads").document(list.get(i).toString()).collection("ads").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                    for(DocumentSnapshot doc : task.getResult()){
//                        if(doc.getData().toString().toLowerCase().contains("java")){
//                            Log.e(TAG, doc.getId());
//                            list1.add(doc.getId());
//                        }
//                    }
//                    thirdJob();
//                }
//            });
//        }
//    }
//
//    public void thirdJob(){
//        for(int i=0; i<list.size(); i++){
//            sleep();
//            for(int j=0; j<list1.size(); j++){
//                sleep();
//                db.collection("company-ads").document(list.get(i).toString()).collection("ads").document(list1.get(j).toString()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        if(task.isSuccessful()){
//                            DocumentSnapshot doc = task.getResult();
//                            if(doc.exists()){
//                                Log.e(TAG, task.getResult().get("title").toString());
//                            }
//                        }
//                    }
//                });
//            }
//        }
//    }



//    public void getUserHaveSkill(final String skill){
//
//        db.collection("skills").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                for(DocumentSnapshot doc : task.getResult()) {
//                    if (doc.getData().containsValue(skill)) {
//                        if(doc.getData().toString().toLowerCase().contains("java")){
//                            String l = doc.getData().toString().toLowerCase();
//                            Log.e(TAG, ": " +l);
//                        }
//
//                        list.add(doc.getId());
//                    }
//                }
//
//                for(int i=0; i<list.size(); i++){
//                    db.collection("employee").document(list.get(i).toString()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                            Log.e(TAG, task.getResult().get("employeeName").toString() + " " + task.getResult().get("employeeSurname").toString());
//                        }
//                    });
//                }
//            }
//        });
//    }




//        db = FirebaseFirestore.getInstance();
//        mAuth = FirebaseAuth.getInstance();
//
//        getUserSkills("eIG74tscGVdggZwQ4urH");
////        updateSkills("java", "eIG74tscGVdggZwQ4urH");
//
//    }
//
//
//
//    public void createList(List<String> skills){
//        if(skills == null) {
//            skills = new ArrayList<>();
//        }
//        Map<String, List<String>> map = new HashMap<>();
//        map.put("skills", skills);
//        FirebaseFirestore.getInstance().collection("skills").document("tgxVBFJA7pbF3fNCbLBozDeLsMg2").set(map);
//    }
//
//    public void getUserHaveSkill(final String skill, final String ref){
//        Query query = FirebaseFirestore.getInstance().collection("skills").whereEqualTo("Java", true);
//        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                for(DocumentSnapshot doc : task.getResult()){
//                    Log.e(TAG, doc.getId() + " " + doc.getData());
//                }
//            }
//        });
//    }
//    public void getUserSkills(final String ref) {
//        FirebaseFirestore.getInstance().collection("skills").document(ref).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                List<String> skills = (List<String>) task.getResult().get("skills");
//                for(int i=0; i<skills.size(); i++){
//                    Log.e(TAG, skills.get(i).toString());
//                }
//            }
//        });
//    }
//    public void updateSkills(final String skill, final String ref){
//        FirebaseFirestore.getInstance().collection("skills").document(ref).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                List<String> skills = (List<String>) task.getResult().get("skills");
//                if(!skills.contains(skill)) {
//                    skills.add(skill);
//                    Map<String, List<String>> map = new HashMap<>();
//                    map.put("skills", skills);
//                    FirebaseFirestore.getInstance().collection("skills").document(ref).set(map);
//                }
//                Log.e(TAG, skills.toString());
//            }
//        });
//    }
//
//
//
//    public void addSkill(final String s) {
//
//
//
//
//
//        FirebaseFirestore.getInstance().collection("skills").whereEqualTo("skills.0", "javascript").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                for(DocumentSnapshot a :task.getResult()) {
//                    Log.e(TAG, a.getId());
//                }
//            }
//        });
//
//
//
//
//        FirebaseFirestore.getInstance().collection("skills").document("tgxVBFJA7pbF3fNCbLBozDeLsMg2").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                List<String> skills = (List<String>) task.getResult().get("skills");
//                Map<String, List> map = new HashMap<>();
//                skills.add(s);
//                map.put("skills", skills);
//
//                FirebaseFirestore.getInstance().collection("skills").document("tgxVBFJA7pbF3fNCbLBozDeLsMg2").set(map);
//
//            }
//        });
//    }
//
//    public void getArray() {
//        FirebaseFirestore.getInstance().collection("skills").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                for(DocumentSnapshot doc : task.getResult() ) {
//                    Log.e(TAG, doc.getId() + " : " + doc.getData());
//                }
//            }
//        });
//    }
//
//
//
//    class Skills {
//        public List<String> skills;
//        public Skills(){
//            skills = new ArrayList<>();
//        }
//    }