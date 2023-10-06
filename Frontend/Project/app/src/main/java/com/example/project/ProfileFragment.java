package com.example.project;

import android.content.Intent;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


public class ProfileFragment extends Fragment {

    private static RequestQueue mQueue;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        mQueue = Volley.newRequestQueue(getActivity());
        TextView tvUserName = rootView.findViewById(R.id.tvUserName);
        TextView tvEmail = rootView.findViewById(R.id.tvEmail);
        TextView tvPhoneNumber = rootView.findViewById(R.id.tvPhoneNumber);
        Button btnLogout = rootView.findViewById(R.id.btnLogout);


        String userId = SharedPrefsUtil.getUserId(getActivity()); // Get userId from SharedPreferences
        getProfile(userId, tvUserName, tvEmail, tvPhoneNumber);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle logout logic here.
                handleLogout();
            }
        });

        return rootView;
    }

    private void getProfile(String userId, TextView tvUserName, TextView tvEmail, TextView tvPhoneNumber) {
        String getUrl = "http://coms-309-018.class.las.iastate.edu:8080/users/" + userId; // Fetch user info based on userId

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, getUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String username = response.getString("userName");
                            String email = response.getString("email");
                            String phoneNumber = response.getString("phoneNumber");

                            tvUserName.setText(username); // Set the retrieved userName to TextView
                            tvEmail.setText(email);
                            tvPhoneNumber.setText(phoneNumber);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        mQueue.add(jsonObjectRequest);
    }

    private void handleLogout() {
        SharedPrefsUtil.clearUserData(getActivity()); // Clear user data using SharedPreferences utility.
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}



