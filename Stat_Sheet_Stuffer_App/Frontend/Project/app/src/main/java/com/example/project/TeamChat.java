package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
 * Activity that incorporates a team chat that can be accessed in the team roster
 * activity. Uses a websocket to talk to the server.
 *
 * @author Adam Hisel
 */

public class TeamChat extends AppCompatActivity implements WebSocketListener{
    private String BASE_URL = "ws://coms-309-018.class.las.iastate.edu:8080/chat/";
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
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.black));
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

        header.setText(SharedPrefsTeamUtil.getTeamName(this) + " Team Chat");

        String userName = SharedPrefsUtil.getUserName(this);

        String serverUrl = BASE_URL + userName;



        WebSocketManager.getInstance().connectWebSocket(serverUrl);
        WebSocketManager.getInstance().setWebSocketListener(TeamChat.this);

        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TeamRosterFragment teamRosterFragment = new TeamRosterFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout, teamRosterFragment);
                fragmentTransaction.commit();
                finish();
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