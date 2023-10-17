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

import com.example.project.databinding.ActivityWorkoutBinding;

public class WorkoutActivity extends AppCompatActivity {

    private static final int ICON_SIZE_PX = (int) (20 * Resources.getSystem().getDisplayMetrics().density);
    private ActivityWorkoutBinding binding;
    private ImageView imageView;
    private Drawable green, red;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWorkoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        green = ContextCompat.getDrawable(this, R.drawable.outline_circle_10);
        red = ContextCompat.getDrawable(this, R.drawable.outline_cancel_10);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Detect a touch down event
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float x = event.getX();
            float y = event.getY();

            // Ignore touches on the bottom half of the screen
            if (y > binding.getRoot().getHeight() / 2) {
                return super.onTouchEvent(event);
            }

            // Show a dialog to let the user record a make or miss
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Was it a make or miss?")
                    .setPositiveButton("Make", (dialog, id) -> {
                        setIconAndPosition(green, x, y);
                    })
                    .setNegativeButton("Miss", (dialog, id) -> {
                        setIconAndPosition(red, x, y);
                    });
            builder.create().show();
            return true;
        }
        return super.onTouchEvent(event);
    }


    //  Helper method to set the icon and position it at the specified x and y coordinates.
    private void setIconAndPosition(Drawable drawable, float x, float y) {
        // Create a new ImageView instance for each shot
        ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ICON_SIZE_PX, ICON_SIZE_PX));
        imageView.setImageDrawable(drawable);
        // Center the icon at the touched location
        imageView.setX(x - (ICON_SIZE_PX / 2));
        imageView.setY(y - (ICON_SIZE_PX / 2));
        // Add the new ImageView to the root layout
        binding.getRoot().addView(imageView);
    }
}