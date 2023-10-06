package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private static RequestQueue mQueue;
    TextInputLayout tilUserName;
    TextInputLayout tilPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.black));


        mQueue = Volley.newRequestQueue(this);
        tilUserName = findViewById(R.id.tilUserName);
        tilPassword = findViewById(R.id.tilPassword);
        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnCreateAccount = findViewById(R.id.btnCreateAccount);
        EditText etUserName = findViewById(R.id.etUserName);
        EditText etPassword = findViewById(R.id.etPassword);

        // Remove
        Button btnTest = findViewById(R.id.btnTest);
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = etUserName.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                boolean isValidUserName = validateUserName();
                boolean isValidPassword = validatePassword();

                if (!isValidUserName || !isValidPassword) {
                    return;  // Stops further execution if any validation fails
                }


                // URL of server's GET API endpoint to loginUser
                String loginUrl = "http://coms-309-018.class.las.iastate.edu:8080/loginUser/" + userName + "/" + password;

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, loginUrl, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    String retrievedUserName = response.getString("userName");
                                    String retrievedPassword = response.getString("password");

                                    Log.d("LoginActivity", "Retrieved Username from server: " + retrievedUserName); // Added log
                                    Log.d("LoginActivity", "Retrieved Password from server: " + retrievedPassword); // Added log

                                    if (userName.equals(retrievedUserName) && password.equals(retrievedPassword)) {
                                        // Successful login
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
                                        // Wrong credentials
                                        Toast.makeText(LoginActivity.this, "Invalid credentials!", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // Handle the error. This could also mean the user doesn't exist.
                                Toast.makeText(LoginActivity.this, "User does not exist or other error!", Toast.LENGTH_SHORT).show();
                            }
                        });

                // Add the request to the RequestQueue
                mQueue.add(jsonObjectRequest);
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
