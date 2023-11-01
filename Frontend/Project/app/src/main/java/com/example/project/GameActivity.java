package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.project.databinding.ActivityGameBinding;

import java.util.ArrayList;
import java.util.List;

public class GameActivity extends AppCompatActivity {

    private static final String BASE_URL = "http://coms-309-018.class.las.iastate.edu:8080/";

    private static final String LOCAL_URL = "http://10.0.2.2:8080/";
    /**
     * Size of the icons (made or missed shots) in pixels
     */
    private static final int ICON_SIZE_PX = (int) (20 * Resources.getSystem().getDisplayMetrics().density);

    /**
     * View binding for this activity
     */
    private ActivityGameBinding binding;

    /**
     * ImageView representing the basketball court
     */
    private ImageView imageView;

    /**
     * Drawable for a made shot (green circle) or missed shot (red cross)
     */
    private Drawable green, red;

    private int totalShots = 0;
    private int threePointMakes = 0;
    private int threePointAttempts = 0;
    private int twoPointMakes = 0;
    private int twoPointAttempts = 0;

    private int[] playerButtonIds = new int[] {
            R.id.imgBtnPlayer1, R.id.imgBtnPlayer2, R.id.imgBtnPlayer3, R.id.imgBtnPlayer4, R.id.imgBtnPlayer5
    };

    private List<Player> allPlayers = new ArrayList<>();
    private List<Player> activeLineup = new ArrayList<>();
    private Player activePlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initializeViews();
        setupCourtImageView();
        setupShotTypeIndicator();


    }

    private void initializeViews() {
        imageView = binding.courtImageView;
        green = ContextCompat.getDrawable(this, R.drawable.outline_circle_10);
        red = ContextCompat.getDrawable(this, R.drawable.outline_cancel_10);

        hideShotButtons();

        binding.imgBtnPlayer1.setOnClickListener(v -> setActivePlayer(1));
        binding.imgBtnPlayer2.setOnClickListener(v -> setActivePlayer(2));
        binding.imgBtnPlayer3.setOnClickListener(v -> setActivePlayer(3));
        binding.imgBtnPlayer4.setOnClickListener(v -> setActivePlayer(4));
        binding.imgBtnPlayer5.setOnClickListener(v -> setActivePlayer(5));

        binding.btnEndSession.setOnClickListener(new View.OnClickListener() {
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
        imageView.setX(x - ICON_SIZE_PX/2);
        imageView.setY(y - ICON_SIZE_PX/2);
        // Add the new ImageView to the root layout
        binding.getRoot().addView(imageView);
    }

    private void updateStats() {
        // Update the TextViews with the new stats
        float shootingPercentage = 0;
        if (totalShots > 0) {
            shootingPercentage = (float) (threePointMakes + twoPointMakes) / totalShots * 100;
        }

    }

    private void setActivePlayer(Player player) {
        activePlayer = player;
        // Reset background color for all player buttons
        for (int i = 0; i < playerButtonIds.length; i++) {
            ImageButton button = findViewById(playerButtonIds[i]);
            button.setBackgroundColor(Color.TRANSPARENT); // Or any default color
        }

        // Set background color for active player's button
        ImageButton activePlayerButton = findViewById(playerButtonIds[playerNumber - 1]);
        activePlayerButton.setBackgroundColor(Color.DKGRAY); // Dark color for active player
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

