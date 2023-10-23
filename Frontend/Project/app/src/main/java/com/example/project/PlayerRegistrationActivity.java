package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.project.databinding.ActivityPlayerRegistrationBinding;

import org.json.JSONException;
import org.json.JSONObject;

public class PlayerRegistrationActivity extends AppCompatActivity {

    private static final String BASE_URL = "http://coms-309-018.class.las.iastate.edu:8080/";
    private static final String LOCAL_URL = "http://10.0.2.2:8080/";
    private ActivityPlayerRegistrationBinding binding;
    private static RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlayerRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.black));

        setupDropdownPlayerPositions();
        mQueue = Volley.newRequestQueue(this);
        setupButtonListeners();

    }

    private void setupDropdownPlayerPositions() {
        String[] playerPositions = getResources().getStringArray(R.array.player_positions);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, playerPositions);
        binding.tvPosition.setAdapter(adapter);
    }

    private void setupButtonListeners() {
        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlePlayerInfoUpdate();
            }
        });
    }

    private void handlePlayerInfoUpdate() {
        String name = binding.etName.getText().toString().trim();
        String number = binding.etNumber.getText().toString().trim();
        String position = binding.tvPosition.getText().toString().trim();
        String userType = SharedPrefsUtil.getUserType(this);

        String userId = SharedPrefsUtil.getUserId(this);

        JSONObject postData = new JSONObject();
        try {
            postData.put("playerName", name);
            postData.put("number", number);
            postData.put("position", position);
            postData.put("userType", userType);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = BASE_URL + "players/" + userId;
        String testUrl = LOCAL_URL + "players/" + userId;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, testUrl, postData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        handleUpdateResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(PlayerRegistrationActivity.this, "Update failed!", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        mQueue.add(jsonObjectRequest);
    }

    private void handleUpdateResponse(JSONObject response) {
        try {
            // Check if the response has a userName, indicating successful user update
            if (response.has("userName")) {
                // Assuming you'd want to update the SharedPrefs with the new user data as well
                SharedPrefsUtil.saveUserData(
                        PlayerRegistrationActivity.this,
                        response.getString("userName"),
                        response.getString("email"),
                        response.getString("phoneNumber"),
                        response.getString("userType"),
                        response.getString("userID")  // If your server response includes a userID
                );

                Intent intent = new Intent(PlayerRegistrationActivity.this, MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(PlayerRegistrationActivity.this, "Update failed!", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(PlayerRegistrationActivity.this, "Unexpected response from server", Toast.LENGTH_SHORT).show();
        }

    }
}