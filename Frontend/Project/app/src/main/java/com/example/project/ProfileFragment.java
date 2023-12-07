package com.example.project;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

/**
 * ProfileFragment handles user profile display and interactions within the app.
 * It fetches and displays the user's profile details and provides options to edit
 * the profile or log out.
 *
 * @author Jagger Gourley
 */
public class ProfileFragment extends Fragment {
    private static final String BASE_URL = "http://coms-309-018.class.las.iastate.edu:8080/";
    private static final String LOCAL_URL = "http://10.0.2.2:8080/";
    private static RequestQueue mQueue;
    private ImageDownloader imageDownloader;
    private FragmentProfileBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        mQueue = Volley.newRequestQueue(requireActivity());

        imageDownloader = new ImageDownloader();


        String userId = SharedPrefsUtil.getUserId(requireActivity());
        downloadAndSetImage(0);
        getProfile(userId);
        setupButtonListeners();

        return binding.getRoot();
    }

    private void downloadAndSetImage(int imageId) {
        imageDownloader.downloadImage(getContext(), imageId, binding.imageView);
    }

    // Refresh profile details when fragment is resumed.
    @Override
    public void onResume() {
        super.onResume();
        String userId = SharedPrefsUtil.getUserId(requireActivity());
        getProfile(userId); // Fetches the profile data again after returning from EditProfileActivity
    }

    //Sets up click listeners for the edit profile and logout buttons.
    private void setupButtonListeners() {

        binding.btnEditProfile.setOnClickListener(view -> {
            Intent intent = new Intent(requireActivity(), EditProfileActivity.class);
            startActivity(intent);
        });

        binding.btnLogout.setOnClickListener(view -> {
            handleLogout();
        });
    }

    // Fetches the user's profile from the server and updates the UI.
    private void getProfile(String userId) {
        String url = BASE_URL + "users/" + userId;
        String testUrl = LOCAL_URL + "users/" + userId;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (binding != null) {
                    try {
                        String userName = response.getString("userName");
                        String firstName = response.getString("firstName");
                        String lastName = response.getString("lastName");
                        String email = response.getString("email");
                        String phoneNumber = response.getString("phoneNumber");

                        binding.etUserName.setText(userName);
                        binding.etFirstName.setText(firstName);
                        binding.etLastName.setText(lastName);
                        binding.etEmail.setText(email);
                        binding.etPhoneNumber.setText(phoneNumber);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(requireActivity(), "Failed to fetch profile details!", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });

        mQueue.add(jsonObjectRequest);
    }

    // Clears the user's session data and navigates back to the login screen.
    private void handleLogout() {
        SharedPrefsUtil.clearUserData(requireActivity());
        Intent intent = new Intent(requireActivity(), LoginActivity.class);
        startActivity(intent);
        requireActivity().finish();
    }

    // Cleanup when the view is destroyed
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mQueue != null) {
            mQueue.cancelAll(TAG);
        }
        binding = null;
    }
}
