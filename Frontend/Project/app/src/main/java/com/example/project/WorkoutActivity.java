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

public class WorkoutActivity extends AppCompatActivity {

    private static final int ICON_SIZE_PX = (int) (20 * Resources.getSystem().getDisplayMetrics().density);
    private ActivityWorkoutBinding binding;
    private ImageView imageView;
    private Drawable green, red;
    private TextView coordinatesTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWorkoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        imageView = binding.courtImageView;
        coordinatesTextView = findViewById(R.id.coordinatesTextView);

        imageView.post(() -> {
            int width = imageView.getWidth();
            ViewGroup.LayoutParams params = imageView.getLayoutParams();
            params.height = width;
            imageView.setLayoutParams(params);
        });

        green = ContextCompat.getDrawable(this, R.drawable.outline_circle_10);
        red = ContextCompat.getDrawable(this, R.drawable.outline_cancel_10);

        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    float x = event.getX();
                    float y = event.getY();

                    // Transform the coordinates
                    float transformedX = imageView.getWidth() - x;
                    float transformedY = imageView.getHeight() - y;

                    // Display the transformed coordinates on the TextView
                    coordinatesTextView.setText(String.format("X: %.2f, Y: %.2f", transformedX, transformedY));

                    // Show a dialog to let the user record a make or miss
                    AlertDialog.Builder builder = new AlertDialog.Builder(WorkoutActivity.this);
                    builder.setMessage("Was it a make or miss?").setPositiveButton("Make", (dialog, id) -> {
                        setIconAndPosition(green, transformedX + imageView.getLeft(), transformedY + imageView.getTop());
                    }).setNegativeButton("Miss", (dialog, id) -> {
                        setIconAndPosition(red, transformedX + imageView.getLeft(), transformedY + imageView.getTop());
                    });
                    builder.create().show();
                    return true;
                }
                return false;
            }
        });

    }

    //  Helper method to set the icon and position it at the specified x and y coordinates.
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

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}

