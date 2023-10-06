package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

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
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TeamRoster extends AppCompatActivity {

    TableLayout tl;
    private RequestQueue mQueue;

    private EditText teamName;

    boolean exists = false;

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

        teamName = findViewById(R.id.etTeamname);



        mQueue = Volley.newRequestQueue(this);

        addPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TeamRoster.this, EditRosterActivity.class);
                startActivity(intent);
            }
        });
        findTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeHeader();
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
        String url = "https://5a183357-b941-4d66-b21b-3b4961c7a63e.mock.pstmn.io/teams";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject team = response.getJSONObject(i);
                        String name = team.getString("teamName");
                        if(name.equals(teamName.getText().toString())){
                            exists = true;
                            int id = team.getInt("id");
                            addPlayerDisplay(id);
                        }
                        if(exists == false){
                            throw new CustomException();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (CustomException e) {
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
                    textView.setText("This Team Does Not Exist");
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

    class CustomException extends Exception {
        public CustomException() {

        }
    }


    public void addPlayerDisplay(int id) {

        String url = "https://5a183357-b941-4d66-b21b-3b4961c7a63e.mock.pstmn.io/teams/";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url + id, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {


                    JSONObject team = response.getJSONObject(id - 1);


                        int id = team.getJSONObject("player").getInt("id");
                        String name = team.getJSONObject("player").getString("playerName");
                        String number = "#" + team.getJSONObject("player").getString("number");
                        String position = team.getJSONObject("player").getString("position");

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

                   // e.printStackTrace();


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

}