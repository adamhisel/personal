package com.example.project;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.project.databinding.ActivityWorkoutDetailsBinding;

import org.json.JSONException;
import org.json.JSONObject;

public class WorkoutDetailsActivity extends AppCompatActivity {

    private static final String BASE_URL = "http://coms-309-018.class.las.iastate.edu:8080/";
    private static final String LOCAL_URL = "http://10.0.2.2:8080/";
    private static final int ICON_SIZE_PX = (int) (20 * Resources.getSystem().getDisplayMetrics().density);
    private static RequestQueue mQueue;
    private ActivityWorkoutDetailsBinding binding;
    private ImageView imageView;
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
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.black));

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


        binding.btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
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

            int statsTopMargin = imageHeight + 100;
            setViewTopMargin(binding.llStats, statsTopMargin);
        });
    }

    private void setViewTopMargin(View view, int topMargin) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        layoutParams.topMargin = topMargin;
        view.setLayoutParams(layoutParams);
    }

    private void fetchAndDisplayShots(int workoutId) {
        String url = BASE_URL + "workouts/" + workoutId + "/shots";
        String testUrl = LOCAL_URL + "workouts/" + workoutId + "/shots";

        // Create a JSON Array request for fetching the shots data
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, testUrl, null, response -> {
            // Parse the response and add shots to the view
            try {
                for (int i = 0; i < response.length(); i++) {
                    JSONObject shot = response.getJSONObject(i);
                    boolean made = shot.getBoolean("made");
                    int value = shot.getInt("value");
                    float xCoord = (float) shot.getDouble("xCoord");
                    float yCoord = (float) shot.getDouble("yCoord");

                    Drawable drawable = made ? green : red;
                    setIconAndPosition(drawable, xCoord, yCoord);

                    // Update statistics accordingly
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
        }, error -> {
            // Handle the error
            Log.e("Volley", "Error fetching shot data", error);
        });

        // Add the request to RequestQueue
        mQueue.add(jsonArrayRequest);
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
    }

    private void updateStats() {
        // Update the TextViews with the new stats
        float shootingPercentage = 0;
        if (totalShots > 0) {
            shootingPercentage = (float) (threePointMakes + twoPointMakes) / totalShots * 100;
        }

        binding.tvFGPercentValue.setText(String.format("%.2f%%", shootingPercentage));
        binding.tvThreePointValue.setText(String.format("%d/%d", threePointMakes, threePointAttempts));
        binding.tvTwoPointValue.setText(String.format("%d/%d", twoPointMakes, twoPointAttempts));
        binding.tvTotalShotsValue.setText(String.format("%d/%d", (twoPointMakes + threePointMakes), totalShots));
    }
}