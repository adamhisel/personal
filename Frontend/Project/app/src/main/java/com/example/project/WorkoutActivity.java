package com.example.project;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project.databinding.ActivityWorkoutBinding;

/**
 * WorkoutActivity is an activity that represents a basketball workout session.
 * It allows users to track their basketball shots on a court, marking them as made or missed,
 * and provides feedback on shot types based on the location of the shot.
 */
public class WorkoutActivity extends AppCompatActivity {

    /**
     * Size of the icons (made or missed shots) in pixels
     */
    private static final int ICON_SIZE_PX = (int) (20 * Resources.getSystem().getDisplayMetrics().density);

    /**
     * View binding for this activity
     */
    private ActivityWorkoutBinding binding;

    /**
     * ImageView representing the basketball court
     */
    private ImageView imageView;

    /**
     * Drawable for a made shot (green circle) or missed shot (red cross)
     */
    private Drawable green, red;

    /**
     * TextView for displaying shot information and coordinates
     */
    private TextView coordinatesTextView;

    /**
     * Called when the activity is starting.
     * This is where most initialization should go: calling setContentView(int) to inflate
     * the activity's UI, using findViewById(int) to programmatically interact with widgets in the UI.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down
     *                           then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     *                           Note: Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWorkoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initializeViews();
        setupCourtImageView();
        setupShotTypeIndicator();
    }

    /**
     * Initializes view elements and related objects.
     */
    private void initializeViews() {
        imageView = binding.courtImageView;
        coordinatesTextView = findViewById(R.id.coordinatesTextView);
        green = ContextCompat.getDrawable(this, R.drawable.outline_circle_10);
        red = ContextCompat.getDrawable(this, R.drawable.outline_cancel_10);
    }

    /**
     * Sets up the court ImageView's dimensions based on the aspect ratio of a real basketball court.
     */
    private void setupCourtImageView() {
        imageView.post(() -> {
            int width = imageView.getWidth();
            float aspectRatio = 564f / 600f;  // real court's height / width
            ViewGroup.LayoutParams params = imageView.getLayoutParams();
            params.height = (int) (width * aspectRatio);
            imageView.setLayoutParams(params);
        });
    }

    /**
     * Sets up the touch listener for the court ImageView to handle basketball shot interactions.
     */
    private void setupShotTypeIndicator() {
        imageView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                recordBasketballShot(event);
                return true;
            }
            return false;
        });
    }

    /**
     * Records a basketball shot based on the touch event's location and
     * shows a dialog to record it as a make or miss.
     *
     * @param event The MotionEvent associated with the touch event.
     */
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

        // Display the shot type and transformed coordinates
        coordinatesTextView.setText(String.format("Shot Type: %s, X: %.2f, Y: %.2f", shotType, realX, realY));

        // Show dialog to record shot as make or miss
        showShotResultDialog(shotType, x, y);
    }

    /**
     * Shows a dialog to let the user record a basketball shot as a make or miss.
     *
     * @param shotType Type of the shot (e.g., "Two-Point Shot" or "Three-Point Shot").
     * @param x        The x-coordinate of the touch event.
     * @param y        The y-coordinate of the touch event.
     */
    private void showShotResultDialog(String shotType, float x, float y) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(shotType)
                .setMessage("Was it a make or miss?")
                .setPositiveButton("Make", (dialog, id) -> setIconAndPosition(green, x + imageView.getLeft(), y + imageView.getTop()))
                .setNegativeButton("Miss", (dialog, id) -> setIconAndPosition(red, x + imageView.getLeft(), y + imageView.getTop()));
        builder.create().show();
    }

    /**
     * Helper method to set an icon (representing a basketball shot) at the specified screen coordinates.
     *
     * @param drawable The Drawable to set as the image for the ImageView.
     * @param x        The x-coordinate at which to set the ImageView.
     * @param y        The y-coordinate at which to set the ImageView.
     */
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

}
