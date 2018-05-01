package projects.etrkk5.employeer;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivityForCompany extends AppCompatActivity {

    String usersId;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    TextView textViewCompanyName, textViewCompanyLocation, textViewCompanyPhone, textViewCompanyDescription;
    final static String TAG = "message: ";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_for_company);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        Intent i = getIntent();
        Bundle b = i.getExtras();

        if(b != null){
            usersId = (String) b.getString("userId");
        }

        textViewCompanyName = (TextView)findViewById(R.id.textViewCompanyName);
        textViewCompanyLocation = (TextView)findViewById(R.id.textViewCompanyLocation);
        textViewCompanyPhone = (TextView)findViewById(R.id.textViewShowCompanyPhone);
        textViewCompanyDescription = (TextView)findViewById(R.id.textViewShowCompanyDescription);

        getCompany(usersId);
    }

    public void getCompany(String userId){
        db.collection("company").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                textViewCompanyName.setText(task.getResult().get("companyName").toString());
                textViewCompanyLocation.setText(task.getResult().get("companyLocation").toString());
                textViewCompanyPhone.setText(task.getResult().get("companyPhone").toString());
                textViewCompanyDescription.setText(task.getResult().get("companyDescription").toString());
            }
        });

    }
}
