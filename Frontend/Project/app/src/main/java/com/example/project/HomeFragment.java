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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_home_coach, container, false);

        mQueue = Volley.newRequestQueue(requireContext());

        ll = view.findViewById(R.id.linearLayout);

        ImageButton addTeam = view.findViewById(R.id.plus);



        TextView header = view.findViewById(R.id.header);

        header.setText("Hello, " + SharedPrefsUtil.getUserName(requireContext()));

        jsonParseArray();
        addTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddTeamActivity.class);
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
    public void jsonParseArray() {
        String url = "https://5a183357-b941-4d66-b21b-3b4961c7a63e.mock.pstmn.io/teams";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject team = response.getJSONObject(i);

                        Button button = new Button(requireContext());

                        String teamName = team.getString("teamName");
                        int tag = team.getInt("id");

                        button.setText(teamName);
                        button.setTag(tag);
                        button.setTextSize(25);

                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                int teamId = tag;
                                Intent intent = new Intent(getActivity(), TeamRosterCoach.class);
                                intent.putExtra("teamId", teamId);
                                startActivity(intent);
                            }
                        });

                        ll.addView(button, ll.getChildCount()-4);


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
