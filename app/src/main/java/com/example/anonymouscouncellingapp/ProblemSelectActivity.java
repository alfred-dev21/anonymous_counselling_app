package com.example.anonymouscouncellingapp;

import static com.example.anonymouscouncellingapp.links.Links.PROBLEMS_PHP;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ProblemSelectActivity extends AppCompatActivity {
    SearchView searchView;
    ListView listView;
    ArrayList<String> problems;
    ArrayAdapter<String> adapter;
    OkHttpClient client;
    Button doneBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_problem);

        client = new OkHttpClient();
        searchView = findViewById(R.id.searchBar);
        listView = findViewById(R.id.list_item);
        problems = new ArrayList<>();

        Request request = new Request.Builder()
                .url(PROBLEMS_PHP)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                runOnUiThread(() -> {
                    assert response.body() != null;
                    try {
                        String responseBody = response.body().string();
                        JSONObject items = new JSONObject(responseBody);

                        String result = items.getString("result");
                        JSONArray dataArray = new JSONArray(items.getString("data"));

                        if (result.equals("OK")){
                            for (int i = 0; i < dataArray.length(); i++){
                                JSONObject data = dataArray.getJSONObject(i);
                                problems.add(data.getString("problem_description"));
                            }

                            adapter = new ArrayAdapter<>(
                                    ProblemSelectActivity.this,
                                    android.R.layout.simple_list_item_multiple_choice,
                                    problems
                            );

                            listView.setAdapter(adapter);

                            //getCheckedItems

                            doneBtn = findViewById(R.id.doneBtn);
                            doneBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    // TODO: write some code here
                                }
                            });

                            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                @Override
                                public boolean onQueryTextSubmit(String query) {
                                    ProblemSelectActivity.this.adapter.getFilter().filter(query);
                                    return false;
                                }

                                @Override
                                public boolean onQueryTextChange(String newText) {
                                    ProblemSelectActivity.this.adapter.getFilter().filter(newText);
                                    return false;
                                }
                            });
                        }else{
                            Toast.makeText(ProblemSelectActivity.this, items.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        });
    }
}