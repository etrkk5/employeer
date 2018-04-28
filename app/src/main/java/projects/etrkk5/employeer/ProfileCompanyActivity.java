package projects.etrkk5.employeer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import projects.etrkk5.employeer.Profiles.company;

public class ProfileCompanyActivity extends AppCompatActivity implements View.OnClickListener{

    TextView textViewCompanyName;
    TextView textViewCompanyLocation;
    TextView textViewShowCompanyDescription;
    TextView textViewShowCompanyPhone;
    ImageView imageViewEdit;
    ImageView imageViewSearch;
    ImageView imageViewPostAd;
    ImageView imageViewMessages;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_company);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        textViewCompanyName = (TextView)findViewById(R.id.textViewCompanyName);
        textViewCompanyLocation = (TextView)findViewById(R.id.textViewCompanyLocation);
        textViewShowCompanyDescription = (TextView)findViewById(R.id.textViewShowCompanyDescription);
        textViewShowCompanyPhone = (TextView)findViewById(R.id.textViewShowCompanyPhone);
        imageViewEdit = (ImageView)findViewById(R.id.imageViewEdit);
        imageViewSearch = (ImageView)findViewById(R.id.imageViewSearch);
        imageViewPostAd = (ImageView)findViewById(R.id.imageViewPostAd);
        imageViewMessages = (ImageView)findViewById(R.id.imageViewMessages);

        imageViewEdit.setOnClickListener(this);
        imageViewSearch.setOnClickListener(this);
        imageViewPostAd.setOnClickListener(this);
        imageViewMessages.setOnClickListener(this);

        getCompanyInfortmations();
    }

    public void getCompanyInfortmations(){
        final String cid = mAuth.getCurrentUser().getUid();
        final DocumentReference docRef = db.collection("company").document(cid);
        Log.e("message", "message");
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                company company = documentSnapshot.toObject(company.class);
                String companyName = company.getCompanyName();
                String companyLocation = company.getCompanyLocation();
                String companyDescription = company.getCompanyDescription();
                String companyPhone = company.getCompanyPhone();

                textViewCompanyName.setText(companyName);
                textViewCompanyLocation.setText(companyLocation);
                textViewShowCompanyDescription.setText(companyDescription);
                textViewShowCompanyPhone.setText(companyPhone);
            }
        });
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == imageViewEdit.getId()){
            Intent intent = new Intent(ProfileCompanyActivity.this, EditCompanyActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        if(v.getId() == imageViewSearch.getId()){
            Intent intent = new Intent(ProfileCompanyActivity.this, SearchActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        if(v.getId() == imageViewPostAd.getId()){
            Intent intent = new Intent(ProfileCompanyActivity.this, CompanyPostAdActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        if(v.getId() == imageViewMessages.getId()){
            Intent intent = new Intent(ProfileCompanyActivity.this, CompanyMessagesActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }
}
