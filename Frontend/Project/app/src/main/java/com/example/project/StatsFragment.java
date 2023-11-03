package com.example.project;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

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

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * @author Adam Hisel
 * Fragment that displays a teams overall stats from previous games. Also has a
 * leaderboard that displays the teams leading scorers.
 */
public class StatsFragment extends Fragment {

    private RequestQueue mQueue;
    private TextView fgPerc1;
    private TextView threePPerc1;
    private TextView twoPPerc1;

    private ProgressBar fgProg;
    private ProgressBar threeProg;
    private ProgressBar twoProg;

    private TextView gamesVal;
    private TextView pointsVal;
    private TextView ppgVal;

    private TextView fgVal;
    private TextView threepVal;
    private TextView twoPVal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stats, container, false);

        mQueue = Volley.newRequestQueue(requireContext());
        TextView header = view.findViewById(R.id.header);

        header.setText(SharedPrefsUtil.getTeamName(getContext()) + " Team Stats");

        fgPerc1 = view.findViewById(R.id.fgPerc);
        threePPerc1 = view.findViewById(R.id.threePPerc);
        twoPPerc1 = view.findViewById(R.id.twoPPerc);

        fgProg = view.findViewById(R.id.fgprog);
        threeProg = view.findViewById(R.id.threepprog);
        twoProg = view.findViewById(R.id.twopprog);

        gamesVal = view.findViewById(R.id.gamesVal);
        pointsVal = view.findViewById(R.id.pointsVal);
        ppgVal = view.findViewById(R.id.ppgVal);

        fgVal = view.findViewById(R.id.fgVal);
        threepVal = view.findViewById(R.id.threepVal);
        twoPVal = view.findViewById(R.id.twoPVal);

        getGameStats();

        return view;

    }

    private void getGameStats(){
        String url = "http://10.0.2.2:8080/teams/" + SharedPrefsUtil.getTeamId(getContext());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray games = response.getJSONArray("games");

                            int gameCount = 0;
                            int fgCount = 0;
                            int fgMade = 0;
                            int threePCount = 0;
                            int threePMade = 0;
                            int twoPCount = 0;
                            int twoPMade = 0;
                            int totalP = 0;

                            for (int i = 0; i < games.length(); i++) {
                                JSONObject game = games.getJSONObject(i);
                                gameCount += 1;

                                JSONArray players = game.getJSONArray("players");

                                for(int k = 0; k < players.length(); k++) {

                                    JSONObject player = players.getJSONObject(k);

                                    int playersPoints = 0;
                                    JSONArray shots = player.getJSONArray("shots");

                                    for (int j = 0; j < shots.length(); j++) {
                                        JSONObject shot = shots.getJSONObject(j);

                                        int makeMiss = shot.getInt("makemiss");
                                        int value = shot.getInt("value");

                                        if (makeMiss == 0) {
                                            fgCount += 1;
                                            if (value == 2) {
                                                twoPCount += 1;
                                            } else {
                                                threePCount += 1;
                                            }
                                        } else {
                                            fgCount += 1;
                                            fgMade += 1;
                                            if (value == 2) {
                                                twoPCount += 1;
                                                twoPMade +=1;
                                                playersPoints += 2;
                                            } else {
                                                threePCount += 1;
                                                threePMade+=1;
                                                playersPoints += 3;
                                            }
                                        }

                                    }


                                }
                            }

                                DecimalFormat decimalFormat = new DecimalFormat("#.##");
                                DecimalFormat decimalFormat2 = new DecimalFormat("#.#");

                                totalP = threePMade * 3 + twoPMade * 2;

                                float ppg = ((float)totalP/gameCount);

                                String ppgString = decimalFormat2.format(ppg);


                                float fgPerc = ((float) fgMade / fgCount) * 100;

                                String fgPercString = decimalFormat.format(fgPerc);

                                float fgPercFloat = Float.parseFloat(fgPercString);

                                int fgPercInt = Math.round(fgPercFloat);

                                fgProg.setProgress(fgPercInt);



                                float threePPerc = ((float) threePMade / threePCount) * 100;

                                String threePPercString = decimalFormat.format(threePPerc);

                                float threePPercFloat = Float.parseFloat(threePPercString);

                                int threePercInt = Math.round(threePPercFloat);

                                threeProg.setProgress(threePercInt);



                                float twoPPerc = ((float) twoPMade / twoPCount) * 100;

                                String twoPPercString = decimalFormat.format(twoPPerc);

                                float twoPPercFloat = Float.parseFloat(twoPPercString);

                                int twoPercInt = Math.round(twoPPercFloat);

                                twoProg.setProgress(twoPercInt);

                                fgPerc1.setText("FG%: " + twoPPercString + "%");
                                threePPerc1.setText("3PT%: " + threePPercString + "%");
                                twoPPerc1.setText("2PT%: " + twoPPercString + "%");

                                gamesVal.setText(gameCount);
                                pointsVal.setText(totalP);
                                ppgVal.setText(ppgString);

                                fgVal.setText(fgMade + "/" + fgCount);
                                threepVal.setText(threePMade + "/" + threePCount);
                                twoPVal.setText(twoPMade + "/" + twoPCount);


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
}
