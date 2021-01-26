package com.asaru.projectmcq;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DatabaseRegistrar;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class sign_in extends AppCompatActivity {
   private FirebaseAuth firebaseAuth;
   FirebaseDatabase firebaseDatabase;
   DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ImageView back=findViewById(R.id.back);
        EditText User_name=findViewById(R.id.name);
        EditText User_second=findViewById(R.id.email);
        EditText User_email=findViewById(R.id.password);
        EditText User_password=findViewById(R.id.password_2);

        Context context=this;

        Button Sign =findViewById(R.id.confirm);

        firebaseAuth=FirebaseAuth.getInstance();
        //back button
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context,LogIn.class));
            }
        });
        //sing button
        Sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (User_name.length() == 0) {
                    User_name.setError("This Field Can't Be Empty");
                } else {
                    if (User_second.length() == 0) {
                        User_second.setError("This Field Can't Be Empty ");
                    } else {
                        if (User_email.length() == 0) {
                            User_email.setError("This Field Can't Be Empty");
                        } else {
                            if (!Patterns.EMAIL_ADDRESS.matcher(User_email.getText().toString().trim()).matches()) {
                                User_email.setError("enter valid email");
                            }else{
                                if (User_password.length() == 0) {
                                    User_password.setError("This Field Can't be Empty ");
                                } else {
                                    String UserEmail = User_email.getText().toString().trim();
                                    String UserPassword1 = User_password.getText().toString().trim();
                                    firebaseAuth.createUserWithEmailAndPassword(UserEmail, UserPassword1)
                                            .addOnCompleteListener(sign_in.this, new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    if (task.isComplete()) {
                                                        firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(sign_in.this, new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isComplete()) {
                                                                    Toast.makeText(sign_in.this, "Sign Up Complete Check  your Email", Toast.LENGTH_LONG).show();
                                                                    startActivity(new Intent(sign_in.this, LogIn.class));
                                                                    //update database
                                                                    firebaseDatabase=FirebaseDatabase.getInstance();
                                                                    databaseReference=firebaseDatabase.getReference("users");
                                                                    String userName=User_name.getText().toString()+" "+User_second.getText().toString();
                                                                    //String imageUri="https://www.bing.com/images/search?view=detailV2&ccid=9XKP%2bU%2fY&id=B21C8A779791C6060646C4FC0013B49F4A3288A7&thid=OIP.9XKP-U_YpNZSs9sGPn3jbQAAAA&mediaurl=https%3a%2f%2fpecb.com%2fconferences%2fwp-content%2fuploads%2f2017%2f10%2fno-profile-picture-300x216.jpg&exph=216&expw=300&q=no+profile+picture+image&simid=608038095513257571&ck=DAF69E0769B9543FE7DE7F79A167B4AC&selectedIndex=4&FORM=IRPRST";
                                                                    userhadler addData=new userhadler(userName,User_email.getText().toString());
                                                                    databaseReference.child(userName).setValue(addData);

                                                                } else {
                                                                    Toast.makeText(sign_in.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                                                }
                                                            }
                                                        });
                                                    } else {
                                                        Toast.makeText(sign_in.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                    }

                                                }
                                            });



                                }
                            }
                        }
                    }
                }

            }

            });

    }
}