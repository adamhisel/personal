package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class HomeFragmentCoach extends Fragment {

    ActivityMainBinding binding;

    ArrayList<Button> dynamicButtons;

    private RequestQueue mQueue;
    LinearLayout ll;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_home_coach, container, false);

        mQueue = Volley.newRequestQueue(requireContext());

        ll = view.findViewById(R.id.linearLayout);

        Button addTeam = (Button)view.findViewById(R.id.addTeam);

        Button findTeam = (Button)view.findViewById(R.id.findTeam);

        TextView header = (TextView)view.findViewById(R.id.header);

        header.setText("Hello, " + SharedPrefsUtil.getUserName(requireContext()));

        //jsonParseArray();
        addTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddTeamActivity.class);
                startActivity(intent);
            }
        });

        findTeam.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                Intent intent = new Intent(getActivity(), TeamRosterCoach.class);
                startActivity(intent);
            }

        });

        return view;
/*
        for(int i = 0; i < dynamicButtons.size(); i++) {

            dynamicButtons.get(i).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fr = fragmentManager.beginTransaction();

                TeamRosterSubFragment fragment = new TeamRosterSubFragment();

                Button clickedButton = (Button) view;

                Bundle bundle = new Bundle();
                bundle.putString("teamName", (String) clickedButton.getText());

                fragment.setArguments(bundle);

                fr.replace(R.id.frame_layout, fragment);
                fr.commit();
            }
            });

         */
    }

    public void jsonParseArray() {
        String url = "http://coms-309-018.class.las.iastate.edu:8080/teams";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    //dynamicButtons = new ArrayList<>();
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject team = response.getJSONObject(i);

                        Button button = new Button(requireContext());

                        String teamName = team.getString("teamName");

                        button.setText(teamName);
                        button.setTextSize(25);

                        ll.addView(button, ll.getChildCount() - 1);

                        //dynamicButtons.add(button);
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
