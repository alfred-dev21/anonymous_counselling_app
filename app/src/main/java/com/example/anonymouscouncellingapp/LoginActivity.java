package com.example.anonymouscouncellingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;


public class LoginActivity extends AppCompatActivity {
    EditText email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.client_username);
        password = findViewById(R.id.client_password);

    }

    public void toRegistration(View view) {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        finish();
    }

    public void loginUser(View view) {
        String email = this.email.getText().toString();
        String password = this.password.getText().toString();

        if (!email.isEmpty() && !password.isEmpty()){
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();
        }
    }
}