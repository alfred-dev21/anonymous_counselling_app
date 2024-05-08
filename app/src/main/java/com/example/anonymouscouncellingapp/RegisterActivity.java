package com.example.anonymouscouncellingapp;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.anonymouscouncellingapp.links.Links;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {
    EditText username, mail, pass;
    OkHttpClient client;
    RadioButton r1, r2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.user_username);
        mail = findViewById(R.id.user_email);
        pass = findViewById(R.id.user_password);
        r1 = findViewById(R.id.radioBtn1);
        r2 = findViewById(R.id.radioBtn2);
        client = new OkHttpClient();
    }

    public void registerUser(View view) {
        String text_username, text_password, text_email;
        text_username = username.getText().toString();
        text_email = mail.getText().toString();
        text_password = pass.getText().toString();

        boolean isValid = validateData(text_username, text_email, text_password);

        if (isValid){
            String userType = (r1.isChecked()) ? "client": "counselor";

            RequestBody requestBody = new FormBody.Builder()
                    .add("type", "register")
                    .add("username", text_username)
                    .add("password", text_password)
                    .add("usertype", userType)
                    .build();

            Request request = new Request.Builder()
                    .url(Links.MAIN_PHP)
                    .post(requestBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    runOnUiThread(() -> {
                        try {
                            assert response.body() != null;
                            String responseBody = response.body().string();
                            JSONObject items = new JSONObject(responseBody);

                            String result = items.getString("result");
                            JSONObject data = new JSONObject(items.getString("data"));

                            if (result.equals("Success")){
                                Toast.makeText(RegisterActivity.this, data.getString("message"), Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }else{
                                Toast.makeText(RegisterActivity.this, data.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
            });
        }else{
            Toast.makeText(this, "Kindly fill and select all the required fields", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateData(String username, String email, String password) {
        return  !username.isEmpty() &&
                !email.isEmpty() &&
                !password.isEmpty() &&
                (r1.isChecked() || r2.isChecked());
    }

    public void toLoginScreen(View view) {
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
    }
}