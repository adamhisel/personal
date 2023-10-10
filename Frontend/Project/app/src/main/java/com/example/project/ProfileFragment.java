package com.example.project;

import android.content.Intent;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.project.databinding.FragmentProfileBinding;

import org.json.JSONException;
import org.json.JSONObject;


public class ProfileFragment extends Fragment {

    private static final String BASE_URL = "http://coms-309-018.class.las.iastate.edu:8080/";
    private FragmentProfileBinding binding;
    private static RequestQueue mQueue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        mQueue = Volley.newRequestQueue(requireActivity());

        String userId = SharedPrefsUtil.getUserId(requireActivity());
        getProfile(userId);
        setupButtonListeners();

        return binding.getRoot();
    }

    private void setupButtonListeners() {

        binding.btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireActivity(), EditProfileActivity.class);
                startActivity(intent);
            }
        });

        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogout();
            }
        });


    }

    private void getProfile(String userId) {
        String url = BASE_URL + userId;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String username = response.getString("userName");
                            String email = response.getString("email");
                            String phoneNumber = response.getString("phoneNumber");

                            binding.etUserName.setText(username);
                            binding.etEmail.setText(email);
                            binding.etPhoneNumber.setText(phoneNumber);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(requireActivity(), "Failed to fetch profile details!", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                });

        mQueue.add(jsonObjectRequest);
    }

    private void handleLogout() {
        SharedPrefsUtil.clearUserData(requireActivity());
        Intent intent = new Intent(requireActivity(), LoginActivity.class);
        startActivity(intent);
        requireActivity().finish();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
