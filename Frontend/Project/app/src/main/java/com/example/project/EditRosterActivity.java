package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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

import org.json.JSONException;
import org.json.JSONObject;

public class EditRosterActivity extends AppCompatActivity {
    private RequestQueue mQueue;
    private EditText name;
    private EditText number;

    private EditText position;
/*
    private String[] item = {"PG", "SG", "PF", "SF", "C"};

    AutoCompleteTextView position;

    ArrayAdapter<String> adaptorItems;

    private String itemSelected;*/

    private String teamName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_roster);

        mQueue = Volley.newRequestQueue(this);

        Button add = findViewById(R.id.addPlayer);



        name = findViewById(R.id.etName);
        number = findViewById(R.id.etNumber);
        position = findViewById(R.id.etPosition);

        /*adaptorItems = new ArrayAdapter<String>(this, R.layout.activity_edit_roster, item);

        position.setAdapter(adaptorItems);

        position.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                itemSelected = adapterView.getItemAtPosition(i).toString();
            }
        });
*/

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postUser();
                Intent intent = new Intent(EditRosterActivity.this, TeamRoster.class);
                startActivity(intent);
            }
        });
    }

   /* private void deleteUser() {

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
    }*/

    private void postUser() {
        //change this to address
        String url = "http://coms-309-018.class.las.iastate.edu:8080/players";

        JSONObject postData = new JSONObject();
        try {
            postData.put("playerName", name.getText().toString());
            postData.put("number", number.getText().toString());
            postData.put("position", position.getText().toString());

            //team_id to get what team they on

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
