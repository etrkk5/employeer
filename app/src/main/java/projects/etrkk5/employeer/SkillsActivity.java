package projects.etrkk5.employeer;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class SkillsActivity extends AppCompatActivity implements View.OnClickListener{

    EditText editTextSkill1, editTextSkill2, editTextSkill3, editTextSkill4, editTextSkill5, editTextSkill6, editTextSkill7, editTextSkill8;
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
        editTextSkill6 = (EditText)findViewById(R.id.editTextSkill6);
        editTextSkill7 = (EditText)findViewById(R.id.editTextSkill7);
        editTextSkill8 = (EditText)findViewById(R.id.editTextSkill8);
        buttonSave = (Button)findViewById(R.id.buttonSave);

        buttonSave.setOnClickListener(this);
    }

    public void setSkills(){
        final String skill1 = editTextSkill1.getText().toString();
        final String skill2 = editTextSkill2.getText().toString();
        final String skill3 = editTextSkill3.getText().toString();
        final String skill4 = editTextSkill4.getText().toString();
        final String skill5 = editTextSkill5.getText().toString();
        final String skill6 = editTextSkill6.getText().toString();
        final String skill7 = editTextSkill7.getText().toString();
        final String skill8 = editTextSkill8.getText().toString();
        final String uid = mAuth.getCurrentUser().getUid();

                Map<String, Object> map = new HashMap<>();
                map.put(skill1, true);
                map.put(skill2, true);
                map.put(skill3, true);
                map.put(skill4, true);
                map.put(skill5, true);
                map.put(skill6, true);
                map.put(skill7, true);
                map.put(skill8, true);
                db.collection("skills").document(uid).update("skills", map);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == buttonSave.getId()){
            setSkills();
            startActivity(new Intent(SkillsActivity.this, EditEmployeeActivity.class));
        }
    }
}