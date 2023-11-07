package com.example.project;


import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.project.databinding.ActivityGameBinding;


import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.List;


public class GameActivity extends AppCompatActivity implements WebSocketListener {


    private static final String TAG = "GameActivity";
    private static final String BASE_URL = "http://coms-309-018.class.las.iastate.edu:8080/";
    private static final String LOCAL_URL = "http://10.0.2.2:8080/";


    private static final int ICON_SIZE_PX = (int) (20 * Resources.getSystem().getDisplayMetrics().density);
    private static RequestQueue mQueue;
    private final int[] playerButtonIds = new int[]{
            R.id.imgBtnPlayer1, R.id.imgBtnPlayer2, R.id.imgBtnPlayer3, R.id.imgBtnPlayer4, R.id.imgBtnPlayer5
    };
    private final int[] playerTextViewIds = new int[]{
            R.id.tvBtnPlayer1, R.id.tvBtnPlayer2, R.id.tvBtnPlayer3, R.id.tvBtnPlayer4, R.id.tvBtnPlayer5
    };
    private final List<Shots> teamShots = new ArrayList<>();
    private ActivityGameBinding binding;
    private ImageView imageView;
    private Drawable green, red;
    private int totalShots = 0;
    private int threePointMakes = 0;
    private int threePointAttempts = 0;
    private int twoPointMakes = 0;
    private int twoPointAttempts = 0;
    private int teamPoints = 0;
    private final List<Player> players = new ArrayList<>();


    private Player activePlayer;
    private int activePlayerIndex;


    private int gameId;


    private String teamId;


