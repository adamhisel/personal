package com.example.project;

import android.os.Bundle;


import androidx.fragment.app.Fragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import android.content.Intent;
import android.widget.ScrollView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


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
import com.example.project.databinding.FragmentStatsBinding;
import com.example.project.databinding.FragmentWorkoutBinding;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;


/**
 * @author Adam Hisel
 * Fragment that displays a teams overall stats from previous games. Also has a
 * leaderboard that displays the teams leading scorers.
 */
public class StatsFragment extends Fragment {


    private RequestQueue mQueue;
    private FragmentStatsBinding binding;

    private int totalPoints = 0;
    private int totalGames = 0;
    private int totalFGM = 0;
    private int totalFGA = 0;
    private int total3PM = 0;
    private int total3PA = 0;
    private int total2PM = 0;
    private int total2PA = 0;


    private ArrayList<Player> players;
    private ArrayList<Integer> pointsArr;
    private ArrayList<Integer> gameIdArr;
    private String teamId;

    private Map<Integer, Integer> playerPointsMap = new HashMap<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentStatsBinding.inflate(inflater, container, false);

        mQueue = Volley.newRequestQueue(requireContext());

        teamId = SharedPrefsUtil.getTeamId(requireContext());
        binding.header.setText(SharedPrefsUtil.getTeamName(getContext()) + " Team Stats");

        getPlayers();

        return binding.getRoot();
    }

    private void getPlayers() {
        String url = "http://coms-309-018.class.las.iastate.edu:8080/teams/" + teamId;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray playersArray = response.getJSONArray("players");
                        players = new ArrayList<>(); // Initialize the players list
                        for (int i = 0; i < playersArray.length(); i++) {
                            JSONObject playerJSON = playersArray.getJSONObject(i);
                            int playerId = playerJSON.getInt("id");
                            String playerName = playerJSON.getString("playerName");
                            int playerNumber = playerJSON.getInt("number");
                            String playerPosition = playerJSON.getString("position");

                            // Initialize Player object and add to the list
                            Player player = new Player(playerId, playerName, playerNumber, playerPosition);
                            players.add(player);
                        }

                        // After players are fetched, proceed to get games
                        getGames();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> error.printStackTrace()
        );
        mQueue.add(request);
    }

    private void getGames() {
        String url = "http://coms-309-018.class.las.iastate.edu:8080/games";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        gameIdArr = new ArrayList<>();
                        int savedTeamId = Integer.parseInt(SharedPrefsUtil.getTeamId(getContext()));
                        for (int j = 0; j < response.length(); j++) {
                            JSONObject game = response.getJSONObject(j);
                            JSONObject team = game.getJSONObject("team");
                            int tid = team.getInt("id");
                            if (tid == savedTeamId) {
                                int gid = game.getInt("id");
                                gameIdArr.add(gid);
                            }
                        }

                        // Now that we have all the game IDs, fetch the shots for each game
                        int gamesCount = gameIdArr.size();
                        for (Integer gameId : gameIdArr) {
                            getShotsForGame(gameId, gamesCount);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> error.printStackTrace()
        );
        mQueue.add(request);
    }

    private void getShotsForGame(final int gameId, final int gamesCount) {
        String url = "http://coms-309-018.class.las.iastate.edu:8080/games/" + gameId + "/shots";
        JsonArrayRequest shotsRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject shot = response.getJSONObject(i);
                            boolean made = shot.getBoolean("made");
                            int value = shot.getInt("value");

                            // Extract the player JSONObject from the shot JSONObject
                            JSONObject playerObject = shot.getJSONObject("player");
                            int playerId = playerObject.getInt("id"); // Get the player ID

                            // Find the player by ID and update their stats
                            Player player = getPlayerById(playerId);
                            if (player != null) {
                                if (value == 3) {
                                    player.recordThreePointShot(made);
                                } else {
                                    player.recordTwoPointShot(made);
                                }
                            }

                            if (made) {
                                totalPoints += value;
                                totalFGM++;
                            }
                            totalFGA++;

                            if (value == 3) {
                                if (made) {
                                    total3PM++;
                                }
                                total3PA++;
                            } else {
                                if (made) {
                                    total2PM++;
                                }
                                total2PA++;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    // Increment the totalGames after all shots for a game have been processed
                    totalGames++;

                    for (Player player : players) {
                        int points = player.getThreePointMakes() * 3 + player.getTwoPointMakes() * 2;
                        playerPointsMap.put(player.getId(), playerPointsMap.getOrDefault(player.getId(), 0) + points);
                    }

                    // Update the UI after all games have been processed
                    if (totalGames == gamesCount) {
                        findLeaders();
                        updateUI();
                    }
                },
                error -> error.printStackTrace()
        );
        mQueue.add(shotsRequest);
    }

    private void updateUI() {
        // Update the UI components
        binding.gamesVal.setText(String.valueOf(totalGames));
        binding.pointsVal.setText(String.valueOf(totalPoints));
        binding.ppgVal.setText(String.format(Locale.US, "%.2f", totalPoints / (double) totalGames));

        binding.fgPerc.setText(String.format(Locale.US, "FG%%: %.2f%%", totalFGM * 100.0 / totalFGA));
        binding.fgVal.setText(totalFGM + "/" + totalFGA);

        binding.threePPerc.setText(String.format(Locale.US, "3PT%%: %.2f%%", total3PM * 100.0 / total3PA));
        binding.threepVal.setText(total3PM + "/" + total3PA);

        binding.twoPPerc.setText(String.format(Locale.US, "2PT%%: %.2f%%", total2PM * 100.0 / total2PA));
        binding.twoPVal.setText(total2PM + "/" + total2PA);

        // Update ProgressBars
        binding.fgprog.setProgress((int) (totalFGM * 100.0 / totalFGA));
        binding.threepprog.setProgress((int) (total3PM * 100.0 / total3PA));
        binding.twopprog.setProgress((int) (total2PM * 100.0 / total2PA));
    }


    private void findLeaders() {
        // Create a list from elements of HashMap
        List<Map.Entry<Integer, Integer>> list = new LinkedList<>(playerPointsMap.entrySet());

        // Sort the list
        list.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        for (Map.Entry<Integer, Integer> entry : list) {
            Player player = getPlayerById(entry.getKey());
            if (player != null) {
                String playerDetails = player.getName() + " - " + entry.getValue() + " pts";
                TextView textView = new TextView(requireContext());
                textView.setText(playerDetails);
                binding.ll.addView(textView);
            }
        }
        binding.sv.fullScroll(ScrollView.FOCUS_UP);
    }

    private Player getPlayerById(int id) {
        for (Player player : players) {
            if (player.getId() == id) {
                return player;
            }
        }
        return null;
    }
}


