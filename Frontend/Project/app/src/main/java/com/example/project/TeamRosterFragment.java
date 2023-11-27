package com.example.project;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.project.R;
import com.example.project.TeamRosterCoach;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Activity that is opened when a coach clicks on a team they coach.
 * Displays a roster with the players, coaches and managers currently on
 * the team. Gives the coach special options such as the ability
 * to remove players, edit positions and change team settings.
 *
 * @author Adam Hisel
 */
public class TeamRosterFragment extends Fragment implements UpdatePlayerDialogFragment.UpdatePlayerInputListener{

    private RequestQueue mQueue;

    private Context mContext;

    private TextView coachText;

    private ArrayList<Integer> playerList;

    private ArrayList<String> playerNameList;

    private String playerName;

    private int playerId;

    private int teamId;

    private String teamName;

    private LinearLayout ll;


    public TeamRosterFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_team_roster, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mQueue = Volley.newRequestQueue(mContext);
        //tl =  view.findViewById(R.id.tableLayout);
        coachText = view.findViewById(R.id.coach);
        Button back = view.findViewById(R.id.backButton);
        Button teamChat = view.findViewById(R.id.chatButton);
        //Button teamSettings = view.findViewById(R.id.settingsButton);

        ll = view.findViewById(R.id.cardLL);


        teamId= Integer.parseInt(SharedPrefsTeamUtil.getTeamId(mContext));
        teamName= SharedPrefsTeamUtil.getTeamName(mContext);

