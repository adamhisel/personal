package com.example.project;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.example.project.databinding.FragmentProfileBinding;
import com.example.project.databinding.FragmentWorkoutBinding;

/**
 * WorkoutFragment is a Fragment class responsible for displaying the workout screen in the application.
 * It handles user interactions on the workout screen and directs the user to the WorkoutActivity when a button is clicked.
 */
public class WorkoutFragment extends Fragment {

    /**
     * Base URL for the remote server connection
     */
    private static final String BASE_URL = "http://coms-309-018.class.las.iastate.edu:8080/";

    /**
     * Local URL for the local server connection
     */
    private static final String LOCAL_URL = "http://10.0.2.2:8080/";

    /**
     * Binding instance for accessing the view elements
     */
    private FragmentWorkoutBinding binding;

    /**
     * RequestQueue for managing network requests
     */
    private static RequestQueue mQueue;

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * @param inflater           LayoutInflater object that can be used to inflate any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's UI should be attached to.
     *                           The fragment should not add the view itself, but this can be used to generate the
     *                           LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentWorkoutBinding.inflate(inflater, container, false);

        setupButtonListeners();

        return binding.getRoot();
    }

    /**
     * Sets up the button listeners for the user interface.
     */
    private void setupButtonListeners() {
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireActivity(), WorkoutActivity.class);
                startActivity(intent);
            }
        });
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