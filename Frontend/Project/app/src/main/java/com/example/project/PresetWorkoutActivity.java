package com.example.project;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.project.databinding.ActivityPresetWorkoutBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PresetWorkoutActivity extends AppCompatActivity {

    private static final String BASE_URL = "http://coms-309-018.class.las.iastate.edu:8080/";
    private static final String LOCAL_URL = "http://10.0.2.2:8080/";
    private static final int ICON_SIZE_PX = (int) (20 * Resources.getSystem().getDisplayMetrics().density);
    private static RequestQueue mQueue;
    private final List<Shots> shotsList = new ArrayList<>();
    private ActivityPresetWorkoutBinding binding;
    private ImageView imageView;
    private Drawable green, grey;
    private int totalShots = 0;
    private int threePointMakes = 0;
    private int threePointAttempts = 0;
    private int twoPointMakes = 0;
    private int twoPointAttempts = 0;
    private List<Coordinate> workout1Coordinates;
    private List<Coordinate> workout2Coordinates;
    private List<Coordinate> workout3Coordinates;
    private List<Coordinate> currentWorkoutCoordinates;
    private int workoutId;
    private int currentShotIndex = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPresetWorkoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.black));

        initializeViews();
        setupCourtImageView();
        initializeWorkouts();

        String selectedWorkout = getIntent().getStringExtra("selectedWorkout");
        switch (selectedWorkout) {
            case "3PT Workout":
                currentWorkoutCoordinates = workout1Coordinates;
                break;
            case "Mid-Range Workout":
                currentWorkoutCoordinates = workout2Coordinates;
                break;
            case "Short-Range Workout":
                currentWorkoutCoordinates = workout3Coordinates;
                break;
            default:
        }

        binding.tvRemainingShotsValue.setText(String.valueOf(currentWorkoutCoordinates.size()));

        String userId = SharedPrefsUtil.getUserId(this);
        mQueue = Volley.newRequestQueue(this);
        createWorkout(userId);
    }

    // Initializes components and sets the user information in the TextView.
    private void initializeViews() {
        imageView = binding.courtImageView;
        green = ContextCompat.getDrawable(this, R.drawable.outline_circle_10);
        grey = ContextCompat.getDrawable(this, R.drawable.grey_outline_circle_24);
        String userName = SharedPrefsUtil.getUserName(this);
        binding.tvUserInfo.setText(userName);

        binding.btnMake.setOnClickListener(v -> recordMakeOrMiss(true));
        binding.btnMiss.setOnClickListener(v -> recordMakeOrMiss(false));

        binding.btnEndSession.setOnClickListener(view -> {
            sendShots(workoutId, shotsList);
            finish();
        });
    }

    // Configures the court ImageView dimensions and sets up the layout parameters.
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

            int instrucTopMargin = buttonsTopMargin + binding.llButtons.getHeight() + 25;
            setViewTopMargin(binding.tvInstructions, instrucTopMargin);

            int statsTopMargin = instrucTopMargin + binding.tvInstructions.getHeight();
            setViewTopMargin(binding.llStats, statsTopMargin);

            // Delay the initial call to display the first shot icon
            imageView.postDelayed(() -> displayNextShot(), 500); // Delay of 500 milliseconds
        });
    }

    // Sets the top margin of a given view.
    private void setViewTopMargin(View view, int topMargin) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        layoutParams.topMargin = topMargin;
        view.setLayoutParams(layoutParams);
    }

    private void initializeWorkouts() {
        workout1Coordinates = getWorkout1Coordinates();
        workout2Coordinates = getWorkout2Coordinates();
        workout3Coordinates = getWorkout3Coordinates();
    }

    private void displayNextShot() {
        Log.d("Debug", "In displayNextShot, currentShotIndex: " + currentShotIndex);
        if (currentShotIndex < currentWorkoutCoordinates.size()) {
            Coordinate currentShot = currentWorkoutCoordinates.get(currentShotIndex);
            setIconAndPosition(green, currentShot.getXCoord(), currentShot.getYCoord());
        } else {
            hideShotButtons();
            Toast.makeText(this, "Workout complete!", Toast.LENGTH_SHORT).show();
        }
    }

    private String determineShotType(float x, float y) {

        // Calculate the distance to the basket
        float distanceToBasket = (float) Math.sqrt(Math.pow(x - 300f, 2) + Math.pow(y - 65f, 2));

        String shotType;

        // Check for the straight side zones of the 3-point line
        boolean isBeyondSideZoneLeft = x <= (300f - 264f);
        boolean isBeyondSideZoneRight = x >= (300f + 264f);

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

        return shotType;
    }

    private void recordMakeOrMiss(boolean isMade) {
        Coordinate currentShot = currentWorkoutCoordinates.get(currentShotIndex);
        String shotType = determineShotType(currentShot.getXCoord(), currentShot.getYCoord());

        // Calculate the scaled real-world coordinates
        float realX = currentShot.getXCoord() / (600f / imageView.getWidth());
        float realY = currentShot.getYCoord() / (564f / imageView.getHeight());

        int value = "Three-Point Shot".equals(shotType) ? 3 : 2;

        // Add the shot with scaled coordinates to the shotsList
        shotsList.add(new Shots(isMade, value, (int) realX, (int) realY));
        Log.d("ShotLog", "Preset Shot added: Made=" + isMade + ", Value=" + value + ", X=" + realX + ", Y=" + realY);

        setIconAndPosition(grey, currentShot.getXCoord(), currentShot.getYCoord());

        if (isMade) {
            if ("Three-Point Shot".equals(shotType)) {
                threePointMakes++;
            } else {
                twoPointMakes++;
            }
        }

        totalShots++;
        updateStats();
        currentShotIndex++;
        displayNextShot();
    }


    // Sets the icon (make or miss) at the touched position on the imageView.
    private void setIconAndPosition(Drawable drawable, float x, float y) {
        // Create a new ImageView instance for each shot
        ImageView shotIcon = new ImageView(this);
        shotIcon.setLayoutParams(new ViewGroup.LayoutParams(ICON_SIZE_PX, ICON_SIZE_PX));
        shotIcon.setImageDrawable(drawable);

        // Calculate the scaled position based on the imageView size
        float scaledX = x * (imageView.getWidth() / 600f);
        float scaledY = y * (imageView.getHeight() / 564f);

        // Adjusting for the position of the imageView within its parent
        float adjustedX = scaledX + imageView.getX() - ICON_SIZE_PX;
        float adjustedY = scaledY + imageView.getY() - ICON_SIZE_PX;

        // Set the position of the icon
        shotIcon.setX(adjustedX);
        shotIcon.setY(adjustedY);

        // Add the new ImageView to the same layout as the court imageView
        ((ViewGroup) imageView.getParent()).addView(shotIcon);
    }

    // Updates the stats display with the current shooting percentage and counts.
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
        binding.tvRemainingShotsValue.setText(String.valueOf(currentWorkoutCoordinates.size() - currentShotIndex - 1));

    }

    // Creates a new workout session for the user by sending a request to the server.
    private void createWorkout(String userId) {
        String url = BASE_URL + "workouts?userId=" + userId;
        String testUrl = LOCAL_URL + "workouts?userId=" + userId;
        Log.d(TAG, "Creating workout for user with id" + userId);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null, response -> {
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

    // Sends the list of shots to the server to be associated with the current workout session.
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

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST, url, shotsArray, response -> {
            // Log the response
            Log.d(TAG, "Response received");
        }, error -> {
            Log.e(TAG, "Failed to send shots. Error: " + error.toString());
            // Handle error
        });

        mQueue.add(request);
    }

    // Hides the make and miss buttons from the screen.
    private void hideShotButtons() {
        binding.btnMake.setVisibility(View.GONE);
        binding.btnMiss.setVisibility(View.GONE);
    }

    private List<Coordinate> getWorkout1Coordinates() {
        List<Coordinate> coordinates = new ArrayList<>();
        coordinates.add(new Coordinate(18, 65));
        coordinates.add(new Coordinate(18, 65));
        coordinates.add(new Coordinate(18, 65));
        coordinates.add(new Coordinate(90, 280));
        coordinates.add(new Coordinate(90, 280));
        coordinates.add(new Coordinate(90, 280));
        coordinates.add(new Coordinate(300, 368));
        coordinates.add(new Coordinate(300, 368));
        coordinates.add(new Coordinate(300, 368));
        coordinates.add(new Coordinate(520, 280));
        coordinates.add(new Coordinate(520, 280));
        coordinates.add(new Coordinate(520, 280));
        coordinates.add(new Coordinate(582, 65));
        coordinates.add(new Coordinate(582, 65));
        coordinates.add(new Coordinate(582, 65));
        return coordinates;
    }

    private List<Coordinate> getWorkout2Coordinates() {
        List<Coordinate> coordinates = new ArrayList<>();
        coordinates.add(new Coordinate(115, 65));
        coordinates.add(new Coordinate(145, 235));
        coordinates.add(new Coordinate(300, 300));
        coordinates.add(new Coordinate(455, 235));
        coordinates.add(new Coordinate(485, 65));
        coordinates.add(new Coordinate(485, 65));
        coordinates.add(new Coordinate(455, 235));
        coordinates.add(new Coordinate(300, 300));
        coordinates.add(new Coordinate(145, 235));
        coordinates.add(new Coordinate(115, 65));
        return coordinates;
    }

    private List<Coordinate> getWorkout3Coordinates() {
        List<Coordinate> coordinates = new ArrayList<>();
        coordinates.add(new Coordinate(205, 90));
        coordinates.add(new Coordinate(205, 90));
        coordinates.add(new Coordinate(205, 140));
        coordinates.add(new Coordinate(205, 140));
        coordinates.add(new Coordinate(205, 180));
        coordinates.add(new Coordinate(205, 180));
        coordinates.add(new Coordinate(205, 225));
        coordinates.add(new Coordinate(205, 225));
        coordinates.add(new Coordinate(300, 245));
        coordinates.add(new Coordinate(300, 245));
        coordinates.add(new Coordinate(395, 225));
        coordinates.add(new Coordinate(395, 225));
        coordinates.add(new Coordinate(395, 180));
        coordinates.add(new Coordinate(395, 180));
        coordinates.add(new Coordinate(395, 140));
        coordinates.add(new Coordinate(395, 140));
        coordinates.add(new Coordinate(395, 90));
        coordinates.add(new Coordinate(395, 90));
        return coordinates;
    }
}


