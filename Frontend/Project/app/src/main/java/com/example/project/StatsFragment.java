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
    private ScrollView scroll;
    private LinearLayout ll;
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


    private ArrayList<String> playerNameArr;
    private ArrayList<Integer> pointsArr;

    private ArrayList<Integer> gameIdArr;


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


        scroll = view.findViewById(R.id.sv);
        ll = view.findViewById(R.id.ll);


        getGames2(new TeamIntListCallback() {
            @Override
            public void onTeamIntListReceived(ArrayList<Integer> i) {

                gameIdArr = i;
               /* getGames(new TeamStringListAndIntListCallback() {
                    @Override
                    public void onTeamStringListAndIntListReceived(ArrayList<String> s, ArrayList<Integer> i) {
                        playerNameArr = s;
                        pointsArr = i;
                        findLeaders();
                    }
                });*/
            }
        });


        return view;


    }


    private void getGames2(final TeamIntListCallback callback){
        String url = "http://10.0.2.2:8080/games";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {

                           gameIdArr = new ArrayList<>();


                            for(int j = 0; j < response.length(); j++) {
                                JSONObject game = response.getJSONObject(j);


                                int tid = game.getInt("team");


                                if (tid == Integer.parseInt(SharedPrefsUtil.getTeamId(getContext()))) {
                                    int gid = game.getInt("id");
                                    gameIdArr.add(gid);
                                }
                            }
                            callback.onTeamIntListReceived(gameIdArr);
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




    private void getGames(int gameId, final TeamStringListAndIntListCallback callback){
        String url = "http://10.0.2.2:8080/games";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {


                            int gameCount = 0;
                            int fgCount = 0;
                            int fgMade = 0;
                            int threePCount = 0;
                            int threePMade = 0;
                            int twoPCount = 0;
                            int twoPMade = 0;
                            int totalP = 0;


                            ArrayList<String> tempString = new ArrayList<>();
                            ArrayList<Integer> tempInt = new ArrayList<>();


                            for(int j = 0; j < response.length(); j++){
                                JSONObject game = response.getJSONObject(j);


                                int tid = game.getInt("team");


                                if(tid == Integer.parseInt(SharedPrefsUtil.getTeamId(getContext()))){
                                    JSONArray shots = game.getJSONArray("teamShots");


                                    for (int i = 0; i < shots.length(); i++) {
                                        JSONObject shot = shots.getJSONObject(i);


                                        JSONObject player = shot.getJSONObject("player");


                                        String playerName = player.getString("playerName");


                                        int currentPlayerIndex = 0;


                                        if(!(tempString.contains(playerName))){
                                            tempString.add(playerName);
                                            tempInt.add(0);
                                            currentPlayerIndex = tempInt.size()-1;
                                        }
                                        else{
                                            currentPlayerIndex = tempString.indexOf(playerName);
                                        }


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
                                                int originalVal = tempInt.get(currentPlayerIndex);
                                                int newVal = originalVal +2;
                                                tempInt.set(currentPlayerIndex, newVal);
                                            } else {
                                                threePCount += 1;
                                                threePMade+=1;
                                                int originalVal = tempInt.get(currentPlayerIndex);
                                                int newVal = originalVal +3;
                                                tempInt.set(currentPlayerIndex, newVal);
                                            }
                                        }


                                    }






                                }
                            }


                            playerNameArr = tempString;
                            pointsArr = tempInt;


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


                            gamesVal.setText(String.valueOf(gameCount));
                            pointsVal.setText(String.valueOf(totalP));
                            ppgVal.setText(ppgString);


                            fgVal.setText(fgMade + "/" + fgCount);
                            threepVal.setText(threePMade + "/" + threePCount);
                            twoPVal.setText(twoPMade + "/" + twoPCount);


                            callback.onTeamStringListAndIntListReceived(playerNameArr, pointsArr);
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


   /*private void getGameStats(final TeamStringListAndIntListCallback callback){
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


                           ArrayList<String> tempString = new ArrayList<>();
                           ArrayList<Integer> tempInt = new ArrayList<>();


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


                                   String playerName = player.getString("playerName");
                                   tempString.add(playerName);
                                   tempInt.add(playersPoints);


                               }
                           }


                           playerNameArr = tempString;
                           pointsArr = tempInt;


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


                           gamesVal.setText(String.valueOf(gameCount));
                           pointsVal.setText(String.valueOf(totalP));
                           ppgVal.setText(ppgString);


                           fgVal.setText(fgMade + "/" + fgCount);
                           threepVal.setText(threePMade + "/" + threePCount);
                           twoPVal.setText(twoPMade + "/" + twoPCount);


                           callback.onTeamStringListAndIntListReceived(playerNameArr, pointsArr);
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
   }*/


    private void findLeaders(){
        if (playerNameArr.size() > 0) {
            for(int j = 0; j < playerNameArr.size(); j++) {
                int highestPoints = pointsArr.get(0);
                String highestScorer = playerNameArr.get(0);
                int index  = 0;


                for (int i = 1; i < pointsArr.size(); i++) {
                    if (pointsArr.get(i) > highestPoints) {
                        highestPoints = pointsArr.get(i);
                        highestScorer = playerNameArr.get(i);
                        index = i;
                    }
                }


                TextView textView = new TextView(requireContext());
                textView.setText(j+ ". " + highestScorer + "            " + highestPoints);


                ll.addView(textView);


                scroll.fullScroll(ScrollView.FOCUS_UP);


                playerNameArr.remove(index);
                pointsArr.remove(index);


            }
        } else {
            Toast.makeText(getContext(), "No shots where taken", Toast.LENGTH_SHORT).show();
            return;
        }
    }


    public interface TeamStringListAndIntListCallback {
        void onTeamStringListAndIntListReceived(ArrayList<String> s, ArrayList<Integer> i);
    }

    public interface TeamIntListCallback {
        void onTeamIntListReceived(ArrayList<Integer> i);
    }
}
