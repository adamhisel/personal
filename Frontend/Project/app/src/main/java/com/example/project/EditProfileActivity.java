package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.project.databinding.ActivityEditProfileBinding;

import org.json.JSONException;
import org.json.JSONObject;

public class EditProfileActivity extends AppCompatActivity {

    private static final String BASE_URL = "http://coms-309-018.class.las.iastate.edu:8080/";
    private ActivityEditProfileBinding binding;
    private static RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupButtonListeners();
    }

    private void setupButtonListeners() {
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleSave();
            }
        });

        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void handleSave() {
        // Get the updated values
        String updatedUserName = binding.etUserName.getText().toString().trim();
        String updatedEmail = binding.etEmail.getText().toString().trim();
        String updatedPhoneNumber = binding.etPhoneNumber.getText().toString().trim();
        String updatedPassword = binding.etPassword.getText().toString().trim();

        // Create a JSONObject with the updated values
        JSONObject postData = new JSONObject();
        try {
            postData.put("userName", updatedUserName);
            postData.put("email", updatedEmail);
            postData.put("phoneNumber", updatedPhoneNumber);
            postData.put("password", updatedPassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Create the PUT request
        String url = BASE_URL + "";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, postData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle successful response
                        Toast.makeText(EditProfileActivity.this, "Profile Updated!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error response
                        Toast.makeText(EditProfileActivity.this, "Error updating profile!", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                });

        mQueue.add(jsonObjectRequest);
    }
}
