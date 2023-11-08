package com.example.project;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.project.databinding.ActivityEditProfileBinding;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Activity for editing the user's profile.
 * This includes updating the username, email, phone number, and password.
 *
 * @author Jagger Gourley
 */
public class EditProfileActivity extends AppCompatActivity {

    private static final String BASE_URL = "http://coms-309-018.class.las.iastate.edu:8080/";
    private static final String LOCAL_URL = "http://10.0.2.2:8080/";
    private static RequestQueue mQueue;
    private ActivityEditProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupButtonListeners();
        mQueue = Volley.newRequestQueue(this);
    }

    // Sets up listeners for save and cancel buttons.
    private void setupButtonListeners() {
        binding.btnSave.setOnClickListener(view -> handleSave());
        binding.btnCancel.setOnClickListener(view -> finish());
    }

    // Handles the save button click by attempting to update the user's profile.
    private void handleSave() {
        String userId = SharedPrefsUtil.getUserId(this);
        // Prepare updated user profile details
        String updatedUserName = binding.etUserName.getText().toString().trim();
        String updatedEmail = binding.etEmail.getText().toString().trim();
        String updatedPhoneNumber = binding.etPhoneNumber.getText().toString().trim();
        String updatedPassword = binding.etPassword.getText().toString().trim();

        // Construct a JSON object with the updated profile information
        JSONObject postData = new JSONObject();
        try {
            postData.put("userName", updatedUserName);
            postData.put("email", updatedEmail);
            postData.put("phoneNumber", updatedPhoneNumber);
            postData.put("password", updatedPassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = BASE_URL + "updateUser/" + userId;
        String testUrl = LOCAL_URL + "updateUser/" + userId;

        // Create and execute a POST request to update the user's profile
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status = response.getString("message");
                            if ("success".equals(status)) {
                                Toast.makeText(EditProfileActivity.this, "Profile Updated!", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(EditProfileActivity.this, "Error updating profile!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(EditProfileActivity.this, "Error parsing response!", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EditProfileActivity.this, "Error updating profile!", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                });

        mQueue.add(jsonObjectRequest);
    }
}
