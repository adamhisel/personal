package com.example.project;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.project.R;
import com.example.project.TeamRosterCoach;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Adam Hisel
 * Activity that is opened when a coach clicks on a team they coach.
 * Displays a roster with the players, coaches and managers currently on
 * the team. Gives the coach special options such as the ability
 * to remove players, edit positions and change team settings.
 *
 */
public class TeamRosterFragment extends Fragment {

    private TableLayout tl;
    private RequestQueue mQueue;

    private TextView coachText;
    private boolean exists = false;

    private int teamId;

    private String teamName;


    public TeamRosterFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_team_roster, container, false);

        mQueue = Volley.newRequestQueue(getContext());
        tl =  view.findViewById(R.id.tableLayout);
        coachText = view.findViewById(R.id.coach);
        Button back = view.findViewById(R.id.backButton);
        Button teamChat = view.findViewById(R.id.chatButton);


        teamId= Integer.parseInt(SharedPrefsUtil.getTeamId(getContext()));
        teamName= SharedPrefsUtil.getTeamName(getContext());

        makeHeader();
        addPlayerDisplay();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // You can replace getActivity() with the appropriate activity if needed
                if (getActivity() != null) {
                    getActivity().onBackPressed();
                }
            }
        });

        teamChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), TeamChat.class);
                startActivity(intent);
            }
        });

        return view;
    }

    /**
     * This method is called by findTeam() and is used to input players from the team data into a
     * table that shows up on the screen. Calls a JsonObjectRequest and recieves
     * a response with a list of players
     */
    public void addPlayerDisplay() {

        String url = "http://coms-309-018.class.las.iastate.edu:8080/teams/" + teamId;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONArray coaches = response.getJSONArray("coaches");
                    String text = "Coached by:";
                    for(int j = 0; j < coaches.length(); j++){
                        JSONObject coach = coaches.getJSONObject(j);
                        text +=  " " + coach.getString("name");
                    }
                    coachText.setText(text);

                    JSONArray players = response.getJSONArray("players");

                    if (players.length() > 0) {

                        for (int i= 0; i < players.length(); i++) {
                            JSONObject player = players.getJSONObject(i);
                            int playerId = player.getInt("id");
                            String name = player.getString("playerName");
                            String number = "#" + player.getString("number");
                            String position = player.getString("position");


                            Log.d("TeamRoster", "Info" + name);


                            android.widget.TableRow.LayoutParams trparams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);

                            TableRow tableRow = new TableRow(requireContext());

                            Resources resources = getResources();
                            Drawable drawable = resources.getDrawable(R.drawable.textbox_borders);

                            TextView textView = new TextView(requireContext());
                            textView.setPadding(10, 10, 10, 10);
                            textView.setLayoutParams(trparams);
                            textView.setTextSize(25);
                            textView.setBackground(drawable);
                            textView.setText(number);
                            tableRow.addView(textView);


                            TextView textView2 = new TextView(requireContext());
                            textView2.setPadding(10, 10, 10, 10);
                            textView2.setLayoutParams(trparams);
                            textView2.setTextSize(25);
                            textView2.setBackground(drawable);
                            textView2.setText(name);
                            tableRow.addView(textView2);

                            TextView textView3 = new TextView(requireContext());
                            textView3.setPadding(10, 10, 10, 10);
                            textView3.setLayoutParams(trparams);
                            textView3.setTextSize(25);
                            textView3.setBackground(drawable);
                            textView3.setText(position);
                            tableRow.addView(textView3);


                            tl.addView(tableRow);
                        }
                    }

                } catch (JSONException e) {

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

    public void makeHeader(){
        android.widget.TableRow.LayoutParams trparams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);

        TableRow tableRow = new TableRow(requireContext());

        Resources resources = getResources();
        Drawable drawable = resources.getDrawable(R.drawable.textbox_borders);

        TextView textView = new TextView(requireContext());
        textView.setPadding(10, 10, 10, 10);
        textView.setLayoutParams(trparams);
        textView.setTextSize(25);
        textView.setTypeface(null, android.graphics.Typeface.BOLD);
        textView.setBackground(drawable);
        textView.setText("Number");
        tableRow.addView(textView);


        TextView textView2 = new TextView(requireContext());
        textView2.setPadding(10, 10, 10, 10);
        textView2.setLayoutParams(trparams);
        textView2.setTextSize(25);
        textView2.setTypeface(null, android.graphics.Typeface.BOLD);
        textView2.setBackground(drawable);
        textView2.setText("Name");
        tableRow.addView(textView2);

        TextView textView3 = new TextView(requireContext());
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
