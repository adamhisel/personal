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
import com.example.project.databinding.ActivityRegistrationBinding;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * The RegistrationActivity class represents the activity in the application where
 * users can sign up for an account.
 */
public class RegistrationActivity extends AppCompatActivity {

    private static final String BASE_URL = "http://coms-309-018.class.las.iastate.edu:8080/";
    private static final String LOCAL_URL = "http://10.0.2.2:8080/";
    private ActivityRegistrationBinding binding;
    private static RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.black));

        mQueue = Volley.newRequestQueue(this);
        setupButtonListeners();
    }

    private void setupButtonListeners() {
        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSignUp();
            }
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

    private void handleSignUp() {
        String userName = binding.etUserName.getText().toString().trim();
        String firstName = binding.etFirstName.getText().toString().trim();
        String lastName = binding.etLastName.getText().toString().trim();
        String email = binding.etEmail.getText().toString().trim();
        String phoneNumber = binding.etPhoneNumber.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();

        boolean isValidUserName = validateUserName();
        boolean isValidFirstName = validateFirstName();
        boolean isValidLastName = validateLastName();
        boolean isValidEmail = validateEmail();
        boolean isValidPhoneNumber = validatePhoneNumber();
        boolean isValidPassword = validatePassword();

        if (!isValidUserName || !isValidFirstName || !isValidLastName || !isValidEmail || !isValidPhoneNumber || !isValidPassword) {
            return;  // Stops further execution if any validation fails.
        }

        JSONObject postData = new JSONObject();
        try {
            postData.put("userName", userName);
            postData.put("firstName", firstName);
            postData.put("lastName", lastName);
            postData.put("email", email);
            postData.put("phoneNumber", phoneNumber);
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

    private void handleSignUpResponse(JSONObject response) {
        try {
            int id = response.getInt("id");
            String userName = response.getString("userName");
            String firstName = response.getString("firstName");
            String lastName = response.getString("lastName");
            String email = response.getString("email");
            String phoneNumber = response.getString("phoneNumber");

            // Save user information using SharedPrefsUtil
            SharedPrefsUtil.saveUserData(this, userName, firstName, lastName, email, phoneNumber, String.valueOf(id));

            Toast.makeText(RegistrationActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();

            // Redirect to MainActivity
            Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(RegistrationActivity.this, "Unexpected response from server", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isEmpty(String text) {
        return text == null || text.trim().isEmpty();
    }

    private boolean setFieldError(TextInputLayout field, String errorText) {
        field.setError(errorText);
        return false;
    }

    private void clearFieldError(TextInputLayout field) {
        field.setError(null);
        field.setErrorEnabled(false);
    }

    private boolean validateUserName() {
        String userName = binding.etUserName.getText().toString().trim();
        if (isEmpty(userName)) {
            return setFieldError(binding.tilUserName, "Field cannot be empty");
        } else {
            clearFieldError(binding.tilUserName);
            return true;
        }
    }

    private boolean validateFirstName() {
        String firstName = binding.etFirstName.getText().toString().trim();
        if (isEmpty(firstName)) {
            return setFieldError(binding.tilFirstName, "Field cannot be empty");
        } else {
            clearFieldError(binding.tilFirstName);
            return true;
        }
    }

    private boolean validateLastName() {
        String lastName = binding.etLastName.getText().toString().trim();
        if (isEmpty(lastName)) {
            return setFieldError(binding.tilLastName, "Field cannot be empty");
        } else {
            clearFieldError(binding.tilLastName);
            return true;
        }
    }

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

    private boolean validatePhoneNumber() {
        String phoneNumber = binding.etPhoneNumber.getText().toString().trim();
        if (isEmpty(phoneNumber)) {
            return setFieldError(binding.tilPhoneNumber, "Field cannot be empty");
        } else {
            clearFieldError(binding.tilPhoneNumber);
            return true;
        }
    }

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
