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
import com.example.project.databinding.ActivityPlayerRegistrationBinding;
import com.example.project.databinding.FragmentProfileBinding;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * ProfileFragment is responsible for displaying and managing the user's profile information.
 * It allows users to view their current profile details and provides options to edit the profile
 * or log out of the application.
 */
public class ProfileFragment extends Fragment {

    /**
     * Base URL for the remote server connection
     */
    private static final String BASE_URL = "http://coms-309-018.class.las.iastate.edu:8080/";

    /**
     * Local URL for the local server connection
     */
    private static final String LOCAL_URL = "http://10.0.2.2:8080/";

    /**
     * View binding for this activity
     */
    private FragmentProfileBinding binding;
    /**
     * RequestQueue for handling network requests.
     */
    private static RequestQueue mQueue;

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * @param inflater           LayoutInflater: The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container          ViewGroup: If non-null, this is the parent view that the fragment's UI should be attached to.
     *                           The fragment should not add the view itself, but this can be used to generate the LayoutParams
     *                           of the view.
     * @param savedInstanceState Bundle: If non-null, this fragment is being re-constructed from a previous saved state
     *                           as given here.
     * @return View: Return the View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        mQueue = Volley.newRequestQueue(requireActivity());

        String userId = SharedPrefsUtil.getUserId(requireActivity());
        getProfile(userId);
        setupButtonListeners();

        return binding.getRoot();
    }

    /**
     * Called when the Fragment is visible to the user. This is generally tied to Activity.onResume of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onResume() {
        super.onResume();
        String userId = SharedPrefsUtil.getUserId(requireActivity());
        getProfile(userId); // Fetches the profile data again after returning from EditProfileActivity
    }

    /**
     * Sets up button listeners for profile interactions.
     */
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

    /**
     * Fetches the user's profile information from the server and updates the UI.
     *
     * @param userId The ID of the user whose profile is being fetched.
     */
    private void getProfile(String userId) {
        String url = BASE_URL + "users/" + userId;
        String testUrl = LOCAL_URL + "users/" + userId;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, testUrl, null,
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

    /**
     * Handles the logout functionality, clearing user data and navigating back to the login screen.
     */
    private void handleLogout() {
        SharedPrefsUtil.clearUserData(requireActivity());
        Intent intent = new Intent(requireActivity(), LoginActivity.class);
        startActivity(intent);
        requireActivity().finish();
    }

    /**
     * Called when the view previously created by onCreateView(LayoutInflater, ViewGroup, Bundle) has
     * been detached from the fragment. The next time the fragment needs to be displayed, a new view will
     * be created. This is called after onStop() and before onDestroy(). It is called regardless of whether
     * onCreateView(LayoutInflater, ViewGroup, Bundle) returned a non-null view. Internally it is called
     * after the view's state has been saved but before it has been removed from its parent.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
