package projects.etrkk5.employeer;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpEmployeeActivity extends AppCompatActivity implements View.OnClickListener{

    EditText editTextName;
    EditText editTextSurname;
    EditText editTextEmail;
    EditText editTextPassword;
    TextView textViewLogin;
    Button buttonSignUp;
    ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseDatabase fbDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_employee);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        fbDb = FirebaseDatabase.getInstance();

        textViewLogin = (TextView)findViewById(R.id.textViewSignUp);
        buttonSignUp = (Button)findViewById(R.id.buttonSignUp);
        editTextName = (EditText)findViewById(R.id.editTextName);
        editTextSurname = (EditText)findViewById(R.id.editTextSurname);
        editTextEmail = (EditText)findViewById(R.id.editTextEmail);
        editTextPassword = (EditText)findViewById(R.id.editTextPassword);
        progressBar = (ProgressBar)findViewById(R.id.progressbar);

        textViewLogin.setOnClickListener(this);
        buttonSignUp.setOnClickListener(this);
    }

    public void RegisterUser(){
        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        final String name = editTextName.getText().toString().trim();
        final String surname = editTextSurname.getText().toString().trim();

        if(email.isEmpty()){
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Please enter a valid email");
            editTextEmail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }

        if(name.isEmpty()){
            editTextName.setError("Name is required");
            editTextName.requestFocus();
            return;
        }

        if(surname.isEmpty()){
            editTextSurname.setError("Surname is required");
            editTextSurname.requestFocus();
            return;
        }

        if(password.length() < 6){
            editTextPassword.setError("Minimum length of password should be 6!");
            editTextPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if(task.isSuccessful()){

                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    final String eid = currentUser.getUid();

                    HashMap<String, String> userMap = new HashMap<>();
                    userMap.put("employeeName", name);
                    userMap.put("employeeSurname", surname);
                    userMap.put("employeeEmail", email);
                    userMap.put("employeeId", eid);
                    userMap.put("employeeAge", "");
                    userMap.put("userType", "employee");
                    userMap.put("employeeLocation", "");
                    userMap.put("employeeProfession", "");
                    userMap.put("employeePhone", "");
                    userMap.put("employeeExperience", "");

                    db.collection("employee").document(eid).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            DatabaseReference mRef =  fbDb.getReference().child("Users").child(eid);
                            mRef.child("usersType").setValue("employee");

                            Toast.makeText(getApplicationContext(), "User registered succesfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignUpEmployeeActivity.this, ProfileEmployeeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    });

                    HashMap<String, String> map = new HashMap<>();
                    map.put("skill1", "");
                    map.put("skill2", "");
                    map.put("skill3", "");
                    map.put("skill4", "");
                    map.put("skill5", "");
                    db.collection("skills").document(eid).set(map);

                }else{
                    if(task.getException() instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(getApplicationContext(),"User is already registered!", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {

        if(v.getId() == buttonSignUp.getId()){
            RegisterUser();
        }
        if(v.getId() == textViewLogin.getId()){
            startActivity(new Intent(SignUpEmployeeActivity.this, LoginActivity.class));
        }

    }
}
