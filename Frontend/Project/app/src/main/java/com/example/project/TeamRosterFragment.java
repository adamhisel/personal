package com.example.project;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.project.R;
import com.example.project.TeamRosterCoach;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

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
public class TeamRosterFragment extends Fragment implements UpdatePlayerDialogFragment.UpdatePlayerInputListener, ConfirmDialogFragment.ConfirmInputListener{

    private RequestQueue mQueue;

    private static final String LOCAL_URL = "http://10.0.2.2:8080/";

    private static final String BASE_URL = "http://coms-309-018.class.las.iastate.edu:8080/";

    private Context mContext;

    private TextView coachText;

    private ArrayList<Integer> playerList;

    private ArrayList<String> playerNameList;

    private String playerName;

    private int playerId;

    private int newCoachId;

    private String newCoachName;

    private boolean isPrivate;

    private String teamPassword;

    private int newCoachUserId;

    private int removeUserId;
    private boolean teamHasMoreThanOneCoach;

    private int teamId;

    private LinearLayout topLL;
    private LinearLayout ll;

    private LinearLayout settingsLL;

    private LinearLayout updateLL;

    private Switch publicPrivate;
    private TextInputLayout teamName;

    private TextInputLayout password;

    private Button saveButton;

    private Button updateBackButton;

    private TextView updateValidation;

    private boolean removeClicked;

    private boolean promoteClicked;

    private boolean deleteTeamClicked;





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
        requireActivity().getWindow().setStatusBarColor(ContextCompat.getColor(requireContext(), R.color.black));
        deleteTeamClicked = false;
        mQueue = Volley.newRequestQueue(mContext);
        topLL = view.findViewById(R.id.topLL);
        coachText = view.findViewById(R.id.coach);
        Button back = view.findViewById(R.id.backButton);
        Button teamChat = view.findViewById(R.id.chatButton);
        Button teamSettings = view.findViewById(R.id.settingsButton);
        Button fanLeaveButton = view.findViewById(R.id.fanLeaveTeamButton);
        Button deleteTeam = view.findViewById(R.id.deleteTeamButton);
        Button coachLeaveButton = view.findViewById(R.id.coachLeaveTeamButton);

        settingsLL = view.findViewById(R.id.settingsll);
        publicPrivate = view.findViewById(R.id.publicPrivate);
        teamName = view.findViewById(R.id.tilTeamName);
        password = view.findViewById(R.id.tilPassword);
        saveButton = view.findViewById(R.id.saveButton);
        updateValidation = view.findViewById(R.id.validation);
        updateLL = view.findViewById(R.id.updateLL);
        updateBackButton = view.findViewById(R.id.backUpdateButton);




        ll = view.findViewById(R.id.cardLL);

        if(SharedPrefsTeamUtil.getIsCoach(mContext).equals("true")){
            teamSettings.setVisibility(View.VISIBLE);
        }
        else{
            teamSettings.setVisibility(View.GONE);
        }

        if(SharedPrefsTeamUtil.getIsFan(mContext).equals("true")){
            fanLeaveButton.setVisibility(View.VISIBLE);
        }
        else{
            fanLeaveButton.setVisibility(View.GONE);
        }






        teamId= Integer.parseInt(SharedPrefsTeamUtil.getTeamId(mContext));

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

        teamSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                topLL.setVisibility(View.GONE);
                coachText.setVisibility(View.GONE);
                ll.setVisibility(View.GONE);
                settingsLL.setVisibility(View.VISIBLE);
                updateLL.setVisibility(View.VISIBLE);
                teamSettings.setVisibility(View.GONE);
                fillTeamSettingsBoxes();

            }
        });

        deleteTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteTeamClicked = true;
                showConfirmDialog();
            }
        });


        publicPrivate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b == true){
                    password.setVisibility(View.VISIBLE);
                    publicPrivate.setChecked(true);
                }
                else{
                    password.setVisibility(View.GONE);
                    publicPrivate.setChecked(false);
                }
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateValidation.setVisibility(View.VISIBLE);
                String tn = teamName.getEditText().getText().toString().trim();
                if(publicPrivate.isChecked() == true){
                    String p = password.getEditText().getText().toString().trim();
                    if(!validateTeamName() || !validatePassword()){
                        updateValidation.setTextColor(Color.RED);
                        updateValidation.setText("Unsuccessful Team Update");
                    }
                    else{
                       updateTeamInformation(true, tn, p);
                       updateValidation.setTextColor(Color.GREEN);
                       updateValidation.setText("Successful Team Update");
                    }
                }
                else {
                    if(!validateTeamName()){
                        updateValidation.setTextColor(Color.RED);
                        updateValidation.setText("Unsuccessful Team Update");
                    }
                    else {
                        String p = "";
                        updateTeamInformation(false, tn, p);
                        updateValidation.setTextColor(Color.GREEN);
                        updateValidation.setText("Successful Team Update");
                    }
                }

            }

        });

        updateBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                topLL.setVisibility(View.VISIBLE);
                coachText.setVisibility(View.VISIBLE);
                ll.setVisibility(View.VISIBLE);
                settingsLL.setVisibility(View.GONE);
                updateLL.setVisibility(View.GONE);
                teamSettings.setVisibility(View.VISIBLE);
            }
        });

        fanLeaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int fanId = Integer.parseInt(SharedPrefsTeamUtil.getFanId(getContext()));
                deleteFan(fanId);
            }
        });

        coachLeaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int coachId = Integer.parseInt(SharedPrefsTeamUtil.getCoachId(getContext()));
                checkIfTeamHasCoach();
                if(teamHasMoreThanOneCoach == true){
                    deleteCoach(coachId);
                }
                else{
                    Toast.makeText(mContext, "Must Promote a Player to Coach Before Leaving", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    private void getPlayers(final TeamIdListsCallback callback){
        String url = BASE_URL + "teams/" + teamId;

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

        String url = BASE_URL + "teams/" + teamId;

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
                                                                    int id = cv.getId();
                                                                    int buttonId = b.getId();
                                                                    buttonId = buttonId % 2;
                                                                    if(buttonId == 0){
                                                                        removeClicked = true;
                                                                        promoteClicked = false;
                                                                    }
                                                                    else{
                                                                        removeClicked = false;
                                                                        promoteClicked = true;
                                                                    }
                                                                    playerId = playerList.get(id);
                                                                    showConfirmDialog();
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
                        int buttonId = 0;
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

                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            );
                            LinearLayout buttonLayout = new LinearLayout(mContext);
                            buttonLayout.setLayoutParams(layoutParams);
                            buttonLayout.setOrientation(LinearLayout.HORIZONTAL);

                            Button delete = new Button(mContext);
                            LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            );
                            delete.setId(buttonId);
                            delete.setVisibility(View.GONE);
                            delete.setLayoutParams(btnParams);
                            delete.setText("REMOVE PLAYER");

                            buttonId+=1;

                            Button promote = new Button(mContext);
                            LinearLayout.LayoutParams btnParams2 = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            );
                            promote.setId(buttonId);
                            promote.setVisibility(View.GONE);
                            promote.setLayoutParams(btnParams2);
                            promote.setText("PROMOTE PLAYER TO COACH");

                            linearLayout.addView(num);
                            linearLayout.addView(namePlayer);
                            linearLayout.addView(pos);
                            linearLayout.addView(delete);
                            linearLayout.addView(promote);

                            cardView.addView(linearLayout);

                            ll.addView(cardView);

                            cardId += 1;
                            buttonId+=1;
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

    private void deleteTeam() {
        String url = BASE_URL + "teams/" + teamId + "/" + SharedPrefsUtil.getUserId(getContext());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String message = response.optString("message");
                            Log.d("DeleteTeam", "Team deleted successfully");
                        } catch (Exception e) {
                            Log.e("DeleteTeam", "Error parsing response: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null) {
                            Log.e("DeleteTeam", "Error code: " + error.networkResponse.statusCode);
                        }
                        Log.e("DeleteTeam", "Error in request: " + error.getMessage());
                    }
                });

        mQueue.add(jsonObjectRequest);
    }

    private void deleteUserFromTeam(int userId) {
        String url = BASE_URL + "users/" + userId +"/" + teamId;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (response.equals("success")) {

                            Log.d("DeleteUser", "Player deleted successfully");
                        } else {
                            // Unexpected response, handle it accordingly
                            Log.e("DeleteUser", "Unexpected response after deletion");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null) {
                            Log.e("DeleteUserFromTeam", "Error code: " + error.networkResponse.statusCode);
                        }
                        Log.e("DeleteUserFromTeam", "Error in request: " + error.getMessage());
                    }
                });

        mQueue.add(jsonObjectRequest);
    }

    private void deletePlayer(int pid) {
        String url = BASE_URL + "players/" + pid +"/" + teamId;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (response.equals("success")) {

                            Log.d("DeletePlayer", "Player deleted successfully");
                        } else {
                            // Unexpected response, handle it accordingly
                            Log.e("DeletePlayer", "Unexpected response after deletion");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null) {
                            Log.e("DeletePlayer", "Error code: " + error.networkResponse.statusCode);
                        }
                        Log.e("DeletePlayer", "Error in request: " + error.getMessage());
                    }
                });

        mQueue.add(jsonObjectRequest);
    }

    private void deleteFan(int fid) {
        String url = BASE_URL + "fans/" + fid + "/" + teamId;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (response.length() == 0) {

                            Log.d("DeleteFan", "Player deleted successfully");
                        } else {
                            // Unexpected response, handle it accordingly
                            Log.e("DeleteFan", "Unexpected response after deletion");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null) {
                            Log.e("DeleteFan", "Error code: " + error.networkResponse.statusCode);
                        }
                        Log.e("DeleteFan", "Error in request: " + error.getMessage());
                    }
                });

        mQueue.add(jsonObjectRequest);
    }

    private void deleteCoach(int cid) {
        String url = BASE_URL + "coaches/" + cid + "/" + teamId;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (response.length() == 0) {

                            Log.d("DeleteCoach", "Player deleted successfully");
                        } else {
                            // Unexpected response, handle it accordingly
                            Log.e("DeleteCoach", "Unexpected response after deletion");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null) {
                            Log.e("DeleteCoach", "Error code: " + error.networkResponse.statusCode);
                        }
                        Log.e("DeleteCoach", "Error in request: " + error.getMessage());
                    }
                });

        mQueue.add(jsonObjectRequest);
    }

    private void promotePlayer(int pid){
        getPlayerInfo(pid, new CoachIdAndNameCallback() {
            @Override
            public void onCoachIdAndNameReceived(int cid, String name) {
                getTeamInfo();
                deletePlayer(pid);
                postCoach(cid, name);
                joinTeamCoach(cid);
            }
        });

    }

    private void getPlayerInfo(int pid, final CoachIdAndNameCallback callback){
        String url = BASE_URL + "player/" + pid;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            newCoachName = response.getString("playerName");
                            newCoachUserId = response.getInt("user_id");

                            callback.onCoachIdAndNameReceived(newCoachUserId, newCoachName);

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
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


    private void getTeamInfo(){
            String url = BASE_URL + "teams/" + teamId;
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                isPrivate = response.getBoolean("teamIsPrivate");
                                teamPassword = response.getString("password");

                            } catch (JSONException e) {
                                throw new RuntimeException(e);
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



    private void postCoach(int userId, String name) {
        String url = "http://coms-309-018.class.las.iastate.edu:8080/coaches";

        JSONObject postData = new JSONObject();
        try {

            postData.put("name", name);
            postData.put("user_id", userId);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            newCoachId = response.getInt("id");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                        Log.d("PostCoach", "Response received: " + response.toString());

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle the error, e.g., display an error message
                Log.e("PostCoach", "Error in request: " + error.getMessage());
            }
        });
        mQueue.add(jsonObjectRequest);
    }

    private void joinTeamCoach(int userId){

        String url = "";
        if(isPrivate == true) {
            url = "http://coms-309-018.class.las.iastate.edu:8080/teams/" + teamId + "/coaches/" + newCoachId + "/" + teamPassword + "/" + userId;
        }
        else{
            url = "http://coms-309-018.class.las.iastate.edu:8080/teams/" + teamId + "/coaches/" + newCoachId + "/dummy" + "/" + userId;
        }
        StringRequest putRequest = new StringRequest(Request.Method.PUT, url,
                response -> {
                    if ("success".equals(response)) {

                    } else if ("failure".equals(response)) {
                    }
                },
                error -> {

                    if (error.networkResponse != null) {
                        String errorMessage = new String(error.networkResponse.data);
                        Log.e("JoinTeam", "Error in request: " + errorMessage);
                    }
                }
        );

        mQueue.add(putRequest);


    }

    private void fillTeamSettingsBoxes() {
        String url = BASE_URL + "teams/" + teamId;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            teamName.getEditText().setText(response.getString("teamName"));
                            if(response.getBoolean("teamIsPrivate") == true){
                                password.setVisibility(View.VISIBLE);
                                password.getEditText().setText(response.getString("password"));
                                publicPrivate.setChecked(true);
                            }
                            else{
                                password.setVisibility(View.GONE);
                                publicPrivate.setChecked(false);
                            }


                        } catch (JSONException e) {
                            throw new RuntimeException(e);
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

    private void updateTeamInformation(boolean priv, String teamName, String password){
        JSONObject postData = new JSONObject();
        try {
            postData.put("teamName", teamName);
            postData.put("teamIsPrivate", priv);
            postData.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = BASE_URL + "updateTeam/" + teamId;


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status = response.getString("message");
                            if ("success".equals(status)) {
                                Toast.makeText(mContext, "Team Updated!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(mContext, "Error pupdating team!", Toast.LENGTH_SHORT).show();
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

    private void checkIfTeamHasCoach(){

        String url = BASE_URL + "teams/" + teamId;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONArray coaches = response.getJSONArray("coaches");
                    teamHasMoreThanOneCoach = false;
                    if(coaches.length() <= 1){
                        teamHasMoreThanOneCoach = false;
                    }
                    else{
                        teamHasMoreThanOneCoach = true;
                    }


                        } catch (JSONException e) {
                            throw new RuntimeException(e);
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

    public interface TeamIdListsCallback {
        void onTeamIdListsReceived(ArrayList<Integer> idList, ArrayList<String> nameList);
    }

    public interface CoachIdAndNameCallback {
        void onCoachIdAndNameReceived(int cid, String name);
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

        String url = BASE_URL + "updatePlayer/" + playerId;


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

    private void showConfirmDialog() {
        ConfirmDialogFragment confirmDialog = new ConfirmDialogFragment();
        confirmDialog.setListener((ConfirmDialogFragment.ConfirmInputListener) this);
        confirmDialog.show(requireActivity().getSupportFragmentManager(), "confirmDialog");
    }

    @Override
    public void onConfirm(boolean confirm) {
        if(confirm == true){
            if(removeClicked == true){
                getPlayerInfo(playerId, new CoachIdAndNameCallback() {
                    @Override
                    public void onCoachIdAndNameReceived(int cid, String name) {
                        deletePlayer(playerId);
                        deleteUserFromTeam(cid);
                        removeClicked = false;
                        promoteClicked = false;
                        deleteTeamClicked = false;
                    }
                });
            }
            else if(promoteClicked == true){
                promotePlayer(playerId);
                removeClicked = false;
                promoteClicked = false;
                deleteTeamClicked = false;
            }
            else if(deleteTeamClicked == true){
                deleteTeam();
                removeClicked = false;
                promoteClicked = false;
                deleteTeamClicked = false;
                SharedPrefsTeamUtil.clearTeamData(mContext);
                Intent intent = new Intent(mContext, MainActivity.class);
                startActivity(intent);
            }
        }
    }



    private Boolean validateTeamName() {
        String tilName = teamName.getEditText().getText().toString().trim();

        if (tilName.isEmpty()) {
            teamName.setError("Field cannot be empty");
            return false;
        } else {
            teamName.setError(null);
            teamName.setErrorEnabled(false);
            return true;

        }
    }

    private Boolean validatePassword() {
        String tilName = password.getEditText().getText().toString().trim();

        if (tilName.isEmpty()) {
            password.setError("Field cannot be empty");
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }
}




