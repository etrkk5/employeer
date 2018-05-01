package projects.etrkk5.employeer;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivityForEmployee extends AppCompatActivity {
    String usersId;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    final static String TAG = "message: ";

    TextView textViewAge, textViewEmail, textViewPhone, textViewNameSurname, textViewLocation, textViewProfession, textViewSkills;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_for_employee);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        Intent i = getIntent();
        Bundle b = i.getExtras();

        if(b != null){
            usersId = (String) b.getString("userId");
        }
        textViewSkills = (TextView)findViewById(R.id.textViewSkill1);
        textViewAge = (TextView)findViewById(R.id.textViewShowAge);
        textViewEmail = (TextView)findViewById(R.id.textViewShowEmail);
        textViewPhone = (TextView)findViewById(R.id.textViewShowPhone);
        textViewNameSurname = (TextView)findViewById(R.id.textViewNameSurname);
        textViewLocation = (TextView)findViewById(R.id.textViewLocation);
        textViewProfession = (TextView)findViewById(R.id.textViewProfession);

        getUser(usersId);

    }

    public void getUser(String userId){
        db.collection("employee").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                textViewAge.setText(task.getResult().get("employeeAge").toString());
                textViewEmail.setText(task.getResult().get("employeeEmail").toString());
                textViewPhone.setText(task.getResult().get("employeePhone").toString());
                textViewNameSurname.setText(task.getResult().get("employeeName").toString() + " " + task.getResult().get("employeeSurname").toString());
                textViewLocation.setText(task.getResult().get("employeeLocation").toString());
                textViewProfession.setText(task.getResult().get("employeeProfession").toString());
            }
        });
        db.collection("skills").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                textViewSkills.setText(task.getResult().get("skill1").toString() + ", " +task.getResult().get("skill2").toString() +
                        ", " +task.getResult().get("skill3").toString() + ", " +task.getResult().get("skill4").toString() +
                        ", " +task.getResult().get("skill5").toString());

            }
        });
    }
}
