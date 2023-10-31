package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.project.databinding.ActivityRegistrationBinding;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * The RegistrationActivity class represents the activity in the application where
 * users can sign up for an account.
 */
public class RegistrationActivity extends AppCompatActivity {

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
    private ActivityRegistrationBinding binding;

    /**
     * RequestQueue for handling network requests.
     */
    private static RequestQueue mQueue;

    /**
     * Initializes the activity, sets up the user interface, and handles user interactions.
     * @param savedInstanceState If the activity is being re-initialized after previously being
     *                           shut down then this Bundle contains the data it most recently
     *                           supplied in onSaveInstanceState(Bundle). Note: Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.black));

        setupDropdownUserTypes();
        mQueue = Volley.newRequestQueue(this);
        setupButtonListeners();
    }


    /**
     * Sets up the dropdown menu for selecting user types.
     */
    private void setupDropdownUserTypes() {
        String[] userTypes = getResources().getStringArray(R.array.user_types);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, userTypes);
        binding.tvUserType.setAdapter(adapter);
    }

    /**
     * Sets up the click listeners for the buttons in the activity.
     */
    private void setupButtonListeners() {
        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { handleSignUp(); }
        });

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        binding.btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Handles the user's sign up request.
     */
    private void handleSignUp() {
        String userName = binding.etUserName.getText().toString().trim();
        String email = binding.etEmail.getText().toString().trim();
        String phoneNumber = binding.etPhoneNumber.getText().toString().trim();
        String userType = binding.tvUserType.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();

        boolean isValidUserName = validateUserName();
        boolean isValidEmail = validateEmail();
        boolean isValidPhoneNumber = validatePhoneNumber();
        boolean isValidUserType = validateUserType();
        boolean isValidPassword = validatePassword();

        if (!isValidUserName || !isValidEmail || !isValidPhoneNumber || !isValidUserType || !isValidPassword) {
            return;  // Stops further execution if any validation fails.
        }

        JSONObject postData = new JSONObject();
        try {
            postData.put("userName", userName);
            postData.put("email", email);
            postData.put("phoneNumber", phoneNumber);
            postData.put("userType", userType);
            postData.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = BASE_URL + "users";
        String testUrl = LOCAL_URL + "users";

        Log.d("PostData", postData.toString());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, testUrl, postData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());
                        handleSignUpResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        );

        mQueue.add(jsonObjectRequest);
    }

    /**
     * Handles the server response for the sign up request.
     * @param response The JSON response received from the server.
     */
    private void handleSignUpResponse(JSONObject response) {
        try {
            // Check if the response has a userName, indicating successful user creation
            if (response.has("userName")) {
                SharedPrefsUtil.saveUserData(
                        RegistrationActivity.this,
                        response.getString("userName"),
                        response.getString("email"),
                        response.getString("phoneNumber"),
                        response.getString("userType"),
                        response.getString("userID")  // Temporary placeholder for userID
                );
                String userType = response.getString("userType");
                Intent intent;
                if ("player".equals(userType)) {
                    intent = new Intent(RegistrationActivity.this, PlayerRegistrationActivity.class);
                } else {
                    intent = new Intent(RegistrationActivity.this, MainActivity.class);
                }
                startActivity(intent);
            } else {
                Toast.makeText(RegistrationActivity.this, "Registration failed!", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(RegistrationActivity.this, "Unexpected response from server", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Checks if a text field is empty.
     * @param text The text to be checked.
     * @return true if the text is empty, false otherwise.
     */
    private boolean isEmpty(String text) {
        return text == null || text.trim().isEmpty();
    }

    /**
     * Sets an error message for a text input layout.
     * @param field     The text input layout to set the error on.
     * @param errorText The error message.
     * @return Always returns false.
     */
    private boolean setFieldError(TextInputLayout field, String errorText) {
        field.setError(errorText);
        return false;
    }

    /**
     * Clears the error message on a text input layout.
     * @param field The text input layout to clear the error on.
     */
    private void clearFieldError(TextInputLayout field) {
        field.setError(null);
        field.setErrorEnabled(false);
    }

    /**
     * Validates the user name input field.
     * @return true if the input is valid, false otherwise.
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
     * Validates the email input field.
     * @return true if the input is valid, false otherwise.
     */
    private boolean validateEmail() {
        String email = binding.etEmail.getText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (isEmpty(email)) {
            return setFieldError(binding.tilEmail, "Field cannot be empty");
        } else if (!email.matches(emailPattern)) {
            return setFieldError(binding.tilEmail, "Invalid email address");
        } else {
            clearFieldError(binding.tilEmail);
            return true;
        }
    }

    /**
     * Validates the phone number input field.
     * @return true if the input is valid, false otherwise.
     */
    private boolean validatePhoneNumber() {
        String phoneNumber = binding.etPhoneNumber.getText().toString().trim();
        if (isEmpty(phoneNumber)) {
            return setFieldError(binding.tilPhoneNumber, "Field cannot be empty");
        } else {
            clearFieldError(binding.tilPhoneNumber);
            return true;
        }
    }

    /**
     * Validates the user type input field.
     * @return true if the input is valid, false otherwise.
     */
    private boolean validateUserType() {
        String userType = binding.tvUserType.getText().toString().trim();
        if (isEmpty(userType)) {
            return setFieldError(binding.tilUserType, "Field cannot be empty");
        } else {
            clearFieldError(binding.tilUserType);
            return true;
        }
    }

    /**
     * Validates the password input field.
     * @return true if the input is valid, false otherwise.
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
