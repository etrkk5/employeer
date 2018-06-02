package projects.etrkk5.employeer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivityForCompany extends AppCompatActivity {

    String usersId;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    FirebaseDatabase fbDb;
    TextView textViewCompanyName, textViewCompanyLocation, textViewCompanyPhone, textViewCompanyDescription;
    CircleImageView profilePicture;
    final static String TAG = "message: ";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_for_company);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        fbDb = FirebaseDatabase.getInstance();

        Intent i = getIntent();
        Bundle b = i.getExtras();

        if(b != null){
            usersId = (String) b.getString("userId");
        }

        textViewCompanyName = findViewById(R.id.textViewCompanyName);
        textViewCompanyLocation = findViewById(R.id.textViewCompanyLocation);
        textViewCompanyPhone = findViewById(R.id.textViewShowCompanyPhone);
        textViewCompanyDescription = findViewById(R.id.textViewShowCompanyDescription);
        profilePicture = findViewById(R.id.profilePicture);

        getCompany(usersId);
    }

    public void getCompany(final String userId){
        db.collection("company").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                textViewCompanyName.setText(task.getResult().get("companyName").toString());
                textViewCompanyLocation.setText(task.getResult().get("companyLocation").toString());
                textViewCompanyPhone.setText(task.getResult().get("companyPhone").toString());
                textViewCompanyDescription.setText(task.getResult().get("companyDescription").toString());
            }
        });

        fbDb.getReference().child("Users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("profilePictureURL")){
                    fbDb.getReference().child("Users").child(userId).child("profilePictureURL").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String picURL = dataSnapshot.getValue().toString();
                            Picasso.get().load(picURL).into(profilePicture);
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) { }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
