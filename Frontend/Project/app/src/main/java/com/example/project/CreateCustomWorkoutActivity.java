package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.project.databinding.ActivityCreateCustomWorkoutBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CreateCustomWorkoutActivity extends AppCompatActivity {

    private static final String BASE_URL = "http://coms-309-018.class.las.iastate.edu:8080/";
    private static final String LOCAL_URL = "http://10.0.2.2:8080/";
    private static final int ICON_SIZE_PX = (int) (20 * Resources.getSystem().getDisplayMetrics().density);
    private static RequestQueue mQueue;
    private List<Coordinate> customWorkoutCoordinates = new ArrayList<>();
    private ImageView imageView;
    private ActivityCreateCustomWorkoutBinding binding;
    private Drawable grey;
    private String workoutName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateCustomWorkoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.black));

        initializeViews();
        setupCourtImageView();

        String userId = SharedPrefsUtil.getUserId(this);
        mQueue = Volley.newRequestQueue(this);
    }

    // Initializes components and sets the user information in the TextView.
    private void initializeViews() {
        imageView = binding.courtImageView;
        grey = ContextCompat.getDrawable(this, R.drawable.grey_outline_circle_24);
        String userName = SharedPrefsUtil.getUserName(this);
        binding.tvUserInfo.setText(userName);

        binding.btnCreateWorkout.setOnClickListener(view -> {
            workoutName = binding.etWorkoutName.getText().toString().trim();
            if (workoutName.isEmpty()) {
                binding.etWorkoutName.setError("Please enter a workout name");
                return;
            }
            sendCustomWorkoutToServer();
            finish();
        });
    }

    private void setupCourtImageView() {
        imageView.post(() -> {
            int width = imageView.getWidth();
            float aspectRatio = 564f / 600f; // real court's height / width
            ViewGroup.LayoutParams params = imageView.getLayoutParams();
            int imageHeight = (int) (width * aspectRatio);
            params.height = imageHeight;
            imageView.setLayoutParams(params);

            // Set the top margins of the layout
            int nameTopMargin = imageHeight + 10;
            setViewTopMargin(binding.tilWorkoutName, nameTopMargin);

            int instrucTopMargin = nameTopMargin + binding.tilWorkoutName.getHeight() + 10;
            setViewTopMargin(binding.tvInstructions, instrucTopMargin);

            int shotsTopMargin = instrucTopMargin + binding.tvInstructions.getHeight() + 50;
            setViewTopMargin(binding.tvShotCount, shotsTopMargin);
        });

        imageView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                recordCustomWorkoutShot(event);
                return true;
            }
            return false;
        });
    }

    // Sets the top margin of a given view.
    private void setViewTopMargin(View view, int topMargin) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        layoutParams.topMargin = topMargin;
        view.setLayoutParams(layoutParams);
    }

    private void recordCustomWorkoutShot(MotionEvent event) {
        int x = Math.round(event.getX());
        int y = Math.round(event.getY());

        customWorkoutCoordinates.add(new Coordinate(x, y));
        binding.tvShotCount.setText("Shots: " + customWorkoutCoordinates.size());

        // Log the coordinates for debugging
        Log.d("CustomWorkout", "Coordinate added: X=" + x + ", Y=" + y);

        setIconAndPosition(grey, x, y);
    }

    private void setIconAndPosition(Drawable drawable, float x, float y) {
        // Create a new ImageView instance for each shot
        ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ICON_SIZE_PX, ICON_SIZE_PX));
        imageView.setImageDrawable(drawable);
        // Center the icon at the touched location
        imageView.setX(x - ICON_SIZE_PX / 2);
        imageView.setY(y - ICON_SIZE_PX / 2);
        // Add the new ImageView to the root layout
        binding.getRoot().addView(imageView);

        // Log the icon position for debugging
        Log.d("CustomWorkout", "Icon placed at: X=" + (x - ICON_SIZE_PX / 2) + ", Y=" + (y - ICON_SIZE_PX / 2));
    }

    private void sendCustomWorkoutToServer() {
        // First, create the CustomWorkout
        String createWorkoutUrl = BASE_URL + "uploadworkout";
        JSONObject workoutJson = new JSONObject();
        try {
            workoutJson.put("workoutName", workoutName);
            workoutJson.put("userId", SharedPrefsUtil.getUserId(this));
            // Log for debugging
            Log.d("CreateCustomWorkout", "Creating workout: " + workoutJson.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Create the CustomWorkout
        JsonObjectRequest createWorkoutRequest = new JsonObjectRequest(Request.Method.POST, createWorkoutUrl, workoutJson,
                response -> {
                    Log.d("CreateCustomWorkout", "Response for workout creation: " + response.toString());
                    // Fetch the created workout ID from the response
                    int customWorkoutId = response.optInt("customWoutId");
                    Log.d("CreateCustomWorkout", "Workout created with ID: " + customWorkoutId);
                    // Now send the coordinates (points) for this workout
                    sendPointsForWorkout(customWorkoutId);
                },
                error -> {
                    Log.e("CreateCustomWorkout", "Error creating workout: " + error.toString());
                    if (error.networkResponse != null) {
                        Log.e("CreateCustomWorkout", "Error details: " + new String(error.networkResponse.data));
                    }
                }
        );

        mQueue.add(createWorkoutRequest);
    }

    private void sendPointsForWorkout(int customWorkoutId) {
        // URL to add points to the created workout
        String addPointsUrl = BASE_URL + customWorkoutId + "/addPoints";

        // Create JSON array of points
        JSONArray pointsArray = new JSONArray();
        for (Coordinate coord : customWorkoutCoordinates) {
            JSONObject pointJson = new JSONObject();
            try {
                pointJson.put("xCoord", coord.getXCoord());
                pointJson.put("yCoord", coord.getYCoord());
                pointsArray.put(pointJson);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // Send the points
        JsonArrayRequest addPointsRequest = new JsonArrayRequest(Request.Method.POST, addPointsUrl, pointsArray,
                response -> Log.d("CreateCustomWorkout", "Points added successfully to workout ID: " + customWorkoutId),
                error -> {
                    Log.e("CreateCustomWorkout", "Error adding points to workout: " + error.toString());
                    if (error.networkResponse != null) {
                        Log.e("CreateCustomWorkout", "Error details: " + new String(error.networkResponse.data));
                    }
                }
        );

        // Log the JSON array of points being sent
        Log.d("CreateCustomWorkout", "Sending points JSON: " + pointsArray.toString());


        mQueue.add(addPointsRequest);
    }
}