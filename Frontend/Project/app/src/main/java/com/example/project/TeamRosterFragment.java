package com.example.project;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
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

/**
 * Activity that is opened when a coach clicks on a team they coach.
 * Displays a roster with the players, coaches and managers currently on
 * the team. Gives the coach special options such as the ability
 * to remove players, edit positions and change team settings.
 *
 * @author Adam Hisel
 */
public class TeamRosterFragment extends Fragment implements UpdatePlayerDialogFragment.UpdatePlayerInputListener{

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
        //tl =  view.findViewById(R.id.tableLayout);
        coachText = view.findViewById(R.id.coach);
        Button back = view.findViewById(R.id.backButton);
        Button teamChat = view.findViewById(R.id.chatButton);
        //Button edit = view.findViewById(R.id.editButton);


        teamId= Integer.parseInt(SharedPrefsUtil.getTeamId(getContext()));
        teamName= SharedPrefsUtil.getTeamName(getContext());

        //makeHeader();
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

//        edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                cardView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                    }
//                });
//            }
//        });

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

                        LinearLayout ll = requireView().findViewById(R.id.cardLL);

                        MaterialButton editButton = new MaterialButton(requireContext(), null);
                        editButton.setLayoutParams(new ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        ));

                        editButton.setTop(5);
                        editButton.setBottom(5);
                        editButton.setBackgroundColor(getResources().getColor(R.color.black));
                        editButton.setTextColor(getResources().getColor(R.color.white));
                        editButton.setIcon(getResources().getDrawable(R.drawable.baseline_edit_24));

                        editButton.setText("EDIT TEAM ROSTER");

                        ll.addView(editButton);

                        MaterialButton saveButton = new MaterialButton(requireContext(), null);
                        saveButton.setLayoutParams(new ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        ));

                        saveButton.setTop(5);
                        saveButton.setBottom(5);
                        saveButton.setBackgroundColor(getResources().getColor(R.color.black));
                        saveButton.setTextColor(getResources().getColor(R.color.white));
                        saveButton.setIcon(getResources().getDrawable(R.drawable.baseline_check_24));

                        saveButton.setText("SAVE ROSTER");
                        saveButton.setVisibility(View.INVISIBLE);

                        ll.addView(saveButton);


                        for (int i= 0; i < players.length(); i++) {
                            JSONObject player = players.getJSONObject(i);

                            String name = player.getString("playerName");
                            String number = "#" + player.getString("number");
                            String position = player.getString("position");


                            Log.d("TeamRoster", "Info" + name);

                            CardView cardView = new CardView(requireContext());
                            LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            );
                            cardParams.setMargins(8, 8, 8, 8);
                            cardView.setLayoutParams(cardParams);
                            cardView.setCardElevation(4);


                            LinearLayout linearLayout = new LinearLayout(requireContext());
                            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            ));
                            linearLayout.setOrientation(LinearLayout.VERTICAL);


                            TextView num = new TextView(requireContext());
                            LinearLayout.LayoutParams numberParams = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            );
                            numberParams.setMargins(16, 16, 16, 0);
                            num.setLayoutParams(numberParams);
                            num.setText(number);
                            num.setTextSize(18);
                            num.setTypeface(null, Typeface.BOLD);


                            TextView namePlayer = new TextView(requireContext());
                            LinearLayout.LayoutParams nameParams = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            );
                            nameParams.setMargins(16, 8, 16, 0);
                            namePlayer.setLayoutParams(nameParams);
                            namePlayer.setText(name);
                            namePlayer.setTextSize(24);
                            namePlayer.setTypeface(null, Typeface.BOLD);


                            TextView pos = new TextView(requireContext());
                            LinearLayout.LayoutParams positionParams = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            );
                            positionParams.setMargins(16, 4, 16, 16);
                            pos.setLayoutParams(positionParams);
                            pos.setText(position);
                            pos.setTextSize(18);




                            linearLayout.addView(num);
                            linearLayout.addView(namePlayer);
                            linearLayout.addView(pos);


                            cardView.addView(linearLayout);

                            ll.addView(cardView);



                            editButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    editButton.setVisibility(View.INVISIBLE);
                                    saveButton.setVisibility((View.VISIBLE));
                                    for (int l = 0; l < ll.getChildCount(); l++) {
                                        View v = ll.getChildAt(l);
                                        if (v instanceof CardView) {
                                            CardView cv = (CardView) v;
                                            cv.setClickable(true);
                                            cv.setFocusable(true);
                                            cv.setForeground(ContextCompat.getDrawable(getContext(), R.drawable.card_foreground));
                                            cv.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    showUpdatePlayerDialog();
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
                                    saveButton.setVisibility((View.INVISIBLE));
                                    for (int l = 0; l < ll.getChildCount(); l++) {
                                        View v = ll.getChildAt(l);
                                        if (v instanceof CardView) {
                                            CardView cv = (CardView) v;
                                            cv.setClickable(false);
                                            cv.setFocusable(false);
                                        }
                                    }
                                }
                            });


