package projects.etrkk5.employeer;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;

public class SkillsActivity extends AppCompatActivity implements View.OnClickListener{

    EditText editTextSkill1, editTextSkill2, editTextSkill3, editTextSkill4, editTextSkill5;
    Button buttonSave;

    FirebaseFirestore db;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skills);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        editTextSkill1 = (EditText)findViewById(R.id.editTextSkill1);
        editTextSkill2 = (EditText)findViewById(R.id.editTextSkill2);
        editTextSkill3 = (EditText)findViewById(R.id.editTextSkill3);
        editTextSkill4 = (EditText)findViewById(R.id.editTextSkill4);
        editTextSkill5 = (EditText)findViewById(R.id.editTextSkill5);
        buttonSave = (Button)findViewById(R.id.buttonSave);

        getUserSkills();

        buttonSave.setOnClickListener(this);
    }

    public void addSkills(){
        final String uid = mAuth.getCurrentUser().getUid();

        HashMap<String, String> map = new HashMap<>();
        map.put("skill1", editTextSkill1.getText().toString());
        map.put("skill2", editTextSkill2.getText().toString());
        map.put("skill3", editTextSkill3.getText().toString());
        map.put("skill4", editTextSkill4.getText().toString());
        map.put("skill5", editTextSkill5.getText().toString());

        db.collection("skills").document(uid).set(map);
    }

    public void getUserSkills(){
        final String uid = mAuth.getCurrentUser().getUid();

        db.collection("skills").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                editTextSkill1.setText(task.getResult().get("skill1").toString());
                editTextSkill2.setText(task.getResult().get("skill2").toString());
                editTextSkill3.setText(task.getResult().get("skill3").toString());
                editTextSkill4.setText(task.getResult().get("skill4").toString());
                editTextSkill5.setText(task.getResult().get("skill5").toString());
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == buttonSave.getId()){
            addSkills();
            startActivity(new Intent(SkillsActivity.this, EditEmployeeActivity.class));
        }
    }
}