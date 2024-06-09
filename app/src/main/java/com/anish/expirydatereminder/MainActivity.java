package com.anish.expirydatereminder;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = findViewById(R.id.textView); // Replace "textView" with your TextView's actual ID in the XML layout file
        // Enable the marquee effect
        textView.setSelected(true);

        Button loginButton = findViewById(R.id.Button02);
        loginButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
        });

        Button signUpButton = findViewById(R.id.Button03);
        signUpButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SignUp.class);
            startActivity(intent);
        });

        ImageButton instagramButton = findViewById(R.id.imageView5);
        instagramButton.setOnClickListener(v -> {
            // Replace "your_instagram_username" with your Instagram username
            String instagramUsername = "coup.earn";
            Uri uri = Uri.parse("https://www.instagram.com/" + instagramUsername);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });
    }
}