//                            android.widget.TableRow.LayoutParams trparams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
//
//                            TableRow tableRow = new TableRow(requireContext());
//
//                            Resources resources = getResources();
//                            Drawable drawable = resources.getDrawable(R.drawable.textbox_borders);
//
//                            TextView textView = new TextView(requireContext());
//                            textView.setPadding(10, 10, 10, 10);
//                            textView.setLayoutParams(trparams);
//                            textView.setTextSize(25);
//                            textView.setBackground(drawable);
//                            textView.setText(number);
//                            tableRow.addView(textView);
//
//
//                            TextView textView2 = new TextView(requireContext());
//                            textView2.setPadding(10, 10, 10, 10);
//                            textView2.setLayoutParams(trparams);
//                            textView2.setTextSize(25);
//                            textView2.setBackground(drawable);
//                            textView2.setText(name);
//                            tableRow.addView(textView2);
//
//                            TextView textView3 = new TextView(requireContext());
//                            textView3.setPadding(10, 10, 10, 10);
//                            textView3.setLayoutParams(trparams);
//                            textView3.setTextSize(25);
//                            textView3.setBackground(drawable);
//                            textView3.setText(position);
//                            tableRow.addView(textView3);


                            //tl.addView(tableRow);
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

    private void showUpdatePlayerDialog() {
        UpdatePlayerDialogFragment updateDialog = new UpdatePlayerDialogFragment();
        updateDialog.setListener((UpdatePlayerDialogFragment.UpdatePlayerInputListener) this);
        updateDialog.show(requireActivity().getSupportFragmentManager(), "updatePlayerDialog");
    }

    @Override
    public void onUpdate(String number, String pos, String playerId) {
        JSONObject postData = new JSONObject();
        try {
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
                                Toast.makeText(requireContext(), "Profile Updated!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(requireContext(), "Error updating profile!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(requireContext(), "Error parsing response!", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(requireContext(), "Error updating profile!", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                });

        mQueue.add(jsonObjectRequest);
    }
}



    /**
     * This method makes a table header on screen with Number, Name and Position
     */

//    public void makeHeader(){
//        android.widget.TableRow.LayoutParams trparams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
//
//        TableRow tableRow = new TableRow(requireContext());
//
//        Resources resources = getResources();
//        Drawable drawable = resources.getDrawable(R.drawable.textbox_borders);
//
//        TextView textView = new TextView(requireContext());
//        textView.setPadding(10, 10, 10, 10);
//        textView.setLayoutParams(trparams);
//        textView.setTextSize(25);
//        textView.setTypeface(null, android.graphics.Typeface.BOLD);
//        textView.setBackground(drawable);
//        textView.setText("Number");
//        tableRow.addView(textView);
//
//
//        TextView textView2 = new TextView(requireContext());
//        textView2.setPadding(10, 10, 10, 10);
//        textView2.setLayoutParams(trparams);
//        textView2.setTextSize(25);
//        textView2.setTypeface(null, android.graphics.Typeface.BOLD);
//        textView2.setBackground(drawable);
//        textView2.setText("Name");
//        tableRow.addView(textView2);
//
//        TextView textView3 = new TextView(requireContext());
//        textView3.setPadding(10, 10, 10, 10);
//        textView3.setLayoutParams(trparams);
//        textView3.setTextSize(25);
//        textView3.setTypeface(null, android.graphics.Typeface.BOLD);
//        textView3.setBackground(drawable);
//        textView3.setText("Position");
//        tableRow.addView(textView3);
//
//        //tl.addView(tableRow);
//    }



