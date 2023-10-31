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
import com.example.project.databinding.ActivityLoginBinding;
import com.example.project.databinding.ActivityPlayerRegistrationBinding;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * PlayerRegistrationActivity is responsible for handling the user interface and logic
 * for registering or updating a player's information in the application.
 */
public class PlayerRegistrationActivity extends AppCompatActivity {

    /**
     * Base URL for the remote server connection
     */
    private static final String BASE_URL = "http://coms-309-018.class.las.iastate.edu:8080/";

    /**
     * Local URL for the local server connection
     */
    private static final String LOCAL_URL = "http://10.0.2.2:8080/";

    /**
     * View binding for this activity
     */
    private ActivityPlayerRegistrationBinding binding;

    /**
     * RequestQueue for handling network requests.
     */
    private static RequestQueue mQueue;

    /**
     * Called when the activity is first created.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     *                           Note: Otherwise it is null.
     */
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

    /**
     * Sets up the dropdown menu for selecting player positions.
     */
    private void setupDropdownPlayerPositions() {
        String[] playerPositions = getResources().getStringArray(R.array.player_positions);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, playerPositions);
        binding.tvPosition.setAdapter(adapter);
    }

    /**
     * Sets up the button listeners for the activity.
     */
    private void setupButtonListeners() {
        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlePlayerInfoUpdate();
            }
        });
    }

    /**
     * Handles the updating of player information by sending a PUT request to the server.
     */
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

    /**
     * Handles the server's response after attempting to update the player's information.
     * @param response The JSON response from the server.
     */
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