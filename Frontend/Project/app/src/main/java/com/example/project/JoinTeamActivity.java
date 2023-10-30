package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JoinTeamActivity extends AppCompatActivity {

    private RequestQueue mQueue;
    private ArrayList<String> teamArr;

    private ArrayList<String> idArr;

    private int teamId;
    private AutoCompleteTextView teamNameAutoComplete;
    private AutoCompleteTextView typeAutoComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_team);

        mQueue = Volley.newRequestQueue(this);

        Button joinButton = findViewById(R.id.btnJoin);

        teamNameAutoComplete = findViewById(R.id.tvTeamName);

        typeAutoComplete = findViewById(R.id.tvUserType);

        String[] typeArr = new String[2];
        typeArr[0] = "Player";
        typeArr[1] = "Fan";

        fillTeamList(new TeamListCallback() {

            @Override
            public void onTeamListReceived(ArrayList<String> teamList) {
                teamArr = teamList;

                ArrayAdapter<String> adapter1 = new ArrayAdapter<>(JoinTeamActivity.this, android.R.layout.simple_dropdown_item_1line, teamArr);
                teamNameAutoComplete.setAdapter(adapter1);

            }
        });

        fillIdList(new TeamListCallback() {
            @Override
            public void onTeamListReceived(ArrayList<String> teamList) {
                idArr = teamList;
            }
        });

        teamNameAutoComplete.setOnItemClickListener((parent, view, position, id) -> {
            String selected = (String) parent.getItemAtPosition(position);
            teamId = Integer.parseInt(idArr.get(position));
        });


        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(JoinTeamActivity.this, android.R.layout.simple_dropdown_item_1line, typeArr);
        typeAutoComplete.setAdapter(adapter2);


        typeAutoComplete.setOnItemClickListener((parent, view, position, id) -> {
            String selected = (String) parent.getItemAtPosition(position);

        });

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(JoinTeamActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    private void fillTeamList(final TeamListCallback callback) {
        String url = "http://10.0.2.2:8080/teams";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            ArrayList<String> temp1 = new ArrayList<>();

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject team = response.getJSONObject(i);
                                String name = team.getString("teamName");
                                //teamId = team.getInt("id");
                                temp1.add(name);

                            }

                            callback.onTeamListReceived(temp1);
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

    private void fillIdList(final TeamListCallback callback) {
        String url = "http://10.0.2.2:8080/teams";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            ArrayList<String> temp1 = new ArrayList<>();

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject team = response.getJSONObject(i);
                                String id = team.getString("id");
                                temp1.add(id);
                            }

                            callback.onTeamListReceived(temp1);
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


    private void joinTeamUser(){
        String userId = SharedPrefsUtil.getUserId(this);

        String url = "http://coms-309-018.class.las.iastate.edu:8080/User/" + userId + "/teams/" + teamId;

        StringRequest putRequest = new StringRequest(Request.Method.PUT, url,
                response -> {
                    if ("success".equals(response)) {

                    } else if ("failure".equals(response)) {
                    }
                },
                error -> {

                    if (error.networkResponse != null) {
                        String errorMessage = new String(error.networkResponse.data);
                        Log.e("JoinTeam", "Error in request: " + errorMessage);
                    }
                }
        );

        mQueue.add(putRequest);


    }

    private void joinTeamPlayer(){
        String userId = SharedPrefsUtil.getUserId(this);

        String url = "http://coms-309-018.class.las.iastate.edu:8080/User/" + userId + "/teams/" + teamId;

        StringRequest putRequest = new StringRequest(Request.Method.PUT, url,
                response -> {
                    if ("success".equals(response)) {

                    } else if ("failure".equals(response)) {
                    }
                },
                error -> {

                    if (error.networkResponse != null) {
                        String errorMessage = new String(error.networkResponse.data);
                        Log.e("JoinTeam", "Error in request: " + errorMessage);
                    }
                }
        );

        mQueue.add(putRequest);


    }

}