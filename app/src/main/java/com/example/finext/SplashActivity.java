package com.example.finext;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends  androidx.appcompat.app.AppCompatActivity{

    // --Commented out by Inspection (7/28/23, 10:03 AM):FirebaseUser currentUser;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        new Handler().postDelayed(() -> {
            FirebaseUser user = mAuth.getCurrentUser();
            if (user == null) {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            } else {
                Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mainIntent);
                finish();
            }
        }, 3000);
    }
}