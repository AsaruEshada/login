package com.asaru.projectmcq;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

public class Profile extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private static final int Pick_Image=100;
    private int Request_file=222;
    public Uri uri;
    private String stringPath;
    private Intent iData;
    public ImageView profile ,upload;
    public String userName;
    public  String userEmail;


    private CallbackManager mcallbackManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        TextView email=findViewById(R.id.email);
        TextView firstName=findViewById(R.id.first);

        upload=findViewById(R.id.imageView4);
         profile=findViewById(R.id.imageView2);
        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser User =firebaseAuth.getCurrentUser();
        String Uemail=User.getEmail();

        //email.setText(user.getDisplayName().toString());

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    //Toast.makeText(Profile.this,"Email is"+ ds.child("email"),Toast.LENGTH_LONG).show();


                   if(ds.child("email").getValue().equals(User.getEmail())){
                        userName=ds.child("firstName").getValue(String.class);
                        userEmail=User.getEmail();
                        firstName.setText(userName);
                        email.setText(userEmail);

                   }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               openGallery();
            }
        });


    }
    private  void openGallery(){
        Intent gallery=new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, Pick_Image);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK && requestCode==Pick_Image){
            uri=data.getData();
            String image= uri.getPath();
            profile.setImageURI(uri);
            DatabaseReference reference= FirebaseDatabase.getInstance().getReference("users").child(userName);
            reference.child("photoUrl").setValue(image);


      }
    }
}