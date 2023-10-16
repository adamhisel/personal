package com.example.project;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.example.project.databinding.ActivityWorkoutBinding;

public class WorkoutActivity extends AppCompatActivity {

    private ActivityWorkoutBinding binding;
    private ImageView imageView;
    private Drawable green, red;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWorkoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        imageView = new ImageView(this);
        setContentView(imageView);

        green = ContextCompat.getDrawable(this, R.drawable.outline_circle_10);
        red = ContextCompat.getDrawable(this, R.drawable.outline_cancel_10);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float x = event.getX();
            float y = event.getY();

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Was it a make or miss?")
                    .setPositiveButton("Make", (dialog, id) -> {
                        imageView.setImageDrawable(green);
                        imageView.setX(x - (green.getIntrinsicWidth() / 2));
                        imageView.setY(y - (green.getIntrinsicHeight() / 2));
                    })
                    .setNegativeButton("Miss", (dialog, id) -> {
                        imageView.setImageDrawable(red);
                        imageView.setX(x - (red.getIntrinsicWidth() / 2));
                        imageView.setY(y - (red.getIntrinsicHeight() / 2));
                    });
            builder.create().show();
            return true;
        }
        return super.onTouchEvent(event);
    }
}