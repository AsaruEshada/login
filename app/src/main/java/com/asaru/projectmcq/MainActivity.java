package com.asaru.projectmcq;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {
    private static int Splash=4000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_main);
        Context context=this;
        new Handler().postDelayed(new Runnable() {
                                      @Override
                                      public void run() {
                                          Intent i =new Intent(context,LogIn.class);
                                          startActivity(i);
                                          finish();
                                      }
                                  }
                ,Splash);

    }
}