package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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

import java.util.ArrayList;

/**
 * @author Adam Hisel
 * Home fragment in the navigation bar that contains options to create a team, join a team
 * or click into the team a user is on or follows.
 */
public class HomeFragment extends Fragment {

    private RequestQueue mQueue;

    private LinearLayout ll;

    private Bundle savedInstance;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        savedInstance = savedInstanceState;
        View view = inflater.inflate(R.layout.fragment_home_coach, container, false);

        mQueue = Volley.newRequestQueue(requireContext());

        ll = view.findViewById(R.id.linearLayout);

        ImageButton addTeam = view.findViewById(R.id.plus);

        ImageButton joinTeam = view.findViewById(R.id.join);

        //TextView header = view.findViewById(R.id.header);

        //header.setText("Hello, " + SharedPrefsUtil.getFirstName(requireContext()));

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
    public void displayTeamButtons() {
        String url = "http://10.0.2.2:8080/users/" + SharedPrefsUtil.getUserId(getContext());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                        JSONArray teams = response.getJSONArray("teams");

                        ArrayList<String> teamList = new ArrayList<>();
                        ArrayList<Integer> teamIds = new ArrayList<>();

                        for (int i = 0; i < teams.length(); i++) {
                            JSONObject team = teams.getJSONObject(i);
                            teamList.add(team.getString("teamName"));
                            teamIds.add(Integer.valueOf(team.getString("id")));
                        }

                        for(int j =0; j< teamList.size(); j++) {

                            Button button = new Button(requireContext());

                            String teamName = teamList.get(j);
                            int id = teamIds.get(j);

                            button.setText(teamName);
                            button.setTag(id);
                            button.setTextSize(25);

                            button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (savedInstance == null) {
                                        TeamRosterFragment fragment = new TeamRosterFragment();
                                        SharedPrefsUtil.saveTeamData(getContext(), teamName, String.valueOf(id));

                                        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                                        transaction.replace(R.id.linear, fragment);
                                        transaction.addToBackStack(null);
                                        transaction.commit();

                                    }
                                }
                            });

                            ll.addView(button, ll.getChildCount() - teams.length()-1);

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


}
