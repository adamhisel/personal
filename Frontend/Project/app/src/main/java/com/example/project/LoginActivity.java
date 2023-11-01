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
 */
public class LoginActivity extends AppCompatActivity {

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
    private ActivityLoginBinding binding;

    /**
     * RequestQueue for handling network requests.
     */
    private static RequestQueue mQueue;


    /**
     * Called when the activity is starting. This is where most initialization should go.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down
     *                           then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     *                           Note: Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.black));

        mQueue = Volley.newRequestQueue(this);
        setupButtonListeners();
    }

    /**
     * Initializes button click listeners.
     */
    private void setupButtonListeners() {
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        binding.btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });

        binding.btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Handles user login.
     */
    private void loginUser() {
        String userName = binding.etUserName.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();

        if (!validateUserName() || !validatePassword()) {
            return;
        }

        String loginUrl = BASE_URL + "loginUser/" + userName + "/" + password;
        String testUrl = LOCAL_URL + "loginUser/" + userName + "/" + password;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, testUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());
                        handleLoginResponse(response, userName, password);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this, "User does not exist or other error!", Toast.LENGTH_SHORT).show();
                    }
                });

        mQueue.add(jsonObjectRequest);
    }

    /**
     * Handles the server's response to a login request.
     * @param response The JSONObject response from the server.
     * @param userName The user's username.
     * @param password The user's password.
     */
    private void handleLoginResponse(JSONObject response, String userName, String password) {
        try {
            String retrievedUserName = response.getString("userName");
            String retrievedPassword = response.getString("password");

            if (userName.equals(retrievedUserName) && password.equals(retrievedPassword)) {
                SharedPrefsUtil.saveUserData(
                        LoginActivity.this,
                        retrievedUserName,
                        response.getString("email"),
                        response.getString("phoneNumber"),
                        null,
                        response.getString("userID")
                );
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(LoginActivity.this, "Invalid credentials!", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if the given text is empty.
     * @param text The text to check.
     * @return True if the text is empty or null, otherwise false.
     */
    private boolean isEmpty(String text) {
        return text == null || text.trim().isEmpty();
    }

    /**
     * Sets an error message on a TextInputLayout and returns false.
     * @param field The TextInputLayout to set the error message on.
     * @param errorText The error message.
     * @return Always returns false.
     */
    private boolean setFieldError(TextInputLayout field, String errorText) {
        field.setError(errorText);
        return false;
    }

    /**
     * Clears the error message from a TextInputLayout.
     * @param field The TextInputLayout to clear the error message from.
     */
    private void clearFieldError(TextInputLayout field) {
        field.setError(null);
        field.setErrorEnabled(false);
    }

    /**
     * Validates the user name input field.
     * @return True if the field is valid, otherwise false.
     */
    private boolean validateUserName() {
        String userName = binding.etUserName.getText().toString().trim();
        if (isEmpty(userName)) {
            return setFieldError(binding.tilUserName, "Field cannot be empty");
        } else {
            clearFieldError(binding.tilUserName);
            return true;
        }
    }

    /**
     * Validates the password input field.
     * @return True if the field is valid, otherwise false.
     */
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
