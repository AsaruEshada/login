package com.asaru.projectmcq;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;


public class LogIn extends AppCompatActivity {



    private FirebaseAuth mfirebaseAuth;
    public String userEmail;
    private CallbackManager mcallbackManager;
    private static final String TAG="FacebookAuthentication";
    public  Context context;
    private FirebaseAuth.AuthStateListener authStateListener;
    private AccessTokenTracker accessTokenTracker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        TextView Sign_in=findViewById(R.id.sign);
        EditText Email=findViewById(R.id.email);
        EditText Password=findViewById(R.id.password);
        mfirebaseAuth=FirebaseAuth.getInstance();
        FacebookSdk.sdkInitialize(getApplicationContext());
        Button LogIn=findViewById(R.id.login);
        LoginButton loginButton=findViewById(R.id.login_button);
         context=this   ;
        mcallbackManager= CallbackManager.Factory.create();
        loginButton.setReadPermissions("email","public_profile");


       // LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
        loginButton.registerCallback(mcallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG,"OnSuccess" + loginResult);
                handleFacebookToken(loginResult.getAccessToken());
                FirebaseUser user=mfirebaseAuth.getCurrentUser();

            }

            @Override
            public void onCancel() {
                Log.d(TAG,"OnCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG,"OnError" + error);
            }
        });
        authStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
          FirebaseUser user=firebaseAuth.getCurrentUser();
          if(user !=null){
              update(user);
          }else{
              update(null);
          }
            }
        };
        accessTokenTracker=new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
              if(currentAccessToken ==null){
                  mfirebaseAuth.signOut();
              }
            }
        };

        Sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogIn.this,sign_in.class));
            }

        });
// email logIn
        LogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Email.length()==0){
                    Email.setError("Enter Valid Email");
                }else{
                    if(Password.length()==0){
                        Password.setError("Enter Your Password");
                    }else{
                        mfirebaseAuth.signInWithEmailAndPassword(Email.getText().toString(),Password.getText().toString()).addOnCompleteListener(LogIn.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    startActivity(new Intent(LogIn.this,Profile.class));
                                }else{

                                }

                            }
                        });
                    }
                }

            }
        });
    }
    private  void handleFacebookToken(AccessToken token){
        Log.d(TAG,"handleFacebookToken" + token);
        AuthCredential credential= FacebookAuthProvider.getCredential(token.getToken());
        mfirebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Log.d(TAG,"sing in with credential successful");
                    FirebaseUser user =mfirebaseAuth.getCurrentUser();
                    update(user);
                    DatabaseReference reference=FirebaseDatabase.getInstance().getReference("users");
                   FBuserHadler fb=new FBuserHadler(user.getDisplayName(),user.getEmail());
                    String name=user.getDisplayName();
                    reference.child(name).setValue(fb);


                }else{
                    Toast.makeText(LogIn.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    update(null);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mcallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void update(FirebaseUser user){
        if(user !=null){
            String name=user.getDisplayName();
            if(user.getPhotoUrl() !=null){
                String img=user.getPhotoUrl().toString();
                startActivity(new Intent(context,Profile.class));
            }
        }
    }
}