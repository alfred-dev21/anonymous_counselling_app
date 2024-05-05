package com.example.anonymouscouncellingapp;

import static com.example.anonymouscouncellingapp.links.Links.PROBLEMS_PHP;
import static com.example.anonymouscouncellingapp.links.Links.SAVE_PROBLEMS_PHP;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.anonymouscouncellingapp.clientui.clientHomeActivity;
import com.example.anonymouscouncellingapp.counselorui.CounselorHomeActivity;
import com.example.anonymouscouncellingapp.details.UserDetails;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ProblemSelectActivity extends AppCompatActivity {
    SearchView searchView;
    ListView listView;
    ArrayList<String> problems, problems_ids;
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
        problems_ids = new ArrayList<>();
        doneBtn = findViewById(R.id.doneBtn);

        Request request = new Request.Builder()
                .url(PROBLEMS_PHP)
                .build();

        listItemsOnListView(request);
        setUpSearchView(searchView);

        doneBtn.setOnClickListener(view -> {
            saveCheckedUserItems(listView, adapter);
        });

    }

    /**
     * This method will make a request to the sever to store user problems on their specific database
     * But the link to that query is yet to be done
     * @param listView
     * @param adapter
     */
    private void saveCheckedUserItems(ListView listView, ArrayAdapter<String> adapter) {
        FormBody.Builder formBuilder = new FormBody.Builder();

        StringBuilder sqlQuery = new StringBuilder("INSERT INTO userProblem (user_id, problem_id) VALUES");
        SparseBooleanArray checked = listView.getCheckedItemPositions();

        for (int i = 0; i < checked.size(); i++){
            if (checked.valueAt(i)){
                String item = adapter.getItem(checked.keyAt(i));
                int itemIndex = problems.indexOf(item);

                if (i == checked.size() - 1){
                    sqlQuery.append(" (")
                            .append(UserDetails.user_id)
                            .append(",")
                            .append(problems_ids.get(itemIndex))
                            .append(")");
                    break;
                }


                sqlQuery.append(" (")
                        .append(UserDetails.user_id)
                        .append(",")
                        .append(problems_ids.get(itemIndex))
                        .append("), ");
            }
        }

        formBuilder.add("user_id", UserDetails.user_id);
        formBuilder.add("query", sqlQuery.toString());
        RequestBody requestBody = formBuilder.build();

        Request request = new Request.Builder()
                .url(SAVE_PROBLEMS_PHP)
                .post(requestBody)
                .build();

        sendSaveRequest(request);
    }

    private void sendSaveRequest(Request request) {
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
                        System.out.println(responseBody);
                        JSONObject items = new JSONObject(responseBody);

                        String result = items.getString("result");
                        JSONObject data = new JSONObject(items.getString("data"));

                        if (result.equals("Success")){
                            Toast.makeText(ProblemSelectActivity.this, data.getString("message"), Toast.LENGTH_SHORT).show();

                            Intent intent;
                            if (UserDetails.username.equals("client")){
                                intent = new Intent(ProblemSelectActivity.this, clientHomeActivity.class);
                            }else{
                                intent = new Intent(ProblemSelectActivity.this, CounselorHomeActivity.class);
                            }
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(ProblemSelectActivity.this, data.getString("message"), Toast.LENGTH_SHORT).show();
                        }


                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        });
    }

    /**
     * This a search view that allows users to search items of the list of problems
     * This method sets it up and ensures that it is usable.
     * @param searchView
     */
    private void setUpSearchView(SearchView searchView) {
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
    }

    /**
     * This method does a request to the server to retrieve the list of possible problems
     * It then displays the items on a listview in the order of retrieval
     * @param request
     */
    private void listItemsOnListView(Request request) {
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
                                problems_ids.add(data.getString("problem_id"));
                            }

                            adapter = new ArrayAdapter<>(
                                    ProblemSelectActivity.this,
                                    android.R.layout.simple_list_item_multiple_choice,
                                    problems
                            );
                            listView.setAdapter(adapter);
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