package com.anish.expirydatereminder;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ContactFormActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextEmail;
    private EditText editTextSubject;
    private EditText editTextMessage;
    private Button callButton;
    private Button submitButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_form);

        ImageButton backButton = findViewById(R.id.backButton13);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(ContactFormActivity.this, DashboardActivity.class);
            startActivity(intent);
            finish();
        });

        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextSubject = findViewById(R.id.editTextSubject);
        editTextMessage = findViewById(R.id.editTextMessage);
        callButton = findViewById(R.id.callButton);
        submitButton = findViewById(R.id.submitButton);

        callButton.setOnClickListener(v -> makePhoneCall());
        submitButton.setOnClickListener(v -> submitContactForm());
    }

    private void makePhoneCall() {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:+91 90453 77277")); // Replace with actual phone number
        startActivity(callIntent);
    }

    private void submitContactForm() {
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String subject = editTextSubject.getText().toString().trim();
        String message = editTextMessage.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || subject.isEmpty()) {
            Toast.makeText(this, "Please fill out all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("ContactFormActivity", "Creating email intent");

        // Send email
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"gandhidhruv220605@gmail.com"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Contact Form Submission");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Name: " + name + "\nEmail: " + email + "\nSubject: " + subject + "\nMessage: " + message);
        emailIntent.setPackage("com.google.android.gm"); // Explicitly set Gmail package


        try {
            Log.d("ContactFormActivity", "Starting email intent");
            startActivity(Intent.createChooser(emailIntent, "Send email..."));
            Toast.makeText(this, "Contact Form Submitted", Toast.LENGTH_SHORT).show();
            // Redirect to HomeActivity
            Intent homeIntent = new Intent(ContactFormActivity.this, DashboardActivity.class);
            startActivity(homeIntent);
            finish();
        } catch (android.content.ActivityNotFoundException ex) {
            Log.e("ContactFormActivity", "No email clients installed.", ex);
            Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

}