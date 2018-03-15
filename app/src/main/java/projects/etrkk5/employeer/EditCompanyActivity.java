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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import projects.etrkk5.employeer.Profiles.company;

public class EditCompanyActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    EditText editTextCompanyName;
    EditText editTextCompanyLocation;
    EditText editTextCompanyEmail;
    EditText editTextCompanyPhone;
    EditText editTextCompanyDescription;
    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_company);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        editTextCompanyName = (EditText)findViewById(R.id.editTextCompanyName);
        editTextCompanyLocation = (EditText)findViewById(R.id.editTextCompanyLocation);
        editTextCompanyEmail = (EditText)findViewById(R.id.editTextCompanyEmail);
        editTextCompanyPhone = (EditText)findViewById(R.id.editTextCompanyPhone);
        editTextCompanyDescription = (EditText)findViewById(R.id.editTextCompanyDescription);
        save = (Button)findViewById(R.id.buttonSave);

        save.setOnClickListener(this);

        getCompanyInformations();
    }

    public void getCompanyInformations(){
        final String cid = mAuth.getCurrentUser().getUid();
        final DocumentReference docRef = db.collection("company").document(cid);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                company company = documentSnapshot.toObject(company.class);
                String companyName = company.getCompanyName();
                String companyLocation = company.getCompanyLocation();
                String companyEmail = company.getCompanyEmail();
                String companyPhone = company.getCompanyPhone();
                String companyDescription = company.getCompanyDescription();

                editTextCompanyName.setText(companyName);
                editTextCompanyLocation.setText(companyLocation);
                editTextCompanyEmail.setText(companyEmail);
                editTextCompanyPhone.setText(companyPhone);
                editTextCompanyDescription.setText(companyDescription);
            }
        });
    }

    public void companyEdit(){
        String companyName = editTextCompanyName.getText().toString().trim();
        String companyLocation = editTextCompanyLocation.getText().toString().trim();
        String companyEmail = editTextCompanyEmail.getText().toString().trim();
        String companyPhone = editTextCompanyPhone.getText().toString().trim();
        String companyDescription = editTextCompanyDescription.getText().toString().trim();

        final String cid = mAuth.getCurrentUser().getUid();

        HashMap<String, String> companyMap = new HashMap();
        companyMap.put("companyName", companyName);
        companyMap.put("companyLocation", companyLocation);
        companyMap.put("companyEmail", companyEmail);
        companyMap.put("companyPhone", companyPhone);
        companyMap.put("companyDescription", companyDescription);

        db.collection("company").document(cid).set(companyMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getApplicationContext(), "Company updated succesfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EditCompanyActivity.this, ProfileCompanyActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == save.getId()){
            companyEdit();
        }
    }
}
