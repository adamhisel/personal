package com.example.volley_json_request;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private TextView mTextViewResult;
    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextViewResult = findViewById(R.id.textViewResult);
        Button buttonParseArray = findViewById(R.id.buttonParseArray);
        Button buttonParseObject = findViewById(R.id.buttonParseObject);
        Button buttonClear = findViewById(R.id.buttonClear);

        mQueue = Volley.newRequestQueue(this);

        buttonParseArray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jsonParseArray();
            }
        });

        buttonParseObject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jsonParseObject();
            }
        });

        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTextViewResult.setText("");
            }
        });
    }

    private void jsonParseArray() {
        String url = "https://jsonplaceholder.typicode.com/users";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject user = response.getJSONObject(i);
                                String id = user.getString("id");
                                String name = user.getString("name");
                                String username = user.getString("username");

                                mTextViewResult.append(id + ", " + name + ", " + username + "\n\n");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mQueue.add(request);
    }

    private void jsonParseObject() {
        String url = "http://ip.jsontest.com/";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("JSON_RESPONSE", response.toString());
                        try {
                            String ip = response.getString("ip");

                            mTextViewResult.append(ip + "\n\n");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mQueue.add(request);
    }


}