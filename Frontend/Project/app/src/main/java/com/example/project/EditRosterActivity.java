package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class EditRosterActivity extends AppCompatActivity {
    private RequestQueue mQueue;
    private EditText editTextName;
    private EditText editTextName2;
    private EditText editTextName3;
    private EditText editTextName4;

    private Button add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_roster);

        mQueue = Volley.newRequestQueue(this);

        add = findViewById(R.id.button2);

        editTextName = findViewById(R.id.editTextName);
        editTextName2 = findViewById(R.id.editTextName2);
        editTextName3 = findViewById(R.id.editTextName3);
        editTextName4 = findViewById(R.id.editTextName4);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postUser();
            }
        });
    }

    private void deleteUser() {

        String url = "https://5a183357-b941-4d66-b21b-3b4961c7a63e.mock.pstmn.io/roster/1";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String message = response.getString("message");
                            Log.d("DeleteUser", "Response received: " + response.toString());
                        } catch (JSONException e) {
                            Log.e("DeleteUser", "JSON parsing error: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("DeleteUser", "Error in request: " + error.getMessage());
                    }
                });

        mQueue.add(jsonObjectRequest);
    }

    private void postUser() {
        String url = "https://5a183357-b941-4d66-b21b-3b4961c7a63e.mock.pstmn.io/PostPlayer/";

        JSONObject postData = new JSONObject();
        try {
            postData.put("name", editTextName.getText().toString());
            postData.put("number", editTextName2.getText().toString());
            postData.put("position", editTextName3.getText().toString());
            postData.put("height", editTextName4.getText().toString());


        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle the response, e.g., display a success message

                        Log.d("PostUser", "Response received: " + response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle the error, e.g., display an error message
                Log.e("PostUser", "Error in request: " + error.getMessage());
            }
        });

        mQueue.add(jsonObjectRequest);
    }
}
