package com.example.anonymouscouncellingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {
    EditText username;
    EditText mail;
    EditText pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void registerUser(View view) {
        String text_username, text_password, text_email;

        username = findViewById(R.id.user_username);
        mail = findViewById(R.id.user_email);
        pass = findViewById(R.id.user_password);

        text_username = username.getText().toString();
        text_email = mail.getText().toString();
        text_password = pass.getText().toString();

        boolean isValid = validateData(text_username, text_email, text_password);
    }

    private boolean validateData(String username, String email, String password) {
        return !username.isEmpty() && !email.isEmpty() && !password.isEmpty();
    }

    public void toLoginScreen(View view) {
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
    }
}