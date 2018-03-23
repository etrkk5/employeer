package projects.etrkk5.employeer;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.HashMap;

import projects.etrkk5.employeer.Profiles.employee;

public class EditEmployeeActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    EditText editTextEmployeeName;
    EditText editTextEmployeeSurname;
    EditText editTextEmployeeEmail;
    EditText editTextEmployeePhone;
    EditText editTextEmployeeProfession;
    EditText editTextEmployeeLocation;
    EditText editTextEmployeeAge;
    Button save, buttonSkills;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_employee);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        editTextEmployeeName = (EditText)findViewById(R.id.editTextEmployeeName);
        editTextEmployeeSurname = (EditText)findViewById(R.id.editTextEmployeeSurname);
        editTextEmployeeEmail = (EditText)findViewById(R.id.editTextEmployeeEmail);
        editTextEmployeePhone = (EditText)findViewById(R.id.editTextEmployeePhone);
        editTextEmployeeProfession = (EditText)findViewById(R.id.editTextEmployeeProfession);
        editTextEmployeeLocation = (EditText)findViewById(R.id.editTextEmployeeLocation);
        editTextEmployeeAge = (EditText)findViewById(R.id.editTextEmployeeAge);
        buttonSkills = (Button)findViewById(R.id.buttonSkills) ;
        save = (Button)findViewById(R.id.buttonSave);

        save.setOnClickListener(this);
        buttonSkills.setOnClickListener(this);

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
                String employeePhone = employee.getEmployeePhone();
                String employeeProfession = employee.getEmployeeProfession();
                String employeeLocation = employee.getEmployeeLocation();
                String employeeAge = employee.getEmployeeAge();

                editTextEmployeeName.setText(employeeName);
                editTextEmployeeSurname.setText(employeeSurname);
                editTextEmployeeEmail.setText(employeeEmail);
                editTextEmployeePhone.setText(employeePhone);
                editTextEmployeeProfession.setText(employeeProfession);
                editTextEmployeeLocation.setText(employeeLocation);
                editTextEmployeeAge.setText(employeeAge);
            }
        });
    }

    public void userEdit(){

        String employeeName = editTextEmployeeName.getText().toString().trim();
        String employeeSurname = editTextEmployeeSurname.getText().toString().trim();
        String employeeEmail = editTextEmployeeEmail.getText().toString().trim();
        String employeePhone = editTextEmployeePhone.getText().toString().trim();
        String employeeProfession = editTextEmployeeProfession.getText().toString().trim();
        String employeeLocation = editTextEmployeeLocation.getText().toString().trim();

        final String eid = mAuth.getCurrentUser().getUid();
        HashMap<String, String> userMap = new HashMap<>();

        if(!editTextEmployeeAge.getText().toString().equals("")){
            int employeeAge = Integer.parseInt(editTextEmployeeAge.getText().toString());
            if(employeeAge>18 && employeeAge<100){
                String employeesAge = Integer.toString(employeeAge);
                userMap.put("employeeAge", employeesAge);
            }else{
                editTextEmployeeAge.setError("Please enter a valid age(18 to 100)");
                editTextEmployeeAge.requestFocus();
            }

        }
            userMap.put("employeeName", employeeName);
            userMap.put("employeeSurname", employeeSurname);
            userMap.put("employeeEmail", employeeEmail);
            userMap.put("employeeLocation", employeeLocation);
            userMap.put("employeeProfession", employeeProfession);
            userMap.put("employeePhone", employeePhone);

            db.collection("employee").document(eid).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    Toast.makeText(getApplicationContext(), "User updated succesfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EditEmployeeActivity.this, ProfileEmployeeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                }
            });

        }


    @Override
    public void onClick(View v) {
        if(v.getId() == save.getId()){
            userEdit();
        }
        if(v.getId() == buttonSkills.getId()){
            Intent intent = new Intent(EditEmployeeActivity.this, SkillsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }
}
