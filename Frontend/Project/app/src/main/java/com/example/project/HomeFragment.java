package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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

public class HomeFragment extends Fragment implements View.OnClickListener{

    ActivityMainBinding binding;

    ArrayList<Button> dynamicButtons;

    private RequestQueue mQueue;
    LinearLayout ll;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mQueue = Volley.newRequestQueue(requireContext());

        ll = view.findViewById(R.id.linearLayout);

        Button addTeam = (Button)view.findViewById(R.id.addTeam);

        jsonParseArray();

        for(int i = 0; i < dynamicButtons.size(); i++){
            dynamicButtons.get(i).setOnClickListener(this);
        }

        addTeam.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v)
               {
                   Intent intent = new Intent(getActivity(), AddTeamActivity.class);
                   startActivity(intent);
               }

        });

        return view;
    }

    public void jsonParseArray() {
        String url = "https://5a183357-b941-4d66-b21b-3b4961c7a63e.mock.pstmn.io/teams/";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    dynamicButtons = new ArrayList<>();
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject team = response.getJSONObject(i);

                        Button button = new Button(requireContext());

                        String teamName = team.getString("teamName");

                        button.setText(teamName);
                        button.setTextSize(25);

                        ll.addView(button, ll.getChildCount() - 1);

                        dynamicButtons.add(button);
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

}
