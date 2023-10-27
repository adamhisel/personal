package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JoinTeamActivity extends AppCompatActivity {

    private RequestQueue mQueue;
    private String[] teamArr;

    private AutoCompleteTextView teamNameAutoComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_team);

        mQueue = Volley.newRequestQueue(this);

        teamNameAutoComplete = findViewById(R.id.tvTeamName);

        fillTeamList();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, teamArr);
        teamNameAutoComplete.setAdapter(adapter);

        teamNameAutoComplete.setOnItemClickListener((parent, view, position, id) -> {
            String selectedSuggestion = (String) parent.getItemAtPosition(position);
            Toast.makeText(this, "Selected: " + selectedSuggestion, Toast.LENGTH_SHORT).show();
        });

    }

    public void fillTeamList() {
        String url = "http://coms-309-018.class.las.iastate.edu:8080/teams";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject team = response.getJSONObject(i);
                        String name = team.getString("teamName");
                        teamArr[i] = name;

                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });

        mQueue.add(request);
    }





}