        addPlayerDisplay();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPrefsTeamUtil.clearTeamData(mContext);
                Intent intent = new Intent(mContext, MainActivity.class);
                startActivity(intent);
            }
        });

        teamChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, TeamChat.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    private void getPlayers(final TeamIdListsCallback callback){
        String url = "http://coms-309-018.class.las.iastate.edu:8080/teams/" + teamId;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray players = response.getJSONArray("players");
                    playerList = new ArrayList<>();
                    playerNameList = new ArrayList<>();
                    for (int i = 0; i < players.length(); i++) {
                        JSONObject player = players.getJSONObject(i);
                        int id = player.getInt("id");
                        String name = player.getString("playerName");
                        playerList.add(id);
                        playerNameList.add(name);
                    }
                    callback.onTeamIdListsReceived(playerList, playerNameList);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }
        , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mQueue.add(request);
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

                        if(SharedPrefsTeamUtil.getIsCoach(mContext).equals("true")) {


                            MaterialButton editButton = new MaterialButton(mContext, null);
                            editButton.setLayoutParams(new ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT
                            ));

                            editButton.setTop(5);
                            editButton.setBottom(5);
                            editButton.setBackgroundColor(mContext.getColor(R.color.black));
                            editButton.setTextColor(mContext.getColor(R.color.white));
                            editButton.setIcon(mContext.getDrawable(R.drawable.baseline_edit_24));

                            editButton.setText("EDIT TEAM ROSTER");

                            ll.addView(editButton);

                            MaterialButton saveButton = new MaterialButton(mContext, null);
                            saveButton.setLayoutParams(new ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT
                            ));

                            saveButton.setTop(5);
                            saveButton.setBottom(5);
                            saveButton.setBackgroundColor(mContext.getColor(R.color.black));
                            saveButton.setTextColor(mContext.getColor(R.color.white));
                            saveButton.setIcon(mContext.getDrawable(R.drawable.baseline_check_24));

                            saveButton.setText("SAVE ROSTER");
                            saveButton.setVisibility(View.GONE);

                            TextView textView = new TextView(mContext);

                            textView.setText("Click Player Cards To Update Player Information");

                            textView.setTextSize(20);

                            textView.setGravity(Gravity.CENTER);
                            textView.setVisibility(View.GONE);

                            ll.addView(saveButton);
                            ll.addView(textView);


                            editButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    saveButton.setVisibility((View.VISIBLE));
                                    textView.setVisibility((View.VISIBLE));
                                    for (int l = 0; l < ll.getChildCount(); l++) {
                                        View v = ll.getChildAt(l);
                                        if (v instanceof CardView) {
                                            CardView cv = (CardView) v;
                                            cv.setForeground(ContextCompat.getDrawable(mContext, R.drawable.card_foreground));
                                            cv.setClickable(true);
                                            cv.setFocusable(true);

                                            LinearLayout ll2 = (LinearLayout) cv.getChildAt(0);

                                            for (int k = 0; k < ll2.getChildCount(); k++) {
                                                View b = ll2.getChildAt(k);
                                                if(b instanceof Button){
                                                    Button button = (Button) b;
                                                    button.setVisibility(View.VISIBLE);

                                                    button.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            getPlayers(new TeamIdListsCallback() {
                                                                @Override
                                                                public void onTeamIdListsReceived(ArrayList<Integer> idList, ArrayList<String> nameList) {
                                                                    playerList = idList;
                                                                    int id = v.getId();
                                                                    playerId = playerList.get(id);
                                                                    deletePlayer(playerId);

                                                                }
                                                            });
                                                        }
                                                    });
                                                }

                                            }


                                            cv.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {

                                                    getPlayers(new TeamIdListsCallback() {
                                                        @Override
                                                        public void onTeamIdListsReceived(ArrayList<Integer> idList, ArrayList<String> nameList) {
                                                            playerList = idList;
                                                            playerNameList = nameList;
                                                            int id = v.getId();
                                                            playerId = playerList.get(id);
                                                            playerName = playerNameList.get(id);
                                                            showUpdatePlayerDialog();

                                                        }
                                                    });
                                                }
                                            });
                                        }
                                    }
                                }
                            });

                            saveButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    editButton.setVisibility(View.VISIBLE);
                                    saveButton.setVisibility((View.GONE));
                                    textView.setVisibility((View.GONE));
                                    for (int l = 0; l < ll.getChildCount(); l++) {
                                        View v = ll.getChildAt(l);
                                        if (v instanceof CardView) {
                                            CardView cv = (CardView) v;
                                            cv.setClickable(false);
                                            cv.setFocusable(false);

                                        }
                                    }
                                    ll.removeAllViews();
                                    addPlayerDisplay();
                                }
                            });
                        }



                        int cardId = 0;
                        for (int i= 0; i < players.length(); i++) {
                            JSONObject player = players.getJSONObject(i);

                            String name = player.getString("playerName");
                            String number = "#" + player.getString("number");
                            String position = player.getString("position");


                            Log.d("TeamRoster", "Info" + name);

                            CardView cardView = new CardView(mContext);
                            LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            );
                            cardParams.setMargins(8, 8, 8, 8);
                            cardView.setLayoutParams(cardParams);
                            cardView.setId(cardId);
                            cardView.setCardElevation(4);


                            LinearLayout linearLayout = new LinearLayout(mContext);
                            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            ));
                            linearLayout.setOrientation(LinearLayout.VERTICAL);


                            TextView num = new TextView(mContext);
                            LinearLayout.LayoutParams numberParams = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            );
                            numberParams.setMargins(16, 16, 16, 0);
                            num.setLayoutParams(numberParams);
                            num.setText(number);
                            num.setTextSize(18);
                            num.setTypeface(null, Typeface.BOLD);

                            TextView namePlayer = new TextView(mContext);
                            LinearLayout.LayoutParams nameParams = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            );
                            nameParams.setMargins(16, 8, 16, 0);
                            namePlayer.setLayoutParams(nameParams);
                            namePlayer.setText(name);
                            namePlayer.setTextSize(24);
                            namePlayer.setTypeface(null, Typeface.BOLD);


                            TextView pos = new TextView(mContext);
                            LinearLayout.LayoutParams positionParams = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            );
                            positionParams.setMargins(16, 4, 16, 16);
                            pos.setLayoutParams(positionParams);
                            pos.setText(position);
                            pos.setTextSize(18);

                            Button delete = new Button(mContext);
                            LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            );
                            delete.setId(cardId);
                            delete.setVisibility(View.GONE);
                            delete.setLayoutParams(btnParams);
                            delete.setText("REMOVE");


                            linearLayout.addView(num);
                            linearLayout.addView(namePlayer);
                            linearLayout.addView(pos);
                            linearLayout.addView(delete);

                            cardView.addView(linearLayout);

                            ll.addView(cardView);

                            cardId += 1;
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

    private void deletePlayer(int pid) {
        String url = "http://coms-309-018.class.las.iastate.edu:8080/players/" + pid;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String message = response.optString("message");
                            Log.d("DeleteUser", "Player deleted successfully");
                        } catch (Exception e) {
                            Log.e("DeleteUser", "Error parsing response: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null) {
                            Log.e("DeleteUser", "Error code: " + error.networkResponse.statusCode);
                        }
                        Log.e("DeleteUser", "Error in request: " + error.getMessage());
                    }
                });

        mQueue.add(jsonObjectRequest);
    }

    public interface TeamIdListsCallback {
        void onTeamIdListsReceived(ArrayList<Integer> idList, ArrayList<String> nameList);
    }

    private void showUpdatePlayerDialog() {
        UpdatePlayerDialogFragment updateDialog = new UpdatePlayerDialogFragment();
        updateDialog.setListener((UpdatePlayerDialogFragment.UpdatePlayerInputListener) this);
        updateDialog.show(requireActivity().getSupportFragmentManager(), "updatePlayerDialog");
    }

    @Override
    public void onUpdate(String number, String pos) {
        JSONObject postData = new JSONObject();
        try {
            postData.put("playerName", playerName);
            postData.put("number", number);
            postData.put("position", pos);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = "http://coms-309-018.class.las.iastate.edu:8080/updatePlayer/" + playerId;


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status = response.getString("message");
                            if ("success".equals(status)) {
                                Toast.makeText(mContext, "Profile Updated!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(mContext, "Error pupdating profile!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(mContext, "Error parsing response!", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(mContext, "Error updating profile!", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                });

        mQueue.add(jsonObjectRequest);
    }
}




