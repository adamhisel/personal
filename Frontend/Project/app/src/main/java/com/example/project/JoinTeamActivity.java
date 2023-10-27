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

import java.util.ArrayList;

public class JoinTeamActivity extends AppCompatActivity {

    private RequestQueue mQueue;
    private ArrayList<String> teamArr;

    private AutoCompleteTextView teamNameAutoComplete;
    private AutoCompleteTextView typeAutoComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_team);

        mQueue = Volley.newRequestQueue(this);

        teamNameAutoComplete = findViewById(R.id.tvTeamName);

        typeAutoComplete = findViewById(R.id.tvUserType);

        String[] typeArr = new String[2];
        typeArr[0] = "player";
        typeArr[1] = "fan";


        fillTeamList(new TeamListCallback() {
            @Override
            public void onTeamListReceived(ArrayList<String> teamList) {
                teamArr = teamList; // Populate teamArr when the data is available

                ArrayAdapter<String> adapter1 = new ArrayAdapter<>(JoinTeamActivity.this, android.R.layout.simple_dropdown_item_1line, teamArr);
                teamNameAutoComplete.setAdapter(adapter1);
            }
        });

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(JoinTeamActivity.this, android.R.layout.simple_dropdown_item_1line, typeArr);
        typeAutoComplete.setAdapter(adapter2);


        typeAutoComplete.setOnItemClickListener((parent, view, position, id) -> {
            String selectedSuggestion2 = (String) parent.getItemAtPosition(position);
            Toast.makeText(this, "Selected: " + selectedSuggestion2, Toast.LENGTH_SHORT).show();
        });

    }

    public void fillTeamList(final TeamListCallback callback) {
        String url = "http://10.0.2.2:8080/teams";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            ArrayList<String> temp = new ArrayList<>();

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject team = response.getJSONObject(i);
                                String name = team.getString("teamName");
                                temp.add(name);
                            }

                            callback.onTeamListReceived(temp); // Pass the data to the callback
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

    public interface TeamListCallback {
        void onTeamListReceived(ArrayList<String> teamList);
    }

    /*public ArrayList<String> fillTeamList() {
        String url = "http://10.0.2.2:8080/teams";
        ArrayList<String> temp = new ArrayList<>();
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {

                    for (int i = 0; i < response.length(); i++) {
                        JSONObject team = response.getJSONObject(i);
                        String name = team.getString("teamName");
                        temp.add(name);
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

        return temp;
    }*/






}