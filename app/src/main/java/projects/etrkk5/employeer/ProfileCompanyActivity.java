package projects.etrkk5.employeer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import projects.etrkk5.employeer.Profiles.company;

public class ProfileCompanyActivity extends AppCompatActivity implements View.OnClickListener{

    TextView textViewCompanyName;
    TextView textViewCompanyLocation;
    TextView textViewShowCompanyDescription;
    TextView textViewShowCompanyPhone;
    ImageView imageViewEdit;
    ImageView imageViewSearch;
    ImageView imageViewPostAd;
    ImageView imageViewSignOut;
    CircleImageView profilePicture;
    String URL;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseDatabase fbDb;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Uri filePath;
    public static final int PICK_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_company);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        fbDb = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        textViewCompanyName = findViewById(R.id.textViewCompanyName);
        textViewCompanyLocation = findViewById(R.id.textViewCompanyLocation);
        textViewShowCompanyDescription = findViewById(R.id.textViewShowCompanyDescription);
        textViewShowCompanyPhone = findViewById(R.id.textViewShowCompanyPhone);
        imageViewEdit = findViewById(R.id.imageViewEdit);
        imageViewSearch = findViewById(R.id.imageViewSearch);
        imageViewPostAd = findViewById(R.id.imageViewPostAd);
        imageViewSignOut = findViewById(R.id.imageViewSignOut);
        profilePicture = findViewById(R.id.profilePicture);

        imageViewEdit.setOnClickListener(this);
        imageViewSearch.setOnClickListener(this);
        imageViewPostAd.setOnClickListener(this);
        imageViewSignOut.setOnClickListener(this);
        profilePicture.setOnClickListener(this);

        getCompanyInfortmations();
        getProfilePicture();
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
                            Toast.makeText(ProfileCompanyActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            URL = taskSnapshot.getDownloadUrl().toString();
                            fbDb.getReference().child("Users").child(uid).child("profilePictureURL").setValue(URL);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(ProfileCompanyActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
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
        if(v.getId() == imageViewSignOut.getId()){
            mAuth.signOut();
            Intent intent = new Intent(ProfileCompanyActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        if(v.getId() == profilePicture.getId()){
            choseImage();
        }
    }
}
