package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import org.java_websocket.handshake.ServerHandshake;

/**
 * @author Adam Hisel
 * Activity that incorporates a team chat that can be accessed in the team roster
 * activity. Uses a websocket to talk to the server.
 */

public class TeamChat extends AppCompatActivity implements WebSocketListener{

    private String BASE_URL = "ws://10.0.2.2:8080/chat/";

    private Button sendBtn;
    private EditText msgEtx;
    private TextView msgTv;

    private Button exitBtn;


    private ScrollView scrollViewChat;

    private String teamName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_chat);

        scrollViewChat = (ScrollView) findViewById(R.id.scrollView);
        sendBtn = (Button) findViewById(R.id.send);
        exitBtn = (Button) findViewById(R.id.exit);
        msgEtx = (EditText) findViewById(R.id.et2);
        msgTv = (TextView) findViewById(R.id.txt1);
        TextView header = (TextView) findViewById(R.id.header);

        Intent intent = getIntent();
        if (intent != null) {
            teamName =  intent.getStringExtra("teamName");
        }

        header.setText(teamName.toUpperCase() + " Team Chat");



        String serverUrl = BASE_URL + SharedPrefsUtil.getUserName(this);



        WebSocketManager.getInstance().connectWebSocket(serverUrl);
        WebSocketManager.getInstance().setWebSocketListener(TeamChat.this);

        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TeamChat.this, TeamRosterCoach.class);
                startActivity(intent);
            }
        });

        sendBtn.setOnClickListener(v -> {
            try {
                WebSocketManager.getInstance().sendMessage(msgEtx.getText().toString());
            } catch (Exception e) {
                Log.d("ExceptionSendMessage:", e.getMessage().toString());
            }
        });
    }


    @Override
    public void onWebSocketMessage(String message) {

        runOnUiThread(() -> {
            String s = msgTv.getText().toString();
            msgTv.setText(s + "\n"+message);
            scrollViewChat.fullScroll(ScrollView.FOCUS_DOWN);
        });
    }

    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {
        String closedBy = remote ? "server" : "local";
        runOnUiThread(() -> {
            String s = msgTv.getText().toString();
            msgTv.setText(s + "---\nconnection closed by " + closedBy + "\nreason: " + reason);
        });
    }

    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {}

    @Override
    public void onWebSocketError(Exception ex) {}
}