package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

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

public class TeamRoster extends AppCompatActivity {

    TableLayout tl;
    private RequestQueue mQueue;

    private TextInputLayout teamName;

    boolean exists = false;

    private int teamId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_roster);

        /*Bundle args = getArguments();
        if (args != null) {
            teamName = args.getString("teamName");
        }*/

        tl =  findViewById(R.id.tableLayout);

        Button addPlayer = findViewById(R.id.addPlayer);
        Button findTeam = findViewById(R.id.findTeam);
        Button back = findViewById(R.id.backButton);

        teamName = findViewById(R.id.teamname);

        makeHeader();

        mQueue = Volley.newRequestQueue(this);

        addPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TeamRoster.this, EditRosterActivity.class);
                intent.putExtra("id", String.valueOf(teamId));
                intent.putExtra("key_string", teamName.getEditText().getText().toString());
                startActivity(intent);
            }
        });
        findTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isValidTeamName = validateTeamName();

                if (!isValidTeamName) {
                    return;
                }

                findTeam();

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TeamRoster.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void findTeam() {
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

    public void addPlayerDisplay(int id) {

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


                            android.widget.TableRow.LayoutParams trparams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);

                            TableRow tableRow = new TableRow(TeamRoster.this);

                            Resources resources = getResources();
                            Drawable drawable = resources.getDrawable(R.drawable.textbox_borders);

                            TextView textView = new TextView(TeamRoster.this);
                            textView.setPadding(10, 10, 10, 10);
                            textView.setLayoutParams(trparams);
                            textView.setTextSize(25);
                            textView.setBackground(drawable);
                            textView.setText(number);
                            tableRow.addView(textView);


                            TextView textView2 = new TextView(TeamRoster.this);
                            textView2.setPadding(10, 10, 10, 10);
                            textView2.setLayoutParams(trparams);
                            textView2.setTextSize(25);
                            textView2.setBackground(drawable);
                            textView2.setText(name);
                            tableRow.addView(textView2);

                            TextView textView3 = new TextView(TeamRoster.this);
                            textView3.setPadding(10, 10, 10, 10);
                            textView3.setLayoutParams(trparams);
                            textView3.setTextSize(25);
                            textView3.setBackground(drawable);
                            textView3.setText(position);
                            tableRow.addView(textView3);


                            tl.addView(tableRow);
                        }

                } catch (JSONException e) {

                    android.widget.TableRow.LayoutParams trparams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);

                    TableRow tableRow = new TableRow(TeamRoster.this);

                    Resources resources = getResources();
                    Drawable drawable = resources.getDrawable(R.drawable.textbox_borders);

                    TextView textView = new TextView(TeamRoster.this);
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

    public void makeHeader(){
        android.widget.TableRow.LayoutParams trparams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);

        TableRow tableRow = new TableRow(TeamRoster.this);

        Resources resources = getResources();
        Drawable drawable = resources.getDrawable(R.drawable.textbox_borders);

        TextView textView = new TextView(TeamRoster.this);
        textView.setPadding(10, 10, 10, 10);
        textView.setLayoutParams(trparams);
        textView.setTextSize(25);
        textView.setTypeface(null, android.graphics.Typeface.BOLD);
        textView.setBackground(drawable);
        textView.setText("Number");
        tableRow.addView(textView);


        TextView textView2 = new TextView(TeamRoster.this);
        textView2.setPadding(10, 10, 10, 10);
        textView2.setLayoutParams(trparams);
        textView2.setTextSize(25);
        textView2.setTypeface(null, android.graphics.Typeface.BOLD);
        textView2.setBackground(drawable);
        textView2.setText("Name");
        tableRow.addView(textView2);

        TextView textView3 = new TextView(TeamRoster.this);
        textView3.setPadding(10, 10, 10, 10);
        textView3.setLayoutParams(trparams);
        textView3.setTextSize(25);
        textView3.setTypeface(null, android.graphics.Typeface.BOLD);
        textView3.setBackground(drawable);
        textView3.setText("Position");
        tableRow.addView(textView3);

        tl.addView(tableRow);
    }

    private Boolean validateTeamName() {
        String tilTeamName = teamName.getEditText().getText().toString().trim();

        if (tilTeamName.isEmpty()) {
            teamName.setError("Field cannot be empty");
            return false;
        } else {
            teamName.setError(null);
            teamName.setErrorEnabled(false);
            return true;
        }
    }

}