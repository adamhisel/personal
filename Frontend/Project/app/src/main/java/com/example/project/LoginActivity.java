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

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private static RequestQueue mQueue;
    private ActivityLoginBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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

            String loginUrl = "http://coms-309-018.class.las.iastate.edu:8080/loginUser/" + userName + "/" + password;
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, loginUrl, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
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

    private Boolean validateUserName() {
        String userName = binding.etUserName.getText().toString().trim();

        if (userName.isEmpty()) {
            binding.tilUserName.setError("Username cannot be empty");
            return false;
        } else {
            binding.tilUserName.setError(null);
            binding.tilUserName.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePassword() {
        String password = binding.etPassword.getText().toString().trim();

        if (password.isEmpty()) {
            binding.tilPassword.setError("Password cannot be empty");
            return false;
        } else {
            binding.tilPassword.setError(null);
            binding.tilPassword.setErrorEnabled(false);
            return true;
        }
    }

}
