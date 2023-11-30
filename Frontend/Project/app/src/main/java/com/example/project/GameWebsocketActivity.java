package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.project.databinding.ActivityGameWebsocketBinding;

/**
 * Activity responsible for handling WebSocket connections for the game.
 * It subscribes to WebSocket events and handles user interactions.
 *
 * @author Jagger Gourley
 */
public class GameWebsocketActivity extends AppCompatActivity implements WebSocketListener {

    private static final String BASE_URL = "ws://coms-309-018.class.las.iastate.edu:8080/game/";
    private static final String LOCAL_URL = "ws://10.0.2.2:8080/game/";
    private static final int ICON_SIZE_PX = (int) (20 * Resources.getSystem().getDisplayMetrics().density);
    private ActivityGameWebsocketBinding binding;
    private String userName;
    private ImageView imageView;
    private Drawable green, red;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGameWebsocketBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userName = SharedPrefsUtil.getUserName(this);
        String url = BASE_URL + userName;
        String testUrl = LOCAL_URL + userName;

        initializeViews();
        setupCourtImageView();

        initializeWebSocketConnection(url);
        setupButtonListeners();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WebSocketManager.getInstance().disconnectWebSocket();
        WebSocketManager.getInstance().removeWebSocketListener();
    }

    // Initializes view components
    private void initializeViews() {
        imageView = binding.courtImageView;
        green = ContextCompat.getDrawable(this, R.drawable.outline_circle_10);
        red = ContextCompat.getDrawable(this, R.drawable.outline_cancel_10);

        binding.btnExit.setOnClickListener(view -> {
            finish();
        });
    }

    // Sets up the court image view with correct aspect ratio
    private void setupCourtImageView() {
        imageView.post(() -> {
            int width = imageView.getWidth();
            float aspectRatio = 564f / 600f; // real court's height / width
            ViewGroup.LayoutParams params = imageView.getLayoutParams();
            int imageHeight = (int) (width * aspectRatio);
            params.height = imageHeight;
            imageView.setLayoutParams(params);
        });
    }

    // Sets up websocket connection based on url provided.
    private void initializeWebSocketConnection(String url) {
        WebSocketManager.getInstance().setWebSocketListener(this);
        WebSocketManager.getInstance().connectWebSocket(url);
    }

    //Sets up click listeners for the buttons within the fragment.
    private void setupButtonListeners() {
        binding.btnExit.setOnClickListener(view -> finish());
    }

    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {
        // Request current game shots upon connection
        try {
            JSONObject requestGameShotsMessage = new JSONObject();
            requestGameShotsMessage.put("type", "requestGameShots");
            WebSocketManager.getInstance().sendMessage(requestGameShotsMessage.toString());
            JSONObject requestTeamStatsMessage = new JSONObject();

            requestTeamStatsMessage.put("type", "requestTeamStats");
            WebSocketManager.getInstance().sendMessage(requestTeamStatsMessage.toString());
        } catch (JSONException e) {
            Log.e("GameWebsocketActivity", "Error sending game shots request: " + e.getMessage());
        }
    }

    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {
        Log.i("GameWebsocketActivity", "WebSocket Closed");
    }

    @Override
    public void onWebSocketMessage(String message) {
        runOnUiThread(() -> processMessage(message));
    }

    @Override
    public void onWebSocketError(Exception ex) {
        Log.e("GameActivity", "WebSocket Error: " + ex.getMessage());
    }

    private void processMessage(String message) {
        try {
            // Check if the message contains the JSON part
            if (message.contains("{")) {
                // Extract JSON part from the message
                String jsonPart = message.substring(message.indexOf("{"));
                JSONObject messageObject = new JSONObject(jsonPart);
                String type = messageObject.getString("type");

                switch (type) {
                    case "gameShots":
                        updateUIWithGameShots(messageObject);
                        break;
                    case "teamStats":
                        handleTeamStatsMessage(messageObject);
                        break;
                    case "shot":
                        handleShotMessage(messageObject);
                        break;
                    case "statUpdate":
                        handleStatUpdateMessage(messageObject);
                        break;
                    case "playerStatsUpdate":
                        handlePlayerStatsUpdateMessage(messageObject);
                        break;
                }
            }
        } catch (JSONException e) {
            Log.e("GameWebsocketActivity", "Error parsing message: " + e.getMessage());
        }
    }

    private void updateUIWithGameShots(JSONObject gameShots) {
        try {
            JSONArray shotsArray = gameShots.getJSONArray("shots");
            for (int i = 0; i < shotsArray.length(); i++) {
                JSONObject shotObject = shotsArray.getJSONObject(i);
                boolean made = shotObject.getBoolean("made");
                float xCoord = (float) shotObject.getDouble("xCoord");
                float yCoord = (float) shotObject.getDouble("yCoord");

                // Choose the color based on whether the shot was made or missed
                Drawable drawable = made ? green : red;

                // Place the icon on the UI
                setIconAndPosition(drawable, xCoord, yCoord);
            }
        } catch (JSONException e) {
            Log.e("GameActivity", "Error parsing game shots: " + e.getMessage());
        }
    }

    private void handleTeamStatsMessage(JSONObject messageObject) {
        try {
            // Extract team statistics from the message
            int teamPoints = messageObject.getInt("teamPoints");
            String teamFGRatio = messageObject.getString("teamFGRatio");
            String teamThreePointRatio = messageObject.getString("teamThreePointRatio");
            int teamAssists = messageObject.getInt("teamAssists");
            int teamRebounds = messageObject.getInt("teamRebounds");
            int teamSteals = messageObject.getInt("teamSteals");
            int teamBlocks = messageObject.getInt("teamBlocks");

            // Update UI with the extracted statistics
            binding.tvTeamPoints.setText(String.valueOf(teamPoints));
            binding.tvTeamFG.setText(teamFGRatio);
            binding.tvTeam3PT.setText(teamThreePointRatio);
            binding.tvTeamAssists.setText(String.valueOf(teamAssists));
            binding.tvTeamRebounds.setText(String.valueOf(teamRebounds));
            binding.tvTeamSteals.setText(String.valueOf(teamSteals));
            binding.tvTeamBlocks.setText(String.valueOf(teamBlocks));
        } catch (JSONException e) {
            Log.e("GameWebsocketActivity", "Error parsing team stats message: " + e.getMessage());
        }
    }

    private void handleShotMessage(JSONObject messageObject) {
        try {
            // Extract shot details
            String playerName = messageObject.getString("playerName");
            boolean made = messageObject.getBoolean("made");
            String shotType = messageObject.getString("shotType");
            JSONObject coordinates = messageObject.getJSONObject("coordinates");
            float xCoord = (float) coordinates.getDouble("x");
            float yCoord = (float) coordinates.getDouble("y");

            // Update UI based on this information
            // Update the latest shot message
            String shotDetail = playerName + (made ? " made " : " missed ") + "a " + shotType;
            binding.websocketMessages.setText(shotDetail);

            // Display the shot icon on the court
            Drawable drawable = made ? green : red;
            setIconAndPosition(drawable, xCoord, yCoord);
        } catch (JSONException e) {
            Log.e("GameWebsocketActivity", "Error parsing shot message: " + e.getMessage());

        }
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

    private void handleStatUpdateMessage(JSONObject messageObject) {
        try {
            String playerName = messageObject.getString("playerName");
            String stat = messageObject.getString("stat");
            int newValue = messageObject.getInt("newValue");

            // Update UI based on this information
            String statDetail = playerName + " now has " + newValue + " " + stat;
            binding.websocketMessages.setText(statDetail);
        } catch (JSONException e) {
            Log.e("GameWebsocketActivity", "Error parsing stats message: " + e.getMessage());
        }
    }

    private void handlePlayerStatsUpdateMessage(JSONObject messageObject) {
        try {
            String playerName = messageObject.getString("playerName");
            int points = messageObject.getInt("points");
            String fgRatio = messageObject.getString("fgRatio");
            String threePointRatio = messageObject.getString("threePointRatio");
            int assists = messageObject.getInt("assists");
            int rebounds = messageObject.getInt("rebounds");
            int steals = messageObject.getInt("steals");
            int blocks = messageObject.getInt("blocks");

            // Update the UI with these stats
            updatePlayerStats(playerName, points, fgRatio, threePointRatio, assists, rebounds, steals, blocks);
        } catch (JSONException e) {
            Log.e("GameWebsocketActivity", "Error handling player stats update message: " + e.getMessage());
        }
    }

    private void updatePlayerStats(String playerName, int points, String fgRatio, String threePointRatio, int assists, int rebounds, int steals, int blocks) {
        LinearLayout container = binding.playerStatsContainer;
        View playerStatView = container.findViewWithTag(playerName);
        if (playerStatView == null) {
            // Create new stat card for this player
            CardView statCard = createPlayerStatCard(playerName, points, fgRatio, threePointRatio, assists, rebounds, steals, blocks);
            container.addView(statCard);
        } else {
            // Update existing card
            updatePlayerStatCard(playerStatView, points, fgRatio, threePointRatio, assists, rebounds, steals, blocks);
        }
    }

    private CardView createPlayerStatCard(String playerName, int points, String fgRatio, String threePointRatio, int assists, int rebounds, int steals, int blocks) {
        // Initialize a new CardView
        CardView cardView = new CardView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(8, 8, 8, 8);
        cardView.setLayoutParams(layoutParams);

        // Create a vertical LinearLayout to hold the stats
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        cardView.addView(linearLayout);

        // Add TextViews for each stat
        TextView playerNameView = new TextView(this);
        playerNameView.setText(playerName);
        linearLayout.addView(playerNameView);

        TextView pointsView = new TextView(this);
        pointsView.setText("Points: " + points);
        linearLayout.addView(pointsView);

        TextView fgRatioView = new TextView(this);
        pointsView.setText("FG: " + fgRatio);
        linearLayout.addView(fgRatioView);

        TextView threePointView = new TextView(this);
        pointsView.setText("3PT: " + threePointRatio);
        linearLayout.addView(threePointView);

        TextView assistsView = new TextView(this);
        pointsView.setText("AST: " + assists);
        linearLayout.addView(assistsView);

        TextView reboundsView = new TextView(this);
        pointsView.setText("REB: " + rebounds);
        linearLayout.addView(reboundsView);

        TextView stealsView = new TextView(this);
        pointsView.setText("STL: " + steals);
        linearLayout.addView(stealsView);

        TextView blocksView = new TextView(this);
        pointsView.setText("BLK: " + blocks);
        linearLayout.addView(blocksView);

        // Set the tag for future reference
        cardView.setTag(playerName);

        return cardView;
    }

    private void updatePlayerStatCard(View statCard, int points, String fgRatio, String threePointRatio, int assists, int rebounds, int steals, int blocks) {
        if (statCard instanceof CardView) {
            LinearLayout linearLayout = (LinearLayout) ((CardView) statCard).getChildAt(0);
            // Update each TextView with new stats
            // TextViews added in the same order as created
            ((TextView) linearLayout.getChildAt(1)).setText("Points: " + points);
            ((TextView) linearLayout.getChildAt(2)).setText("FG: " + fgRatio);
            ((TextView) linearLayout.getChildAt(3)).setText("3PT: " + threePointRatio);
            ((TextView) linearLayout.getChildAt(4)).setText("AST: " + assists);
            ((TextView) linearLayout.getChildAt(5)).setText("REB: " + rebounds);
            ((TextView) linearLayout.getChildAt(6)).setText("STL: " + steals);
            ((TextView) linearLayout.getChildAt(7)).setText("BLK: " + blocks);
        }
    }

}