package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EditRosterActivity extends AppCompatActivity {
    private RequestQueue mQueue;
    private TextInputLayout name;
    private TextInputLayout number;

    private TextInputLayout position;

    private int teamId;

    private int playerId;
/*
    private String[] item = {"PG", "SG", "PF", "SF", "C"};

    AutoCompleteTextView position;

    ArrayAdapter<String> adaptorItems;

    private String itemSelected;*/

    private String teamName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_roster);

        mQueue = Volley.newRequestQueue(this);

        Button add = findViewById(R.id.addPlayer);
        Button back = findViewById(R.id.backButton);
        TextView success = findViewById(R.id.fillText);


        name = findViewById(R.id.name);
        number = findViewById(R.id.number);
        position = findViewById(R.id.position);


        Intent intent = getIntent();
        if (intent != null) {
           teamName = intent.getStringExtra("key_string");
           String tempId =  intent.getStringExtra("id");
           teamId = Integer.parseInt(tempId);
        }

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean isValidName = validateName();
                boolean isValidNumber = validateNumber();
                boolean isValidPosition = validatePosition();

                if (!isValidName || !isValidNumber || !isValidPosition) {
                    return;
                }

                postUser();
                findPlayer();
                linkUserToTeam();

                success.setText("Successfully added " + name.getEditText().getText());
                name.getEditText().getText().clear();
                number.getEditText().getText().clear();
                position.getEditText().getText().clear();


            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditRosterActivity.this, TeamRosterCoach.class);
                startActivity(intent);
            }
        });
    }

    /*private void postTeam() {
        String url = "http://coms-309-018.class.las.iastate.edu:8080/teams";

        JSONObject postData = new JSONObject();
        try {
            postData.put("teamName", teamName);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject team = response.getJSONObject(String.valueOf(response.length()));

                            teamId = team.getInt("id");

                            Log.d("PostTeam", "Response received: " + response.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("PostTeam", "Error in request: " + error.getMessage());
            }
        });

        mQueue.add(jsonObjectRequest);
    }*/

    public void linkUserToTeam() {

        String url = "http://coms-309-018.class.las.iastate.edu:8080/teams/" + teamId + "/players/" + playerId;

        StringRequest putRequest = new StringRequest(Request.Method.PUT, url,
                response -> {
                    if ("success".equals(response)) {

                    } else if ("failure".equals(response)) {
                        // Team or player not found, or other failure
                    }
                },
                error -> {

                    if (error.networkResponse != null) {
                        String errorMessage = new String(error.networkResponse.data);
                        Log.e("LinkTeam", "Error in request: " + errorMessage);
                    }
                }
        );


        mQueue.add(putRequest);
    }

   /* private void deleteUser() {

        String url = "https://5a183357-b941-4d66-b21b-3b4961c7a63e.mock.pstmn.io/roster/1";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String message = response.getString("message");
                            Log.d("DeleteUser", "Response received: " + response.toString());
                        } catch (JSONException e) {
                            Log.e("DeleteUser", "JSON parsing error: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("DeleteUser", "Error in request: " + error.getMessage());
                    }
                });

        mQueue.add(jsonObjectRequest);
    }*/



    private void postUser() {
        //change this to address
        String url = "http://coms-309-018.class.las.iastate.edu:8080/players";

        JSONObject postData = new JSONObject();
        try {
            postData.put("playerName", name.getEditText().getText().toString());
            postData.put("number", number.getEditText().getText().toString());
            postData.put("position", position.getEditText().getText().toString());


        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                            Log.d("PostUser", "Response received: " + response.toString());

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle the error, e.g., display an error message
                Log.e("PostUser", "Error in request: " + error.getMessage());
            }
        });

        mQueue.add(jsonObjectRequest);
    }

    public void findPlayer() {
        String url = "http://coms-309-018.class.las.iastate.edu:8080/players";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject player = response.getJSONObject(i);
                        String playerName = player.getString("playerName");
                        if (playerName.equals(name.getEditText().getText().toString())) {
                            playerId = player.getInt("id");
                        }
                        else{
                            playerId = response.length()+1;
                        }

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

    private Boolean validateName() {
        String tilName = name.getEditText().getText().toString().trim();

        if (tilName.isEmpty()) {
            name.setError("Field cannot be empty");
            return false;
        } else {
            name.setError(null);
            name.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateNumber() {
        String tilNumber = number.getEditText().getText().toString().trim();


        if (tilNumber.isEmpty()) {
            number.setError("Field cannot be empty");
            return false;
        }
        if (!isInteger(tilNumber)){
            number.setError("Field has to be a number");
            return false;
        }
        else{
            number.setError(null);
            number.setErrorEnabled(false);
            return true;
        }

    }

    private Boolean validatePosition() {
        String tilPosition = position.getEditText().getText().toString().trim();


        if (tilPosition.isEmpty()) {
            position.setError("Field cannot be empty");
            return false;
        }
        if (!tilPosition.equals("PG") && !tilPosition.equals("SG") && !tilPosition.equals("PF") && !tilPosition.equals("SF") && !tilPosition.equals("C")){
            position.setError("Field has to be either a PG, SG, PF, SF, C");
            return false;
        }
        else{
            position.setError(null);
            position.setErrorEnabled(false);
            return true;
        }

    }

    public static boolean isInteger (String str){
        try {
            // Attempt to parse the string as an integer
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            // If an exception is thrown, the string is not an integer
            return false;
        }
    }
}