package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ScrollView;

import org.java_websocket.handshake.ServerHandshake;

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
    private ActivityGameWebsocketBinding binding;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGameWebsocketBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userName = SharedPrefsUtil.getUserName(this);
        String url = BASE_URL + userName;
        String testUrl = LOCAL_URL + userName;

        initializeWebSocketConnection(url);
        setupButtonListeners();
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
        Log.i("GameWebsocketActivity", "WebSocket Opened");
    }

    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {
        Log.i("GameWebsocketActivity", "WebSocket Closed");
    }

    @Override
    public void onWebSocketMessage(String message) {
        // Handle incoming messages from WebSocket
        Log.i("GameWebsocketActivity", "WebSocket Message: " + message);

        runOnUiThread(() -> {
            // Append the new message
            binding.websocketMessages.append(message + "\n");

            // Scroll to the bottom to show the latest message
            binding.websocketScroll.fullScroll(ScrollView.FOCUS_DOWN);
        });
    }

    @Override
    public void onWebSocketError(Exception ex) {
        Log.e("GameActivity", "WebSocket Error: " + ex.getMessage());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WebSocketManager.getInstance().disconnectWebSocket();
        WebSocketManager.getInstance().removeWebSocketListener();
    }
}