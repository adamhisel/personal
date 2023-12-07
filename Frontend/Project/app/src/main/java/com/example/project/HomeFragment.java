package com.example.project;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.BlendMode;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
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
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collection;
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

    private static final String LOCAL_URL = "http://10.0.2.2:8080/";

    private static final String BASE_URL = "http://coms-309-018.class.las.iastate.edu:8080/";

    private LinearLayout ll;

    private Bundle savedInstance;

    private Map<Switch, LinearLayout> toggleButtonMap = new HashMap<>();

    private ArrayList<Integer> gameIdArr;

    private Map<Integer, LinearLayout> teamGamesLLMap = new HashMap<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        savedInstance = savedInstanceState;
        View view = inflater.inflate(R.layout.fragment_home_coach, container, false);

        mQueue = Volley.newRequestQueue(requireContext());
        requireActivity().getWindow().setStatusBarColor(ContextCompat.getColor(requireContext(), R.color.black));
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
        String url = LOCAL_URL + "users/" + SharedPrefsUtil.getUserId(getContext());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                        JSONArray teams = response.getJSONArray("teams");

                        ArrayList<String> teamList = new ArrayList<>();
                        ArrayList<Integer> teamIds = new ArrayList<>();
                        ArrayList<Integer> coachUserIds = new ArrayList<>();
                        ArrayList<Integer> fanUserIds = new ArrayList<>();
                        ArrayList<Integer> coachIds = new ArrayList<>();
                        ArrayList<Integer> fanIds = new ArrayList<>();

                        for (int i = 0; i < teams.length(); i++) {
                            JSONObject team = teams.getJSONObject(i);
                            teamList.add(team.getString("teamName"));
                            teamIds.add(Integer.valueOf(team.getString("id")));
                            JSONArray coaches = team.getJSONArray("coaches");
                            JSONArray fans = team.getJSONArray("fans");

                            for(int l = 0; l < coaches.length(); l++){
                                JSONObject coach = coaches.getJSONObject(l);
                                coachUserIds.add(coach.getInt("user_id"));
                                coachIds.add(coach.getInt("id"));
                            }

                            for(int l = 0; l < fans.length(); l++){
                                JSONObject fan = fans.getJSONObject(l);
                                fanUserIds.add(fan.getInt("user_id"));
                                fanIds.add(fan.getInt("id"));

                            }
                        }

                        for(int j =0; j< teamList.size(); j++) {

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



                            MaterialButton button = new MaterialButton(requireContext());

                            button.setLayoutParams(halfLLVert);
                            button.setTextColor(Color.WHITE);
                            button.setBackgroundColor(Color.BLACK);
                            button.setText("View Team");
                            button.setTag(id);
                            button.setTextSize(20);

                            Switch tButton = new Switch(requireContext());

                            tButton.setLayoutParams(halfLLVert);
                            tButton.setText("View Recent Game");
                            tButton.setTextOff("View Recent Game");
                            tButton.setTextOn("Hide Recent Game");
                            tButton.setTrackTintList(ColorStateList.valueOf(Color.parseColor("#FF9800")));
                            tButton.setThumbTintList(ColorStateList.valueOf(Color.BLACK));
                            tButton.setId(id);
                            tButton.setTextSize(20);



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

//                            teamGamesLLMap.put(id, gamesLL);

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
//                                        int teamId = 0;
//                                        for(int j = 0; j < linearLayout.getChildCount(); j++){
//                                            View v = linearLayout.getChildAt(j);
//                                            if(v instanceof LinearLayout){
//                                                for(int l = 0; l < ((LinearLayout) v).getChildCount(); l++) {
//                                                    View b = ((LinearLayout) v).getChildAt(l);
//                                                    if(b instanceof ToggleButton){
//                                                        teamId = b.getId();
//                                                    }
//                                                }
//                                            }
//                                        }
//                                        getGames(teamId, gamesLL, new TeamListCallback() {
//                                            @Override
//                                            public void onTeamListReceived(ArrayList<Integer> teamList) {
//                                                gameIdArr = teamList;
//                                                for (int i = 0; i < gameIdArr.size(); i++) {
//                                                    getShotsForGame(gameIdArr.get(i), gamesLL);
//                                                }
//                                            }
//                                        });

                                        getGames(id, gamesLL);

                                        Collection<LinearLayout> values = teamGamesLLMap.values();


                                        for (LinearLayout value : values) {
                                            TextView t = (TextView) value.getChildAt(1);
                                        }


                                        gamesLL.setVisibility(View.VISIBLE);

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
                                        int coachId = 0;
                                        int fanId = 0;
                                        for(int i = 0; i < coachUserIds.size(); i++){
                                            int coachUserId = coachUserIds.get(i);

                                            if(SharedPrefsUtil.getUserId(getContext()).equals(String.valueOf(coachUserId))){
                                                isCoach = "true";
                                                coachId = coachIds.get(i);
                                                break;
                                            }else{
                                                isCoach = "false";
                                                coachId = 0;
                                            }

                                        }
                                        for(int j = 0; j < fanUserIds.size(); j++){
                                            int fanUserId = fanUserIds.get(j);
                                            if(SharedPrefsUtil.getUserId(getContext()).equals(String.valueOf(fanUserId))){
                                                isFan = "true";
                                                fanId = fanIds.get(j);
                                                break;
                                            }else{
                                                isFan = "false";
                                                fanId = 0;
                                            }

                                        }

                                        SharedPrefsTeamUtil.saveTeamData(getContext(), teamName, String.valueOf(id), isCoach, isFan, String.valueOf(fanId), String.valueOf(coachId));
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


    private void getGames(int id, LinearLayout parentLL) {
        parentLL.removeAllViews();
        String url = LOCAL_URL + "games";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        if (response.length() >= 1) {
                            gameIdArr = new ArrayList<>();
                            int savedTeamId = id;
                            for (int j = 0; j < response.length(); j++) {
                                JSONObject game = response.getJSONObject(j);
                                JSONObject team = game.getJSONObject("team");
                                int tid = team.getInt("id");
                                if (tid == savedTeamId) {
                                    int gid = game.getInt("id");
                                    gameIdArr.add(gid);

                                }
                            }
                            for(int i = 0; i < gameIdArr.size(); i++){

                                LinearLayout gameLL = new LinearLayout(requireContext());
                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                );
                                gameLL.setLayoutParams(layoutParams);
                                gameLL.setOrientation(LinearLayout.VERTICAL);

                                teamGamesLLMap.put(i, gameLL);

                                if(i == gameIdArr.size()-1){
                                    getShotsForGame(gameIdArr.get(i), gameLL, parentLL);
                                }
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> error.printStackTrace()
        );
        mQueue.add(request);
    }

    private void getShotsForGame(int gameId, LinearLayout gameLL, LinearLayout parentLL) {
        String url = LOCAL_URL + "games/" + gameId + "/shots";
        JsonArrayRequest shotsRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    int gameFGM = 0;
                    int gameFGA = 0;
                    int game3PM = 0;
                    int game3PA = 0;
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            if(!(response.length() == 0)) {
                                JSONObject shot = response.getJSONObject(i);
                                boolean made = shot.getBoolean("made");
                                int value = shot.getInt("value");

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
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    TextView date = new TextView(requireContext());
                    date.setText("Date: ");
                    date.setTextSize(25);

                    TextView fgP = new TextView(requireContext());
                    fgP.setText(String.format(Locale.US, "FG%%: %.2f%%", gameFGM * 100.0 / gameFGA));
                    fgP.setTextSize(25);

                    ProgressBar progFGP = new ProgressBar(requireContext(), null, android.R.attr.progressBarStyleHorizontal);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    progFGP.setLayoutParams(layoutParams);
                    progFGP.setScaleY(2);
                    progFGP.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#FF9800")));
                    progFGP.setProgress((int) (gameFGM * 100.0 / gameFGA));

                    TextView threeP = new TextView(requireContext());
                    threeP.setText(String.format(Locale.US, "3PT%%: %.2f%%", game3PM * 100.0 / game3PA));
                    threeP.setTextSize(25);

                    ProgressBar progTHREEP = new ProgressBar(requireContext(), null, android.R.attr.progressBarStyleHorizontal);
                    progTHREEP.setLayoutParams(layoutParams);
                    progTHREEP.setScaleY(2);
                    progTHREEP.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#FF9800")));
                    progTHREEP.setProgress((int) (game3PM * 100.0 / game3PA));

                    if(fgP.getText() == "FG%: NaN%"){
                        fgP.setText("No Shots Were Shot");
                        threeP.setText("No Shots Were Taken");
                    }
                    else if(threeP.getText() == "3PT%: NaN%"){
                        threeP.setText(("No Three's Were Shot"));
                    }

                    gameLL.addView(date);
                    gameLL.addView(fgP);
                    gameLL.addView(progFGP);
                    gameLL.addView(threeP);
                    gameLL.addView(progTHREEP);

                    parentLL.addView(gameLL);

//                    displayGame(gameLL, gameFGM, gameFGA, game3PM, game3PA);
                },
                error -> error.printStackTrace()
        );
        mQueue.add(shotsRequest);
    }


    public interface TeamListCallback {
        void onTeamListReceived(ArrayList<Integer> teamList);
    }

}
