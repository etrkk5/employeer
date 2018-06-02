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

public class ProfileActivityForEmployee extends AppCompatActivity {
    String usersId;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    FirebaseDatabase fbDb;
    final static String TAG = "message: ";

    TextView textViewAge, textViewEmail, textViewPhone, textViewNameSurname, textViewLocation, textViewProfession, textViewSkills;
    CircleImageView profilePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_for_employee);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        fbDb = FirebaseDatabase.getInstance();

        Intent i = getIntent();
        Bundle b = i.getExtras();

        if(b != null){
            usersId = (String) b.getString("userId");
        }
        textViewSkills = findViewById(R.id.textViewSkill1);
        textViewAge = findViewById(R.id.textViewShowAge);
        textViewEmail = findViewById(R.id.textViewShowEmail);
        textViewPhone = findViewById(R.id.textViewShowPhone);
        textViewNameSurname = findViewById(R.id.textViewNameSurname);
        textViewLocation = findViewById(R.id.textViewLocation);
        textViewProfession = findViewById(R.id.textViewProfession);
        profilePicture = findViewById(R.id.profilePicture);

        getUser(usersId);

    }

    public void getUser(final String userId){
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
