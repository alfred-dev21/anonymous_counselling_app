package com.example.anonymouscouncellingapp;

import static com.example.anonymouscouncellingapp.links.Links.LOGIN_PHP;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class LoginActivity extends AppCompatActivity {
    private OkHttpClient client;
    private EditText username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.client_username);
        password = findViewById(R.id.client_password);
        client = new OkHttpClient();


    }

    public void toRegistration(View view) {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        finish();
    }

    public void loginUser(View view) {
        String username = this.username.getText().toString();
        String password = this.password.getText().toString();

        if (!username.isEmpty() && !password.isEmpty()){
            RequestBody requestBody = new FormBody.Builder()
                    .add("username", username)
                    .add("password", password)
                    .build();

            Request request = new Request.Builder()
                    .url(LOGIN_PHP)
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
                                Toast.makeText(LoginActivity.this, data.getString("message"), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);

                                startActivity(intent);
                                finish();
                            }else{
                                Toast.makeText(LoginActivity.this, data.getString("message"), Toast.LENGTH_SHORT).show();
                            }


                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
            });
        }
    }
}