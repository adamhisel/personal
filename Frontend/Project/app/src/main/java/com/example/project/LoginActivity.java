package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.project.databinding.ActivityLoginBinding;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * LoginActivity handles user authentication including login and providing the option
 * to navigate to the registration page.
 *
 * @author Jagger Gourley
 */
public class LoginActivity extends AppCompatActivity {

    private static final String BASE_URL = "http://coms-309-018.class.las.iastate.edu:8080/";
    private static final String LOCAL_URL = "http://10.0.2.2:8080/";
    private static RequestQueue mQueue;
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.black));

        mQueue = Volley.newRequestQueue(this);
        setupButtonListeners();
    }

    //Sets up onClick listeners for the buttons.
    private void setupButtonListeners() {

        binding.btnLogin.setOnClickListener(view -> loginUser());
        binding.btnCreateAccount.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
            startActivity(intent);
        });
        binding.btnTest.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }

    //Initiates the login process for the user.
    private void loginUser() {
        String userName = binding.etUserName.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();

        if (!validateUserName() || !validatePassword()) {
            return;
        }

        String url = BASE_URL + "loginUser/" + userName + "/" + password;
        String testUrl = LOCAL_URL + "loginUser/" + userName + "/" + password;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Response", response.toString());
                handleLoginResponse(response, userName, password);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, "User does not exist or other error!", Toast.LENGTH_SHORT).show();
            }
        });

        mQueue.add(jsonObjectRequest);
    }

    //Handles the response and updates user information within Shared Preferences.
    private void handleLoginResponse(JSONObject response, String userName, String password) {
        try {
            String retrievedUserName = response.getString("userName");
            String retrievedPassword = response.getString("password");

            if (userName.equals(retrievedUserName) && password.equals(retrievedPassword)) {
                SharedPrefsUtil.saveUserData(LoginActivity.this, retrievedUserName, response.getString("firstName"), response.getString("lastName"), response.getString("email"), response.getString("phoneNumber"), String.valueOf(response.getInt("id")));
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(LoginActivity.this, "Invalid credentials!", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Returns true if the provided text is null or whitespace only.
    private boolean isEmpty(String text) {
        return text == null || text.trim().isEmpty();
    }

    // Assigns an error message to the input field and returns false.
    private boolean setFieldError(TextInputLayout field, String errorText) {
        field.setError(errorText);
        return false;
    }

    // Clears any error message from the input field.
    private void clearFieldError(TextInputLayout field) {
        field.setError(null);
        field.setErrorEnabled(false);
    }

    // Validates the username input and triggers error state if empty.
    private boolean validateUserName() {
        String userName = binding.etUserName.getText().toString().trim();
        if (isEmpty(userName)) {
            return setFieldError(binding.tilUserName, "Field cannot be empty");
        } else {
            clearFieldError(binding.tilUserName);
            return true;
        }
    }

    // Validates the password input and triggers error state if empty.
    private boolean validatePassword() {
        String password = binding.etPassword.getText().toString().trim();
        if (isEmpty(password)) {
            return setFieldError(binding.tilPassword, "Field cannot be empty");
        } else {
            clearFieldError(binding.tilPassword);
            return true;
        }
    }
}
