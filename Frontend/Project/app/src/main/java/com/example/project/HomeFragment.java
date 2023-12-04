package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.project.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Home fragment in the navigation bar that contains options to create a team, join a team
 * or click into the team a user is on or follows.
 *
 * @author Adam Hisel
 */
public class HomeFragment extends Fragment {

    private RequestQueue mQueue;

    private LinearLayout ll;

    private Bundle savedInstance;

    private Map<ToggleButton, LinearLayout> toggleButtonMap = new HashMap<>();

    private int totalPoints = 0;
    private int totalGames = 0;
    private int totalFGM = 0;
    private int totalFGA = 0;
    private int total3PM = 0;
    private int total3PA = 0;
    private int total2PM = 0;
    private int total2PA = 0;

    private ArrayList<Integer> gameIdArr;

    private ArrayList<Player> players;

    private Map<Integer, Integer> playerPointsMap = new HashMap<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        savedInstance = savedInstanceState;
        View view = inflater.inflate(R.layout.fragment_home_coach, container, false);

        mQueue = Volley.newRequestQueue(requireContext());

        ll = view.findViewById(R.id.cardLL);

        ImageButton addTeam = view.findViewById(R.id.plus);

        ImageButton joinTeam = view.findViewById(R.id.join);

        TextView welcome = view.findViewById(R.id.welcome);

        welcome.setText("Hello, " + SharedPrefsUtil.getFirstName(requireContext()));

