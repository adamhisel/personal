package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

public class AddTeamActivity extends AppCompatActivity {

    private EditText teamName;

    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_team);

        mQueue = Volley.newRequestQueue(this);

        Button finish = (Button)findViewById(R.id.finish);
        Button back = findViewById(R.id.backButton);

        teamName = findViewById(R.id.etTeamname);

        finish.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                postTeam();
                Intent intent = new Intent(AddTeamActivity.this, MainActivity.class);
                startActivity(intent);
                //replaceFrag(new HomeFragment());
            }

        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddTeamActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    private void postTeam() {
        String url = "http://coms-309-018.class.las.iastate.edu:8080/teams";

        JSONObject postData = new JSONObject();
        teamName.getText().toString();
        try {
            postData.put("teamName", teamName.getText().toString());


        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("PostTeam", "Response received: " + response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle the error, e.g., display an error message
                Log.e("PostTeam", "Error in request: " + error.getMessage());
            }
        });

        mQueue.add(jsonObjectRequest);
    }

    private void replaceFrag(Fragment frag){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.addTeamLayout, frag);
        fragmentTransaction.commit();
    }

    }

