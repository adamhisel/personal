package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

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

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = etUserName.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                boolean isValidUserName = validateUserName();
                boolean isValidPassword = validatePassword();

                if (!isValidUserName || !isValidPassword) {
                    return;  // Stops further execution if any validation fails.
                }
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