    private String userName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.black));


        mQueue = Volley.newRequestQueue(this);
        teamId = SharedPrefsUtil.getTeamId(this);
        userName = SharedPrefsUtil.getUserName(this);


        Log.d("GameActivity", "Team ID retrieved: " + teamId);
        loadPlayersForTeam(Integer.parseInt(teamId));
        initializeViews();
        setupCourtImageView();
        setupShotTypeIndicator();
        createGame(new TeamIdCallback() {
            @Override
            public void onTeamIdReceived(int id) {
                gameId = id;
                addGameToTeam(Integer.parseInt(teamId), gameId);
            }
        });


        Log.d("GameActivity", "User name retrieved: " + userName);
        String url = "ws://" + "coms-309-018.class.las.iastate.edu:8080/game/" + userName;
        String testUrl = "ws://" + "10.0.2.2:8080/game/" + userName;


        WebSocketManager.getInstance().setWebSocketListener(this);
        WebSocketManager.getInstance().connectWebSocket(testUrl);
    }


    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {
        // WebSocket connection is open and ready to use
        Log.i("GameActivity", "WebSocket Opened");
    }


    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {
        // Handle the closing of the WebSocket
        Log.i("GameActivity", "WebSocket Closed");
    }


    @Override
    public void onWebSocketMessage(String message) {
        // Handle incoming messages from WebSocket
        Log.i("GameActivity", "WebSocket Message: " + message);


        runOnUiThread(() -> {
            // Append the new message
            binding.tvWebsocketMessages.append(message + "\n");


            // Scroll to the bottom to show the latest message
            binding.svWebsocket.fullScroll(ScrollView.FOCUS_DOWN);
        });
    }


    @Override
    public void onWebSocketError(Exception ex) {
        // Handle WebSocket errors
        Log.e("GameActivity", "WebSocket Error: " + ex.getMessage());
    }


    private void initializeViews() {
        imageView = binding.courtImageView;
        green = ContextCompat.getDrawable(this, R.drawable.outline_circle_10);
        red = ContextCompat.getDrawable(this, R.drawable.outline_cancel_10);


        hideShotButtons();


        // Set up player button listeners
        for (int i = 0; i < playerButtonIds.length; i++) {
            ImageButton playerButton = findViewById(playerButtonIds[i]);
            final int index = i;
            playerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setActivePlayer(index);
                }
            });
        }


        binding.btnEndSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //sendTeamShots(gameId, teamShots);
                for (Player player : players) {
                    sendPlayerShots(gameId, player.getId(), player.getShots());
                }
                finish();
            }
        });
    }


    private void setupCourtImageView() {
        binding.courtImageView.post(() -> {
            int width = binding.courtImageView.getWidth();
            float aspectRatio = 564f / 600f; // real court's height / width
            ViewGroup.LayoutParams params = binding.courtImageView.getLayoutParams();
            int imageHeight = (int) (width * aspectRatio);
            params.height = imageHeight;
            binding.courtImageView.setLayoutParams(params);


            // Set the top margin of the buttons layout
            int buttonsTopMargin = imageHeight;
            setViewTopMargin(binding.llButtons, buttonsTopMargin + 10);


            int playersTopMargin = buttonsTopMargin + binding.llButtons.getHeight() + 50; // 50 is the space between buttons and players
            setViewTopMargin(binding.llPlayers, playersTopMargin);


            int scrollViewTopMargin = playersTopMargin + binding.llPlayers.getHeight() + 10; // for example
            setViewTopMargin(binding.svWebsocket, scrollViewTopMargin);
        });
    }


    private void setViewTopMargin(View view, int topMargin) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        layoutParams.topMargin = topMargin;
        view.setLayoutParams(layoutParams);
    }


    private void createGame(final TeamIdCallback callback) {
        String url = BASE_URL + "games/" + teamId; // Include the teamId in the URL
        String testUrl = LOCAL_URL + "games/" + teamId; // Use this for testing with local server


        Log.d(TAG, "Creating new game for team: " + teamId);


        // Create a JsonObjectRequest for a POST request to create a new game
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, testUrl, null,
                response -> {
                    // Handle response
                    gameId = response.optInt("id");
                    Log.d(TAG, "Game created successfully for team " + teamId + " with ID: " + gameId);
                    callback.onTeamIdReceived(gameId);
                }, error -> {
            Log.e(TAG, "Failed to create game. Error: " + error.toString());
            // Handle error
        });


        // Add the request to your request queue
        mQueue.add(request);
    }


    private void addGameToTeam(int teamId, int gameId) {
        String url = BASE_URL + "teams/" + teamId + "/addGame/" + gameId;
        String testUrl = LOCAL_URL + "teams/" + teamId + "/addGame/" + gameId;


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, testUrl, null,
                response -> {
                    // Handle response
                    Log.d(TAG, "Game " + gameId + " added successfully to team " + teamId);
                }, error -> {
            // Handle error
            Log.e(TAG, "Failed to add game to team. Error: " + error.toString());
            Toast.makeText(GameActivity.this, "Failed to add game to team.", Toast.LENGTH_SHORT).show();
        });


        // Add the request to your request queue
        mQueue.add(request);
    }






    private void loadPlayersForTeam(int teamId) {
        String url = BASE_URL + "teams/" + teamId;
        String testUrl = LOCAL_URL + "teams/" + teamId;


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, testUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray playersArray = response.getJSONArray("players");


                    for (int i = 0; i < playersArray.length(); i++) {
                        JSONObject playerObject = playersArray.getJSONObject(i);
                        int playerId = playerObject.getInt("id");
                        String playerName = playerObject.getString("playerName");
                        int playerNumber = playerObject.getInt("number");
                        String position = playerObject.getString("position");
                        Player player = new Player(playerId, playerName, playerNumber, position);
                        players.add(player);
                        Log.i("GameActivity", "Loaded player: " + playerName); // Logging the loaded player
                    }
                    updatePlayerButtonLabels();
                } catch (JSONException e) {
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("GameActivity", "Failed to load team players: " + error.toString());
                Toast.makeText(GameActivity.this, "Failed to load team players.", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });
        mQueue.add(request);
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
        // Check if there is an active player
        if (activePlayer == null) {
            Toast.makeText(this, "Please select a player first!", Toast.LENGTH_SHORT).show();
            return;
        }
        float x = event.getX();
        float y = event.getY();


        String shotType = calculateShotType(x, y);


        // Increment attempt counters
        incrementShotAttempts(shotType);


        // Show shot buttons and then wait for user to record as make or miss
        showShotButtons();
        recordMakeOrMiss(shotType, x, y);
    }


    private String calculateShotType(float x, float y) {
        // Scaling factors
        float widthScale = 600f / imageView.getWidth();
        float heightScale = 564f / imageView.getHeight();


        // Real-world coordinates
        float realX = x * widthScale;
        float realY = y * heightScale;


        // Calculate the distance to the basket
        float distanceToBasket = calculateDistanceToBasket(realX, realY);


        // Determine the shot type
        return (realX <= (300f - 264f) || realX >= (300f + 264f) || distanceToBasket >= 285)
                ? "Three-Point Shot"
                : "Two-Point Shot";
    }


    private float calculateDistanceToBasket(float realX, float realY) {
        return (float) Math.sqrt(Math.pow(realX - 300f, 2) + Math.pow(realY - 65f, 2));
    }


    private void incrementShotAttempts(String shotType) {
        if ("Three-Point Shot".equals(shotType)) {
            threePointAttempts++;
            if (activePlayer != null)
                activePlayer.recordThreePointShot(false);
        } else {
            twoPointAttempts++;
            if (activePlayer != null)
                activePlayer.recordTwoPointShot(false);
        }
    }


    private void recordMakeOrMiss(String shotType, float x, float y) {
        // Set click listeners for make and miss buttons
        binding.btnMake.setOnClickListener(v -> handleShotMade(shotType, x, y));
        binding.btnMiss.setOnClickListener(v -> handleShotMissed(shotType, x, y));
    }


    private void handleShotMade(String shotType, float x, float y) {
        boolean isThreePoint = "Three-Point Shot".equals(shotType);
        int value = isThreePoint ? 3 : 2;
        teamPoints += value;


        Shots shot = new Shots(true, value, (int) x, (int) y);
        teamShots.add(shot); // Add the shot to the team's list
        Log.d("GameActivity", "Shot made: " + shot + " added to team shot list.");


        // Check if there's an active player selected
        if (activePlayer != null) {
            // Add the shot to the active player's list
            activePlayer.addShot(new Shots(true, value, (int) x, (int) y));
            Log.d("GameActivity", "Shot made: " + shot + " added to " + activePlayer.getName() + "'s shot list.");
        }
        int activePlayerPoints = activePlayer.getThreePointMakes() * 3 + activePlayer.getTwoPointMakes() * 2;
        String message = activePlayer.getName() + " made a " + (isThreePoint ? "3" : "2") +
                " point shot, player's points: " + activePlayerPoints +
                ", team's points: " + teamPoints;
        WebSocketManager.getInstance().sendMessage(message);


        setIconAndPosition(green, x + imageView.getLeft(), y + imageView.getTop());
        if (isThreePoint) {
            threePointMakes++;
            if (activePlayer != null)
                activePlayer.recordThreePointShot(true);
        } else {
            twoPointMakes++;
            if (activePlayer != null)
                activePlayer.recordTwoPointShot(true);
        }
        totalShots++;
        hideShotButtons();
    }


    private void handleShotMissed(String shotType, float x, float y) {
        boolean isThreePoint = "Three-Point Shot".equals(shotType);
        int value = isThreePoint ? 3 : 2;


        Shots shot = new Shots(false, value, (int) x, (int) y);
        teamShots.add(shot); // Add the shot to the team's list
        Log.d("GameActivity", "Shot missed: " + shot + " added to team shot list.");


        if (activePlayer != null) {
            // Add the shot to the active player's list
            activePlayer.addShot(new Shots(false, value, (int) x, (int) y));
            Log.d("GameActivity", "Shot missed: " + shot + " added to " + activePlayer.getName() + "'s shot list.");
        }


        int activePlayerPoints = activePlayer.getThreePointMakes() * 3 + activePlayer.getTwoPointMakes() * 2;
        String message = activePlayer.getName() + " missed a " + (isThreePoint ? "3" : "2") +
                " point shot, player's points: " + activePlayerPoints +
                ", team's points: " + teamPoints;
        WebSocketManager.getInstance().sendMessage(message);
        setIconAndPosition(red, x + imageView.getLeft(), y + imageView.getTop());
        totalShots++;
        hideShotButtons();
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


    private void setActivePlayer(int playerIndex) {
        Log.d(TAG, "setActivePlayer called with playerIndex: " + playerIndex);


        activePlayerIndex = playerIndex;
        activePlayer = players.get(playerIndex);


        // Reset background color for all player buttons
        for (int i = 0; i < playerButtonIds.length; i++) {
            ImageButton button = findViewById(playerButtonIds[i]);
            button.setBackgroundColor(Color.TRANSPARENT); // Or any default color
            Log.d(TAG, "Resetting background color for button at index: " + i);
        }


        // Set background color for active player's button
        ImageButton activePlayerButton = findViewById(playerButtonIds[activePlayerIndex]);
        activePlayerButton.setBackgroundColor(Color.DKGRAY); // Dark color for active player
        Log.d(TAG, "Setting dark gray background for active player button at index: " + activePlayerIndex);
    }




    private void updatePlayerButtonLabels() {
        int playerCount = players.size();
        // Limit the button count to 5
        int buttonCount = Math.min(playerButtonIds.length, 5);
        for (int i = 0; i < buttonCount; i++) {
            ImageButton playerButton = findViewById(playerButtonIds[i]);
            TextView playerTextView = findViewById(playerTextViewIds[i]);


            if (i < playerCount) {
                // There is a player for this button
                Player player = players.get(i);
                playerTextView.setText(player.getName());
                enablePlayerButton(playerButton);
            } else {
                // No player for this button
                playerTextView.setText("No Player");
                disablePlayerButton(playerButton);
            }
        }
    }




    private void sendTeamShots(int gameId, List<Shots> teamShots) {
        String url = BASE_URL + "games/" + gameId + "/team-shots";
        String testUrl = LOCAL_URL + "games/" + gameId + "/team-shots";


        JSONArray shotsArray = new JSONArray();
        for (Shots shot : teamShots) {
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


        Log.d(TAG, "Sending team shots: " + shotsArray);


        JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST, testUrl, shotsArray,
                response -> {
                    Log.d(TAG, "Team shots sent successfully");
                },
                error -> {
                    Log.e(TAG, "Failed to send team shots. Error: " + error.toString());
                });


        mQueue.add(request);
    }


    private void sendPlayerShots(int gameId, int playerId, List<Shots> playerShots) {
        String url = BASE_URL + "games/" + gameId + "/players/" + playerId + "/shots";
        String testUrl = LOCAL_URL + "games/" + gameId + "/players/" + playerId + "/shots";


        JSONArray shotsArray = new JSONArray();
        for (Shots shot : playerShots) {
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


        Log.d(TAG, "Sending player shots for player " + playerId + ": " + shotsArray);


        JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST, testUrl, shotsArray,
                response -> {
                    Log.d(TAG, "Player shots sent successfully");
                },
                error -> {
                    Log.e(TAG, "Failed to send player shots. Error: " + error.toString());
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


    private void enablePlayerButton(ImageButton button) {
        button.setEnabled(true);
        // Set button to opaque
        button.setAlpha(1.0f);
    }


    private void disablePlayerButton(ImageButton button) {
        button.setEnabled(false);
        // Set button to semi-transparent
        button.setAlpha(0.5f);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Disconnect WebSocket when the activity is being destroyed
        WebSocketManager.getInstance().disconnectWebSocket();
        WebSocketManager.getInstance().removeWebSocketListener();
    }


    public interface TeamIdCallback {
        void onTeamIdReceived(int id);
    }
}