        displayTeamButtons();
        addTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddTeamActivity.class);
                startActivity(intent);
            }
        });

        joinTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), JoinTeamActivity.class);
                startActivity(intent);
            }
        });

        return view;

    }
    /**
     * This method is called in onCreate and creates the team buttons for the teams a
     * user follows or is on. This method also allows users to click the buttons once
     * they are generated so which then opens into the specific team roster.
     */
    private void displayTeamButtons() {
        String url = "http://coms-309-018.class.las.iastate.edu:8080/users/" + SharedPrefsUtil.getUserId(getContext());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                        JSONArray teams = response.getJSONArray("teams");

                        ArrayList<String> teamList = new ArrayList<>();
                        ArrayList<Integer> teamIds = new ArrayList<>();
                        ArrayList<Integer> coachUserIds = new ArrayList<>();
                        ArrayList<Integer> fanUserIds = new ArrayList<>();

                        for (int i = 0; i < teams.length(); i++) {
                            JSONObject team = teams.getJSONObject(i);
                            teamList.add(team.getString("teamName"));
                            teamIds.add(Integer.valueOf(team.getString("id")));
                            JSONArray coaches = team.getJSONArray("coaches");
                            JSONArray fans = team.getJSONArray("fans");

                            for(int l = 0; l < coaches.length(); l++){
                                JSONObject coach = coaches.getJSONObject(l);
                                coachUserIds.add(coach.getInt("user_id"));
                            }

                            for(int l = 0; l < fans.length(); l++){
                                JSONObject fan = fans.getJSONObject(l);
                                fanUserIds.add(fan.getInt("user_id"));

                            }
                        }

                        for(int j =0; j< teamList.size(); j++) {

                            totalPoints = 0;
                            totalGames = 0;
                            totalFGM = 0;
                            totalFGA = 0;
                            total3PM = 0;
                            total3PA = 0;
                            total2PM = 0;
                            total2PA = 0;

                            CardView cv = new CardView(requireContext());

                            LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            );
                            cardParams.setMargins(8, 8, 8, 8);
                            cv.setLayoutParams(cardParams);
                            cv.setCardElevation(4);

                            LinearLayout linearLayout = new LinearLayout(requireContext());
                            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            ));
                            linearLayout.setOrientation(LinearLayout.HORIZONTAL);


                            String teamName = teamList.get(j);
                            int id = teamIds.get(j);

                            cv.setId(id);

                            LinearLayout.LayoutParams halfLLHor = new LinearLayout.LayoutParams(
                                    0,
                                    LinearLayout.LayoutParams.MATCH_PARENT
                            );

                            LinearLayout.LayoutParams halfLLVert = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    0
                            );

                            halfLLHor.weight = 1;
                            halfLLVert.weight = 1;

                            TextView tv = new TextView(requireContext());

                            tv.setLayoutParams(halfLLHor);
                            tv.setText(teamName);
                            tv.setTextSize(25);



                            LinearLayout buttonLayout = new LinearLayout(requireContext());

                            buttonLayout.setOrientation(LinearLayout.VERTICAL);

                            buttonLayout.setLayoutParams(halfLLHor);



                            Button button = new Button(requireContext());

                            button.setLayoutParams(halfLLVert);
                            button.setText("View Team");
                            button.setTag(id);
                            button.setTextSize(25);

                            ToggleButton tButton = new ToggleButton(requireContext());

                            tButton.setLayoutParams(halfLLVert);
                            tButton.setText("View Recent Games");
                            tButton.setTextOff("View Recent Games");
                            tButton.setTextOn("Hide Recent Games");
                            tButton.setTag(id);
                            tButton.setTextSize(15);



                            buttonLayout.addView(button);
                            buttonLayout.addView(tButton);

                            linearLayout.addView(tv);
                            linearLayout.addView(buttonLayout);

                            LinearLayout gamesLL = new LinearLayout(requireContext());
                            gamesLL.setLayoutParams(new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            ));
                            gamesLL.setOrientation(LinearLayout.HORIZONTAL);
                            gamesLL.setVisibility(View.GONE);

                            toggleButtonMap.put(tButton, gamesLL);

                            LinearLayout cardContentLayout = new LinearLayout(requireContext());
                            cardContentLayout.setLayoutParams(new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            ));
                            cardContentLayout.setOrientation(LinearLayout.VERTICAL);


                            cardContentLayout.addView(linearLayout);
                            cardContentLayout.addView(gamesLL);



                            cv.addView(cardContentLayout);


                            tButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    LinearLayout gamesLL = toggleButtonMap.get(buttonView);
                                    if (isChecked) {
                                        gamesLL.removeAllViews();
                                        getGames(id, gamesLL);
                                    } else {
                                        gamesLL.setVisibility(View.GONE);
                                    }
                                }
                            });
                            button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (savedInstance == null) {
                                        String p = SharedPrefsUtil.getUserId(getContext());
                                        String isCoach = "false";
                                        String isFan = "false";
                                        for(int i = 0; i < coachUserIds.size(); i++){
                                            int coachId = coachUserIds.get(i);
                                            if(SharedPrefsUtil.getUserId(getContext()).equals(String.valueOf(coachId))){
                                                isCoach = "true";
                                                break;
                                            }else{
                                                isCoach = "false";
                                            }

                                        }
                                        for(int j = 0; j < fanUserIds.size(); j++){
                                            int fanId = fanUserIds.get(j);
                                            if(SharedPrefsUtil.getUserId(getContext()).equals(String.valueOf(fanId))){
                                                isFan = "true";
                                                break;
                                            }else{
                                                isFan = "false";
                                            }

                                        }

                                        SharedPrefsTeamUtil.saveTeamData(getContext(), teamName, String.valueOf(id), isCoach, isFan);
                                        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                                        TeamRosterFragment newFragment = new TeamRosterFragment();

                                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                                        transaction.replace(R.id.linear, newFragment);
                                        transaction.addToBackStack(null);
                                        transaction.commit();

                                    }
                                }
                            });

                            ll.addView(cv, ll.getChildCount() - teams.length()-2);

                        }

            } catch (JSONException e) {
                    e.printStackTrace();
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


    private void getGames(int id, LinearLayout gamesLL) {
        String url = "http://coms-309-018.class.las.iastate.edu:8080/games/" + id;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject game = response.getJSONObject(i);
                            int gameId = game.getInt("id");
                            String gameDate = game.getString("date");

                            // Calculate statistics for this game
                            calculateGameStatistics(gameId, gamesLL, gameDate);
                        }

                        // Show the gamesLL layout with all the game statistics
                        gamesLL.setVisibility(View.VISIBLE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> error.printStackTrace()
        );
        mQueue.add(request);
    }

    private void calculateGameStatistics(int gameId, LinearLayout gamesLL, String gameDate) {
        String url = "http://coms-309-018.class.las.iastate.edu:8080/games/" + gameId + "/shots";
        JsonArrayRequest shotsRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    int gameFGM = 0;
                    int gameFGA = 0;
                    int game3PM = 0;
                    int game3PA = 0;

                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject shot = response.getJSONObject(i);
                            boolean made = shot.getBoolean("made");
                            int value = shot.getInt("value");

                            // Update game statistics
                            if (made) {
                                gameFGM++;
                            }
                            gameFGA++;

                            if (value == 3) {
                                if (made) {
                                    game3PM++;
                                }
                                game3PA++;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    // Calculate shooting percentages for this game
                    double gameFGPercentage = gameFGA > 0 ? (gameFGM * 100.0 / gameFGA) : 0.0;
                    double game3PPercentage = game3PA > 0 ? (game3PM * 100.0 / game3PA) : 0.0;

                    // Display game statistics in the layout
                    displayGameStatistics(gamesLL, gameDate, gameFGPercentage, game3PPercentage);
                },
                error -> error.printStackTrace()
        );
        mQueue.add(shotsRequest);
    }

    private void displayGameStatistics(LinearLayout gamesLL, String gameDate, double gameFGPercentage, double game3PPercentage) {
        LinearLayout gameLayout = new LinearLayout(requireContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        gameLayout.setLayoutParams(layoutParams);
        gameLayout.setOrientation(LinearLayout.VERTICAL);

        TextView date = new TextView(requireContext());
        date.setText("Date: " + gameDate);
        date.setTextSize(20);

        TextView fgP = new TextView(requireContext());
        fgP.setText(String.format(Locale.US, "FG%%: %.2f%%", gameFGPercentage));
        fgP.setTextSize(20);

        TextView threeP = new TextView(requireContext());
        threeP.setText(String.format(Locale.US, "3PT%%: %.2f%%", game3PPercentage));
        threeP.setTextSize(20);

        gameLayout.addView(date);
        gameLayout.addView(fgP);
        gameLayout.addView(threeP);

        gamesLL.addView(gameLayout);
    }

}
