package com.example.project;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/**
 * Activity that is opened when a player or fan clicks on a team they play on/follow.
 * Displays a roster with the players, coaches and managers currently on
 * the team.
 *
 * @author Adam Hisel
 */
public class TeamRosterPlayer extends AppCompatActivity {

    private TableLayout tl;
    private RequestQueue mQueue;

    private TextInputLayout teamName;

    private boolean exists = false;

    private int teamId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_roster_player);


        tl =  findViewById(R.id.tableLayout);

        Button teamChat = findViewById(R.id.chatButton);
        Button findTeam = findViewById(R.id.findTeam);
        Button back = findViewById(R.id.backButton);

        teamName = findViewById(R.id.teamname);

        makeHeader();

        mQueue = Volley.newRequestQueue(this);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TeamRosterPlayer.this, MainActivity.class);
                startActivity(intent);
            }
        });

        teamChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TeamRosterPlayer.this, TeamChat.class);
                startActivity(intent);
            }
        });

    }

    /**
     * This method finds the players on the respective team and calls addPlayerDisplay()
     * to put them in the table to be generated on screen
     */
    private void findTeam() {
        String url = "http://coms-309-018.class.las.iastate.edu:8080/teams";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject team = response.getJSONObject(i);
                        String name = team.getString("teamName");
                        if(name.equals(teamName.getEditText().getText().toString())){
                            teamId = team.getInt("id");
                            exists = true;
                            addPlayerDisplay(teamId);
                        }

                    }
                    if(exists == false){
                        throw new CustomException();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (CustomException e) {
                    teamName.setError("No team exists with this team name");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mQueue.add(request);
    }

    class CustomException extends Exception {
        public CustomException() {

        }
    }
    /**
     * This method is called by findTeam() and is used to input players from the team data into a
     * table that shows up on the screen. Calls a JsonObjectRequest and recieves
     * a response with a list of players
     * @param id
     */
    private void addPlayerDisplay(int id) {

        String url = "http://coms-309-018.class.las.iastate.edu:8080/teams/" + id;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {


                    JSONObject players = response.getJSONObject("player"); // Assuming players is an array

                    if (players.length() > 0) {


                            int playerId = players.getInt("id");
                            String name = players.getString("playerName");
                            String number = "#" + players.getString("number");
                            String position = players.getString("position");


                            Log.d("TeamRoster", "Info" + name);


                            TableRow.LayoutParams trparams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);

                            TableRow tableRow = new TableRow(TeamRosterPlayer.this);

                            Resources resources = getResources();
                            Drawable drawable = resources.getDrawable(R.drawable.textbox_borders);

                            TextView textView = new TextView(TeamRosterPlayer.this);
                            textView.setPadding(10, 10, 10, 10);
                            textView.setLayoutParams(trparams);
                            textView.setTextSize(25);
                            textView.setBackground(drawable);
                            textView.setText(number);
                            tableRow.addView(textView);


                            TextView textView2 = new TextView(TeamRosterPlayer.this);
                            textView2.setPadding(10, 10, 10, 10);
                            textView2.setLayoutParams(trparams);
                            textView2.setTextSize(25);
                            textView2.setBackground(drawable);
                            textView2.setText(name);
                            tableRow.addView(textView2);

                            TextView textView3 = new TextView(TeamRosterPlayer.this);
                            textView3.setPadding(10, 10, 10, 10);
                            textView3.setLayoutParams(trparams);
                            textView3.setTextSize(25);
                            textView3.setBackground(drawable);
                            textView3.setText(position);
                            tableRow.addView(textView3);


                            tl.addView(tableRow);
                        }

                } catch (JSONException e) {

                    TableRow.LayoutParams trparams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);

                    TableRow tableRow = new TableRow(TeamRosterPlayer.this);

                    Resources resources = getResources();
                    Drawable drawable = resources.getDrawable(R.drawable.textbox_borders);

                    TextView textView = new TextView(TeamRosterPlayer.this);
                    textView.setPadding(10, 10, 10, 10);
                    textView.setLayoutParams(trparams);
                    textView.setTextSize(25);
                    textView.setTypeface(null, android.graphics.Typeface.BOLD);
                    textView.setBackground(drawable);
                    textView.setText("No Players Exist In This Team");
                    tableRow.addView(textView);

                    tl.addView(tableRow);


                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mQueue.add(request);
    }

    /**
     * This method makes a table header on screen with Number, Name and Position
     */
    private void makeHeader(){
        TableRow.LayoutParams trparams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);

        TableRow tableRow = new TableRow(TeamRosterPlayer.this);

        Resources resources = getResources();
        Drawable drawable = resources.getDrawable(R.drawable.textbox_borders);

        TextView textView = new TextView(TeamRosterPlayer.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setLayoutParams(trparams);
        textView.setTextSize(25);
        textView.setTypeface(null, android.graphics.Typeface.BOLD);
        textView.setBackground(drawable);
        textView.setText("Number");
        tableRow.addView(textView);


        TextView textView2 = new TextView(TeamRosterPlayer.this);
        textView2.setPadding(10, 10, 10, 10);
        textView2.setLayoutParams(trparams);
        textView2.setTextSize(25);
        textView2.setTypeface(null, android.graphics.Typeface.BOLD);
        textView2.setBackground(drawable);
        textView2.setText("Name");
        tableRow.addView(textView2);

        TextView textView3 = new TextView(TeamRosterPlayer.this);
        textView3.setPadding(10, 10, 10, 10);
        textView3.setLayoutParams(trparams);
        textView3.setTextSize(25);
        textView3.setTypeface(null, android.graphics.Typeface.BOLD);
        textView3.setBackground(drawable);
        textView3.setText("Position");
        tableRow.addView(textView3);

        tl.addView(tableRow);
    }

}