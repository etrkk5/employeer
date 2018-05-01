package projects.etrkk5.employeer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import projects.etrkk5.employeer.Profiles.employee;

public class ProfileEmployeeActivity extends AppCompatActivity implements View.OnClickListener{

    TextView textViewNameSurname, textViewLocation, textViewProfession, textViewShowAge, textViewShowExperience, textViewShowEmail, textViewShowPhone;
    TextView textViewSkill1;
    ImageView imageViewEdit, imageViewSearch, imageViewSignOut;
    CircleImageView profilePicture;
    String URL;

    private FirebaseFirestore db;
    private FirebaseDatabase fbDb;
    private FirebaseAuth mAuth;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Uri filePath;
    public static final int PICK_IMAGE = 1;


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
        imageViewSearch = (ImageView)findViewById(R.id.imageViewSearch);
        imageViewSignOut = (ImageView)findViewById(R.id.imageViewSignOut);
        profilePicture = (CircleImageView)findViewById(R.id.profilePicture);

        textViewSkill1 = (TextView)findViewById(R.id.textViewSkill1);

        db= FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        fbDb = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        imageViewEdit.setOnClickListener(this);
        imageViewSearch.setOnClickListener(this);
        imageViewSignOut.setOnClickListener(this);
        profilePicture.setOnClickListener(this);

        getUserInformations();
        getProfilePicture();
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

    public void getProfilePicture(){
        final String uid = mAuth.getCurrentUser().getUid();
        fbDb.getReference().child("Users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("profilePictureURL")){
                    fbDb.getReference().child("Users").child(uid).child("profilePictureURL").addValueEventListener(new ValueEventListener() {
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

    public void choseImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    public void uploadImage(){
        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            String uid = mAuth.getCurrentUser().getUid();
                            progressDialog.dismiss();
                            Toast.makeText(ProfileEmployeeActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            URL = taskSnapshot.getDownloadUrl().toString();
                            fbDb.getReference().child("Users").child(uid).child("profilePictureURL").setValue(URL);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(ProfileEmployeeActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                profilePicture.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            uploadImage();
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == imageViewEdit.getId()){
            Intent intent = new Intent(ProfileEmployeeActivity.this, EditEmployeeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        if(v.getId() == imageViewSearch.getId()){
            Intent intent = new Intent(ProfileEmployeeActivity.this, SearchActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        if(v.getId() == imageViewSignOut.getId()){
            mAuth.signOut();
            Intent intent = new Intent(ProfileEmployeeActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        if(v.getId() == profilePicture.getId()){
            choseImage();
        }
    }
}
