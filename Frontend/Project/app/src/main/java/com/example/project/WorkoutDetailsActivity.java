package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.project.databinding.ActivityWorkoutBinding;
import com.example.project.databinding.ActivityWorkoutDetailsBinding;

import org.json.JSONException;
import org.json.JSONObject;

public class WorkoutDetailsActivity extends AppCompatActivity {

    private static final String BASE_URL = "http://coms-309-018.class.las.iastate.edu:8080/";

    private static final String LOCAL_URL = "http://10.0.2.2:8080/";

    private static final int ICON_SIZE_PX = (int) (20 * Resources.getSystem().getDisplayMetrics().density);
    private ActivityWorkoutDetailsBinding binding;
    private ImageView imageView;
    private static RequestQueue mQueue;
    private Drawable green, red;
    private int totalShots = 0;
    private int threePointMakes = 0;
    private int threePointAttempts = 0;
    private int twoPointMakes = 0;
    private int twoPointAttempts = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWorkoutDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int workoutId = getIntent().getIntExtra("WORKOUT_ID", -1);

        mQueue = Volley.newRequestQueue(this);
        initializeViews();
        setupCourtImageView();
        fetchAndDisplayShots(workoutId);
    }

    private void initializeViews() {
        imageView = binding.courtImageView;
        green = ContextCompat.getDrawable(this, R.drawable.outline_circle_10);
        red = ContextCompat.getDrawable(this, R.drawable.outline_cancel_10);


        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setupCourtImageView() {
        imageView.post(() -> {
            int width = imageView.getWidth();
            float aspectRatio = 564f / 600f;  // real court's height / width
            ViewGroup.LayoutParams params = imageView.getLayoutParams();
            params.height = (int) (width * aspectRatio);
            imageView.setLayoutParams(params);
        });
    }

    private void fetchAndDisplayShots(int workoutId) {
        String url = LOCAL_URL + "workouts/" + workoutId + "/shots";

        // Create a JSON Array request for fetching the shots data
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, url, null,
                response -> {
                    // Parse the response and add shots to the view
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject shot = response.getJSONObject(i);
                            boolean made = shot.getBoolean("made");
                            int value = shot.getInt("value");
                            float xCoord = (float) shot.getDouble("xCoord");
                            float yCoord = (float) shot.getDouble("yCoord");

                            // You may need to scale these coordinates depending on how they match with your imageView's dimensions
                            Drawable drawable = made ? green : red;
                            setIconAndPosition(drawable, xCoord, yCoord);

                            // Update your statistics accordingly
                            if (value == 3) {
                                threePointAttempts++;
                                if (made) threePointMakes++;
                            } else {
                                twoPointAttempts++;
                                if (made) twoPointMakes++;
                            }
                            totalShots++;
                        }
                        // After all shots are processed, update the stats display
                        updateStats();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    // Handle the error
                    Log.e("Volley", "Error fetching shot data", error);
                }
        );

        // Add the request to your RequestQueue
        mQueue.add(jsonArrayRequest);
    }

    private void setIconAndPosition(Drawable drawable, float x, float y) {
        // Create a new ImageView instance for each shot
        ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ICON_SIZE_PX, ICON_SIZE_PX));
        imageView.setImageDrawable(drawable);
        // Center the icon at the touched location
        imageView.setX(x - ICON_SIZE_PX);
        imageView.setY(y - ICON_SIZE_PX);
        // Add the new ImageView to the root layout
        binding.getRoot().addView(imageView);
    }

    private void updateStats() {
        // Update the TextViews with the new stats
        float shootingPercentage = 0;
        if (totalShots > 0) {
            shootingPercentage = (float) (threePointMakes + twoPointMakes) / totalShots * 100;
        }

        binding.shootingPercentageTextView.setText(String.format("Shooting Percentage: %.2f%%", shootingPercentage));
        binding.threePointRatioTextView.setText(String.format("3 Point Ratio: %d/%d", threePointMakes, threePointAttempts));
        binding.twoPointRatioTextView.setText(String.format("2 Point Ratio: %d/%d", twoPointMakes, twoPointAttempts));
        binding.totalShotsTextView.setText(String.format("Total Shots: %d", totalShots));
    }


}