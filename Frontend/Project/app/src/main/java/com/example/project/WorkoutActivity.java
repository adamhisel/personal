package com.example.project;

import static android.content.ContentValues.TAG;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.project.databinding.ActivityWorkoutBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * WorkoutActivity is an activity that represents a basketball workout session.
 * It allows users to track their basketball shots on a court, marking them as made or missed,
 * and provides feedback on shot types based on the location of the shot.
 */
public class WorkoutActivity extends AppCompatActivity {

    private static final String BASE_URL = "http://coms-309-018.class.las.iastate.edu:8080/";
    private static final String LOCAL_URL = "http://10.0.2.2:8080/";
    private static final int ICON_SIZE_PX = (int) (20 * Resources.getSystem().getDisplayMetrics().density);
    private static RequestQueue mQueue;
    private ActivityWorkoutBinding binding;
    private ImageView imageView;
    private Drawable green, red;
    private int totalShots = 0;
    private int threePointMakes = 0;
    private int threePointAttempts = 0;
    private int twoPointMakes = 0;
    private int twoPointAttempts = 0;
    private final List<Shots> shotsList = new ArrayList<>();
    private int workoutId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWorkoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.black));

        initializeViews();
        setupCourtImageView();
        setupShotTypeIndicator();

        String userId = SharedPrefsUtil.getUserId(this);
        mQueue = Volley.newRequestQueue(this);
        createWorkout(userId);
    }

    private void initializeViews() {
        imageView = binding.courtImageView;
        green = ContextCompat.getDrawable(this, R.drawable.outline_circle_10);
        red = ContextCompat.getDrawable(this, R.drawable.outline_cancel_10);
        String userName = SharedPrefsUtil.getUserName(this);
        binding.tvUserInfo.setText(userName);

        hideShotButtons();

        binding.btnEndSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendShots(workoutId, shotsList);
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

            // Set the top margin of the buttons and stats layout
            int buttonsTopMargin = imageHeight;
            setViewTopMargin(binding.llButtons, buttonsTopMargin + 10);

            int statsTopMargin = buttonsTopMargin + binding.llButtons.getHeight() + 50; // 50 is the space between buttons and stats
            setViewTopMargin(binding.llStats, statsTopMargin);
        });
    }

    private void setViewTopMargin(View view, int topMargin) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        layoutParams.topMargin = topMargin;
        view.setLayoutParams(layoutParams);
    }


    private void setupShotTypeIndicator() {
        imageView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                recordBasketballShot(event);
                return true;
            }
            return false;
        });
    }

    private void recordBasketballShot(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        // Scaling factors
        float widthScale = 600f / imageView.getWidth();
        float heightScale = 564f / imageView.getHeight();

        // Real-world coordinates of the touch point
        float realX = x * widthScale;
        float realY = y * heightScale;

        // Calculate the distance to the basket
        float distanceToBasket = (float) Math.sqrt(Math.pow(realX - 300f, 2) + Math.pow(realY - 65f, 2));

        String shotType;

        // Check for the straight side zones of the 3-point line
        boolean isBeyondSideZoneLeft = realX <= (300f - 264f);
        boolean isBeyondSideZoneRight = realX >= (300f + 264f);

        if (isBeyondSideZoneLeft || isBeyondSideZoneRight || distanceToBasket >= 285) {
            shotType = "Three-Point Shot";
        } else {
            shotType = "Two-Point Shot";
        }

        if ("Three-Point Shot".equals(shotType)) {
            threePointAttempts++;
        } else {
            twoPointAttempts++;
        }

        showShotButtons();

        // Show dialog to record shot as make or miss
        recordMakeOrMiss(shotType, x, y);
    }

    private void recordMakeOrMiss(String shotType, float x, float y) {
        binding.btnMake.setOnClickListener(v -> {
            int value = "Three-Point Shot".equals(shotType) ? 3 : 2;
            shotsList.add(new Shots(true, value, (int) x, (int) y));
            setIconAndPosition(green, x + imageView.getLeft(), y + imageView.getTop());
            if ("Three-Point Shot".equals(shotType)) {
                threePointMakes++;
            } else {
                twoPointMakes++;
            }
            totalShots++;
            updateStats();
            hideShotButtons();
        });

        binding.btnMiss.setOnClickListener(v -> {
            int value = "Three-Point Shot".equals(shotType) ? 3 : 2;
            shotsList.add(new Shots(false, value, (int) x, (int) y));
            setIconAndPosition(red, x + imageView.getLeft(), y + imageView.getTop());
            totalShots++;
            updateStats();
            hideShotButtons();
        });
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

        binding.tvFGPercentValue.setText(String.format("%.2f%%", shootingPercentage));
        binding.tvThreePointValue.setText(String.format("%d/%d", threePointMakes, threePointAttempts));
        binding.tvTwoPointValue.setText(String.format("%d/%d", twoPointMakes, twoPointAttempts));
        binding.tvTotalShotsValue.setText(String.format("%d/%d", (twoPointMakes + threePointMakes), totalShots));
    }

    private void createWorkout(String userId) {
        String url = BASE_URL + "workouts?userId=" + userId;
        String testUrl = LOCAL_URL + "workouts?userId=" + userId;
        Log.d(TAG, "Creating workout for user with id" + userId);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null,
                response -> {
                    // Handle response
                    workoutId = response.optInt("workoutId");
                    Log.d(TAG, "Workout created successfully. Received workoutId: " + workoutId);
                    // Send shots to this workout using workoutId
                }, error -> {
            Log.e(TAG, "Failed to create workout. Error: " + error.toString());
            // Handle error
        });

        mQueue.add(request);
    }

    private void sendShots(int workoutId, List<Shots> shotsList) {
        String url = BASE_URL + "workouts/" + workoutId + "/bulk-shots";
        String testUrl = LOCAL_URL + "workouts/" + workoutId + "/bulk-shots";

        JSONArray shotsArray = new JSONArray();
        for (Shots shot : shotsList) {
            JSONObject shotObject = new JSONObject();
            try {
                shotObject.put("made", shot.isMade());
                shotObject.put("value", shot.getValue());
                shotObject.put("xCoord", shot.getxCoord());
                shotObject.put("yCoord", shot.getyCoord());
                shotsArray.put(shotObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Log.d(TAG, "Sending shots: " + shotsArray);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST, url, shotsArray,
                response -> {
                    // Log the response
                    Log.d(TAG, "Response received");
                },
                error -> {
                    Log.e(TAG, "Failed to send shots. Error: " + error.toString());
                    // Handle error
                });

        mQueue.add(request);
    }

    private void showShotButtons() {
        binding.btnMake.setVisibility(View.VISIBLE);
        binding.btnMiss.setVisibility(View.VISIBLE);
    }

    private void hideShotButtons() {
        binding.btnMake.setVisibility(View.GONE);
        binding.btnMiss.setVisibility(View.GONE);
    }

}
