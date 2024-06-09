package com.anish.expirydatereminder;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    Button callSignUp, login_btn;
    ImageView image;
    TextView logoText, sloganText;
    TextInputLayout username, password;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This line will hide the status bar from the screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();

        // Hooks
        callSignUp = findViewById(R.id.signup_screen);
        image = findViewById(R.id.logo_image);
        logoText = findViewById(R.id.logo_name);
        sloganText = findViewById(R.id.slogan_name);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login_btn = findViewById(R.id.login_button);

        callSignUp.setOnClickListener(view -> {
            Intent intent = new Intent(Login.this, SignUp.class);
            Pair[] pairs = new Pair[7];
            pairs[0] = new Pair<>(image, "logo_image");
            pairs[1] = new Pair<>(logoText, "logo_text");
            pairs[2] = new Pair<>(sloganText, "logo_desc");
            pairs[3] = new Pair<>(username, "username_tran");
            pairs[4] = new Pair<>(password, "password_tran");
            pairs[5] = new Pair<>(login_btn, "button_tran");
            pairs[6] = new Pair<>(callSignUp, "login_signup_tran");

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Login.this, pairs);
                startActivity(intent, options.toBundle());
            }
        });

        login_btn.setOnClickListener(v -> {
            String email = username.getEditText().getText().toString().trim();
            String pass = password.getEditText().getText().toString().trim();

            if (!email.isEmpty() && !pass.isEmpty()) {
                loginUser(email, pass);
            } else {
                Toast.makeText(Login.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loginUser(String email, String password) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Login success, update UI with the signed-in user's information
                        FirebaseUser user = auth.getCurrentUser();
                        Toast.makeText(Login.this, "Login successful!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Login.this, DashboardActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // If login fails, display a message to the user.
                        Toast.makeText(Login.this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
