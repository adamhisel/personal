package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author Adam Hisel
 * Activity that allows a user to make a team for people to join. Will have to option
 * make their team private or public. Private teams will have to have a password to join
 * where public teams wont.
 */
public class AddTeamActivity extends AppCompatActivity {

    private TextInputLayout teamName;

    private TextInputLayout password;

    private RequestQueue mQueue;

    private AutoCompleteTextView teamType;

    private TextInputLayout passwordBox;

    private int teamId;

    private int coachId;

    private boolean isPrivate = false;

    private boolean isSelected = false;

    private String selected = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_team);

        mQueue = Volley.newRequestQueue(this);

        Button finish = (Button)findViewById(R.id.finish);

        Button backButton = findViewById(R.id.exit);

        teamName = findViewById(R.id.teamname);

        password = findViewById(R.id.teamPassword);

        teamType = findViewById(R.id.tvTeamType);

        passwordBox = findViewById(R.id.teamPassword);

        String[] typeArr = new String[2];
        typeArr[0] = "Public";
        typeArr[1] = "Private";

        ArrayAdapter<String> adapter = new ArrayAdapter<>(AddTeamActivity.this, android.R.layout.simple_dropdown_item_1line, typeArr);
        teamType.setAdapter(adapter);

        teamType.setOnItemClickListener((parent, view, position, id) -> {
            selected = (String) parent.getItemAtPosition(position);

            if(selected.isEmpty()){
                isSelected = false;
            }
            else{
                isSelected = true;
            }

            if(selected.equals("Public")){
                isPrivate = false;
                passwordBox.setVisibility(View.INVISIBLE);
            }
            else if (selected.equals("Private")){
                isPrivate = true;
                passwordBox.setVisibility(View.VISIBLE);
            }
        });


        finish.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                boolean isValidTeamName = validateTeamName();
                boolean isValidType = validateType();
                if(isPrivate == true){
                    boolean isValidPassword = validatePassword();
                    if (!isValidTeamName || !isValidPassword || !isValidType) {
                        return;
                    }
                }
                else{
                    if (!isValidTeamName || !isValidType) {
                        return;
                    }
                }

                postTeam(new TeamIdCallback() {
                    @Override
                    public void onTeamIdReceived(int id) {
                        teamId = id;
                        postCoach(new TeamIdCallback() {
                            @Override
                            public void onTeamIdReceived(int id) {
                                coachId = id;
                                joinTeamUser();
                                joinTeamCoach();
                                Intent intent = new Intent(AddTeamActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        });
                    }
                });
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddTeamActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
    private void postTeam(final TeamIdCallback callback) {
        String url = "http://coms-309-018.class.las.iastate.edu:8080/teams";

        JSONObject postData = new JSONObject();

        try {
            postData.put("teamName", teamName.getEditText().getText().toString().trim());
            postData.put("teamIsPrivate", isPrivate);
            if(isPrivate == true) {
                postData.put("password", password.getEditText().getText().toString().trim());
            }
            else{
                postData.put("password", "");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            teamId = response.getInt("id");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        callback.onTeamIdReceived(teamId);
                        Log.d("PostTeam", "Response received: " + response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle the error, e.g., display an error message
                Log.e("PostTeam", "Error in request: " + error.getMessage());
            }
        });

        mQueue.add(jsonObjectRequest);
    }

    private void postCoach(final TeamIdCallback callback) {
        String url = "http://coms-309-018.class.las.iastate.edu:8080/coaches";

        JSONObject postData = new JSONObject();
        try {

            postData.put("name", SharedPrefsUtil.getFirstName(this) + " " + SharedPrefsUtil.getLastName(this));
            postData.put("user_id", SharedPrefsUtil.getUserId(this));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            coachId = response.getInt("id");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        callback.onTeamIdReceived(coachId);
                        Log.d("PostCoach", "Response received: " + response.toString());

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle the error, e.g., display an error message
                Log.e("PostCoach", "Error in request: " + error.getMessage());
            }
        });
        mQueue.add(jsonObjectRequest);
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

    private void joinTeamCoach(){

        String url = "";
        if(isPrivate == true) {
            url = "http://coms-309-018.class.las.iastate.edu:8080/teams/" + teamId + "/coaches/" + coachId + "/" + password.getEditText().getText().toString().trim() + "/" + SharedPrefsUtil.getUserId(this);
        }
        else{
            url = "http://coms-309-018.class.las.iastate.edu:8080/teams/" + teamId + "/coaches/" + coachId + "/dummy" + "/" + SharedPrefsUtil.getUserId(this);
        }
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

    private Boolean validateTeamName() {
        String tilName = teamName.getEditText().getText().toString().trim();

        if (tilName.isEmpty()) {
            teamName.setError("Field cannot be empty");
            return false;
        } else {
            teamName.setError(null);
            teamName.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePassword() {
        String tilName = password.getEditText().getText().toString().trim();

        if (tilName.isEmpty()) {
            password.setError("Field cannot be empty");
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateType(){
        if(isSelected == false || selected == ""){
            password.setError("Field cannot be unselected");
            return false;
        }
        else{
            password.setError(null);
            return true;
        }
    }

    public interface TeamIdCallback {
        void onTeamIdReceived(int id);
    }



}

