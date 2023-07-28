package com.example.finext;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private EditText mEmailEditText;
    private EditText mPasswordEditText;

    private FirebaseAuth mAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEmailEditText = findViewById(R.id.register_email);
        mPasswordEditText = findViewById(R.id.register_password);
        Button mSignUpButton = findViewById(R.id.register_button);
        TextView mLoginTextView = findViewById(R.id.sign_button);

        mAuth = FirebaseAuth.getInstance();

        mSignUpButton.setOnClickListener(v -> {
            String email = mEmailEditText.getText().toString();
            String password = mPasswordEditText.getText().toString();

            if (TextUtils.isEmpty(email)) {
                mEmailEditText.setError("Email is required.");
                return;
            }

            if (TextUtils.isEmpty(password)) {
                mPasswordEditText.setError("Password is required.");
                return;
            }

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Sign up success, update UI with the signed-up user's information
                            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                            finish();
                        } else {
                            // If sign up fails, display a message to the user.
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        mLoginTextView.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });
    }
}
