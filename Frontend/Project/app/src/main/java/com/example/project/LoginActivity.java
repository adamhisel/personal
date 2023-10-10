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

public class LoginActivity extends AppCompatActivity {

    private static final String BASE_URL = "http://coms-309-018.class.las.iastate.edu:8080/";
    private static final String LOCAL_URL = "http://10.0.2.2:8080/";
    private ActivityLoginBinding binding;
    private static RequestQueue mQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.black));

        mQueue = Volley.newRequestQueue(this);
        setupButtonListeners();
    }
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
                            response.getString("userType"),
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
