package projects.etrkk5.employeer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class SignUpCompanyActivity extends AppCompatActivity implements View.OnClickListener{

    EditText editTextCompanyName;
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
        setContentView(R.layout.activity_sign_up_company);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        fbDb = FirebaseDatabase.getInstance();

        textViewLogin = findViewById(R.id.textViewSignUp);
        buttonSignUp = findViewById(R.id.buttonSignUp);
        editTextCompanyName = findViewById(R.id.editTextCompanyName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        progressBar = findViewById(R.id.progressbar);

        textViewLogin.setOnClickListener(this);
        buttonSignUp.setOnClickListener(this);
    }

    public void RegisterCompany(){
        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        final String companyName = editTextCompanyName.getText().toString().trim();

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

        if(companyName.isEmpty()){
            editTextCompanyName.setError("Company name is required");
            editTextCompanyName.requestFocus();
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
                    final String cid = currentUser.getUid();

                    HashMap<String, String> companyMap = new HashMap<>();
                    companyMap.put("companyName", companyName);
                    companyMap.put("companyEmail", email);
                    companyMap.put("companyId", cid);
                    companyMap.put("companyPhone", "");
                    companyMap.put("userType", "company");
                    companyMap.put("companyLocation", "");
                    companyMap.put("companyDescription", "");

                    db.collection("company").document(cid).set(companyMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            DatabaseReference mRef =  fbDb.getReference().child("Users").child(cid);
                            mRef.child("usersType").setValue("company");

                            Toast.makeText(getApplicationContext(), "Company registered succesfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignUpCompanyActivity.this, ProfileCompanyActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    });


                    HashMap<String, String> map1 = new HashMap<>();
                    map1.put("title", "");
                    map1.put("description", "");
                    map1.put("location", "");
                    db.collection("company-ads").document(cid).collection("ads").document("ad1").set(map1);

                }else{
                    if(task.getException() instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(getApplicationContext(),"Company is already registered!", Toast.LENGTH_SHORT).show();
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
            RegisterCompany();
        }
        if(v.getId() == textViewLogin.getId()){
            startActivity(new Intent(SignUpCompanyActivity.this, LoginActivity.class));
        }

    }
}
