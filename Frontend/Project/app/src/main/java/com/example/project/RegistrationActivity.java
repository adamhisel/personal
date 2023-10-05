package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

public class RegistrationActivity extends AppCompatActivity {

    private static RequestQueue mQueue;
    TextInputLayout tilUserName;
    TextInputLayout tilEmail;
    TextInputLayout tilPhoneNumber;
    TextInputLayout tilUserType;
    TextInputLayout tilPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.black));


        // Adapter and dropdown for user types
        String[] userTypes = getResources().getStringArray(R.array.user_types);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                userTypes
        );
        AutoCompleteTextView dropdown = findViewById(R.id.tvUserType);
        dropdown.setAdapter(adapter);

        tilUserName = findViewById(R.id.tilUserName);
        tilEmail = findViewById(R.id.tilEmail);
        tilPhoneNumber = findViewById(R.id.tilPhoneNumber);
        tilUserType = findViewById(R.id.tilUserType);
        tilPassword = findViewById(R.id.tilPassword);
        mQueue = Volley.newRequestQueue(this);
        Button btnSignUp = findViewById(R.id.btnSignUp);
        Button btnLogin = findViewById(R.id.btnLogin);
        EditText etUserName = findViewById(R.id.etUserName);
        EditText etEmail = findViewById(R.id.etEmail);
        EditText etPhoneNumber = findViewById(R.id.etPhoneNumber);
        AutoCompleteTextView tvUserType = findViewById(R.id.tvUserType);
        EditText etPassword = findViewById(R.id.etPassword);


        // Remove
        Button btnTest = findViewById(R.id.btnTest);
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = etUserName.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String phoneNumber = etPhoneNumber.getText().toString().trim();
                String userType = tvUserType.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

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

                // URL of server's API endpoint
                String postUrl = "http://10.0.2.2:8080/users";

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, postUrl, postData,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // Handle the response
                                Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle the error
                        error.printStackTrace();
                    }
                });

                // Add the request to the RequestQueue
                mQueue.add(jsonObjectRequest);


            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });


    }

    private Boolean validateUserName() {
        String userName = tilUserName.getEditText().getText().toString().trim();

        if (userName.isEmpty()) {
            tilUserName.setError("Field cannot be empty");
            return false;
        } else {
            tilUserName.setError(null);
            tilUserName.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateEmail() {
        String email = tilEmail.getEditText().getText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (email.isEmpty()) {
            tilEmail.setError("Field cannot be empty");
            return false;
        } else if (!email.matches(emailPattern)) {
            tilEmail.setError("Invalid email address");
            return false;
        } else {
            tilEmail.setError(null);
            tilEmail.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePhoneNumber() {
        String phoneNumber = tilPhoneNumber.getEditText().getText().toString().trim();

        if (phoneNumber.isEmpty()) {
            tilPhoneNumber.setError("Field cannot be empty");
            return false;
        } else {
            tilPhoneNumber.setError(null);
            tilPhoneNumber.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateUserType() {
        String userType = tilUserType.getEditText().getText().toString().trim();

        if (userType.isEmpty()) {
            tilUserType.setError("Field cannot be empty");
            return false;
        } else {
            tilUserName.setError(null);
            tilUserName.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePassword() {
        String password = tilPassword.getEditText().getText().toString().trim();

        if (password.isEmpty()) {
            tilPassword.setError("Field cannot be empty");
            return false;
        } else {
            tilPassword.setError(null);
            tilPassword.setErrorEnabled(false);
            return true;
        }
    }

}
