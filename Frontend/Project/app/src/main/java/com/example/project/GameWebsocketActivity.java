package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;

import org.java_websocket.handshake.ServerHandshake;

import com.example.project.databinding.ActivityGameWebsocketBinding;

public class GameWebsocketActivity extends AppCompatActivity implements WebSocketListener {

    private ActivityGameWebsocketBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityGameWebsocketBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        WebSocketManager.getInstance().setWebSocketListener(this);
        WebSocketManager.getInstance().connectWebSocket("wss://your-websocket-url"); // Server URL

        binding.btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {
        // WebSocket connection is open and ready to use
        Log.i("GameWebsocketActivity", "WebSocket Opened");
    }

    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {
        // Handle the closing of the WebSocket
        Log.i("GameWebsocketActivity", "WebSocket Closed");
    }

    @Override
    public void onWebSocketMessage(String message) {
        // Handle incoming messages from WebSocket
        Log.i("GameWebsocketActivity", "WebSocket Message: " + message);

        // Update UI based on WebSocket messages by running code on the UI thread
        runOnUiThread(() -> {
            // Append the new message
            binding.websocketMessages.append(message + "\n");

            // Scroll to the bottom to show the latest message
            binding.websocketScroll.fullScroll(ScrollView.FOCUS_DOWN);
        });
    }

    @Override
    public void onWebSocketError(Exception ex) {
        // Handle WebSocket errors
        Log.e("GameActivity", "WebSocket Error: " + ex.getMessage());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Disconnect WebSocket when the activity is being destroyed
        WebSocketManager.getInstance().disconnectWebSocket();
        WebSocketManager.getInstance().removeWebSocketListener();
    }
}