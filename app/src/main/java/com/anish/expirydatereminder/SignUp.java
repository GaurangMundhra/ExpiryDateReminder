package com.anish.expirydatereminder;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUp extends AppCompatActivity {

    private FirebaseAuth auth;
    private TextInputLayout usernameInput;
    private TextInputLayout emailInput;
    private TextInputLayout passwordInput;
    private Button signUpButton;
    private Button loginScreenButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();

        // Initialize UI elements
        usernameInput = findViewById(R.id.username);
        emailInput = findViewById(R.id.email);
        passwordInput = findViewById(R.id.password);
        signUpButton = findViewById(R.id.signup_button);
        loginScreenButton = findViewById(R.id.login_screen);

        signUpButton.setOnClickListener(v -> {
            String email = emailInput.getEditText().getText().toString().trim();
            String password = passwordInput.getEditText().getText().toString().trim();

            if (!email.isEmpty() && !password.isEmpty()) {
                signUpUser(email, password);
            } else {
                Toast.makeText(SignUp.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            }
        });

        loginScreenButton.setOnClickListener(v -> startActivity(new Intent(SignUp.this, Login.class)));
    }

    private void signUpUser(String email, String password) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign up success, update UI with the signed-in user's information
                        Toast.makeText(SignUp.this, "Sign up successful!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignUp.this, Login.class));
                        finish();
                    } else {
                        // If sign up fails, display a message to the user.
                        Toast.makeText(SignUp.this, "Sign up failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
