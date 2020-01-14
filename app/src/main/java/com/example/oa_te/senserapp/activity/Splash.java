package com.example.oa_te.senserapp.activity;

import android.content.Intent;
import android.os.Handler;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.oa_te.senserapp.authentication.LoginActivity;

import com.example.oa_te.senserapp.R;

public class Splash extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeIntent = new Intent(Splash.this, LoginActivity.class);
                startActivity(homeIntent);
            }
        },SPLASH_TIME_OUT);

    }
}
