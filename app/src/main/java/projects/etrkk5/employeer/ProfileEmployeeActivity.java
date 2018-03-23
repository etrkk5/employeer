package projects.etrkk5.employeer;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import projects.etrkk5.employeer.Profiles.employee;

public class ProfileEmployeeActivity extends AppCompatActivity implements View.OnClickListener{

    TextView textViewNameSurname, textViewLocation, textViewProfession, textViewShowAge, textViewShowExperience, textViewShowEmail, textViewShowPhone;
    TextView textViewSkill1;
    ImageView imageViewEdit;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_employee);

        textViewNameSurname = (TextView)findViewById(R.id.textViewNameSurname);
        textViewLocation = (TextView)findViewById(R.id.textViewLocation);
        textViewShowAge = (TextView)findViewById(R.id.textViewShowAge);
        textViewShowExperience = (TextView)findViewById(R.id.textViewShowExperience);
        textViewProfession = (TextView)findViewById(R.id.textViewProfession);
        textViewShowEmail = (TextView)findViewById(R.id.textViewShowEmail);
        textViewShowPhone = (TextView)findViewById(R.id.textViewShowPhone);
        imageViewEdit = (ImageView)findViewById(R.id.imageViewEdit);

        textViewSkill1 = (TextView)findViewById(R.id.textViewSkill1);

        db= FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        imageViewEdit.setOnClickListener(this);

        getUserInformations();

    }

    public void getUserInformations(){
        final String eid = mAuth.getCurrentUser().getUid();
        final DocumentReference docRef = db.collection("employee").document(eid);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                employee employee = documentSnapshot.toObject(employee.class);
                String employeeName = employee.getEmployeeName();
                String employeeSurname = employee.getEmployeeSurname();
                String employeeEmail = employee.getEmployeeEmail();
                String employeeAge = employee.getEmployeeAge();
                String employeeExperience = employee.getEmployeeExperience();
                String employeePhone = employee.getEmployeePhone();
                String employeeProfession = employee.getEmployeeProfession();
                String employeeLocation = employee.getEmployeeLocation();
                Log.e("message", employeeName+employeeSurname+employeeEmail);

                String employeeNameSurname = employeeName +" "+ employeeSurname;

                textViewNameSurname.setText(employeeNameSurname);
                textViewShowAge.setText(employeeAge);
                textViewShowEmail.setText(employeeEmail);
                textViewShowPhone.setText(employeePhone);
                textViewProfession.setText(employeeProfession);
                textViewShowExperience.setText(employeeExperience);
                textViewLocation.setText(employeeLocation);
            }
        });

        db.collection("skills").document(eid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                textViewSkill1.setText(task.getResult().get("skill1").toString() + ", " +task.getResult().get("skill2").toString() +
                        ", " +task.getResult().get("skill3").toString() + ", " +task.getResult().get("skill4").toString() +
                        ", " +task.getResult().get("skill5").toString());

            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == imageViewEdit.getId()){
            Intent intent = new Intent(ProfileEmployeeActivity.this, EditEmployeeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }
}
