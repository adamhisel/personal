package com.example.project;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.project.databinding.FragmentWorkoutBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * WorkoutFragment is a Fragment class responsible for displaying the workout screen in the application.
 * It handles user interactions on the workout screen and directs the user to the WorkoutActivity when a button is clicked.
 *
 * @author Jagger Gourley
 */
public class WorkoutFragment extends Fragment {

    private static final String BASE_URL = "http://coms-309-018.class.las.iastate.edu:8080/";
    private static final String LOCAL_URL = "http://10.0.2.2:8080/";
    private static final String[] PRESET_WORKOUTS = {"3PT Workout", "Mid-Range Workout", "Short-Range Workout"};
    private static RequestQueue mQueue;
    private FragmentWorkoutBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentWorkoutBinding.inflate(inflater, container, false);

        setupButtonListeners();

        String userId = SharedPrefsUtil.getUserId(requireActivity());
        fetchWorkoutsForUser(userId);
        requireActivity().getWindow().setStatusBarColor(ContextCompat.getColor(requireContext(), R.color.black));
        AutoCompleteTextView autoCompleteTextView = binding.actvPresetWorkouts;
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, PRESET_WORKOUTS);
        autoCompleteTextView.setAdapter(adapter);

        autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedWorkout = (String) parent.getItemAtPosition(position);
            Intent intent = new Intent(getActivity(), PresetWorkoutActivity.class);
            intent.putExtra("selectedWorkout", selectedWorkout);
            startActivity(intent);
        });

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh your list of workouts
        String userId = SharedPrefsUtil.getUserId(requireActivity());
        fetchWorkoutsForUser(userId);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setupButtonListeners() {
        binding.btnBegin.setOnClickListener(view -> {
            Intent intent = new Intent(requireActivity(), WorkoutActivity.class);
            startActivity(intent);
        });
    }

    // Method to fetch workouts for a specific user
    private void fetchWorkoutsForUser(String userId) {
        String url = BASE_URL + "workouts?userId=" + userId;
        String testUrl = LOCAL_URL + "workouts?userId=" + userId;

        // Initialize the request queue if it's null
        if (mQueue == null) {
            mQueue = Volley.newRequestQueue(requireActivity());
        }

        // Create a GET request to fetch workouts
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            // Parse the response to get a list of workouts
            List<Workout> workouts = parseWorkouts(response);
            displayWorkouts(workouts);
        }, error -> {
            // Handle error
            Log.e("WorkoutFragment", "Error fetching workouts: " + error.getMessage());
        });

        // Add the request to the Volley request queue
        mQueue.add(request);
    }

    // Method to parse the JSON response into a list of Workout objects
    private List<Workout> parseWorkouts(JSONArray response) {
        List<Workout> workouts = new ArrayList<>();
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject workoutJson = response.getJSONObject(i);
                int workoutId = workoutJson.getInt("workoutId");
                // Initialize an empty list of shots
                // Since we are not displaying shots here, it's okay to not fetch them at this stage.
                List<Shots> shots = new ArrayList<>();
                Workout workout = new Workout(workoutId, shots);
                workouts.add(workout);
            }
        } catch (JSONException e) {
            Log.e("WorkoutFragment", "Error parsing workouts JSON", e);
        }
        return workouts;
    }

    // Method to display the workouts in the UI
    private void displayWorkouts(List<Workout> workouts) {
        if (binding == null) {
            // Fragment view is destroyed, no need to update UI
            return;
        }
        LinearLayout workoutsContainer = binding.llWorkoutsContainer;
        workoutsContainer.removeAllViews(); // Clear any existing views

        for (Workout workout : workouts) {
            // Create a new button for each workout
            Button button = new Button(getContext());
            button.setText("Workout " + workout.getWorkoutId());
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle button click
                    Intent intent = new Intent(requireActivity(), WorkoutDetailsActivity.class);
                    intent.putExtra("WORKOUT_ID", workout.getWorkoutId());
                    startActivity(intent);
                }
            });

            // Add the button to the LinearLayout
            workoutsContainer.addView(button);
        }
    }
}