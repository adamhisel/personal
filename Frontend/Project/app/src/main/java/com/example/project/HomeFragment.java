package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.project.databinding.ActivityMainBinding;

import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.ArrayList;

public class HomeFragment extends Fragment /*implements View.OnClickListener*/{

    ActivityMainBinding binding;

    private TextView msg;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ImageButton team_roster = (ImageButton)view.findViewById(R.id.bulls);
        ImageButton team_roster2= (ImageButton)view.findViewById(R.id.heat);
        Button addTeam = (Button)view.findViewById(R.id.button3);
/*
        team_roster.setOnClickListener(this);
        team_roster2.setOnClickListener(this);
*/

        addTeam.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v)
               {
                   Intent intent = new Intent(getActivity(), AddTeamActivity.class);
                   startActivity(intent);
               }

        });
        team_roster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fr = fragmentManager.beginTransaction();
                fr.replace(R.id.frame_layout, new TeamRosterSubFragmentBulls());
                fr.commit();
            }
        });

        return view;
    }

    /*
    @Override
    public void onClick(View view) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fr = fragmentManager.beginTransaction();
        switch (view.getId()){
            case R.id.bulls:
                fr.replace(R.id.frame_layout, new TeamRosterSubFragmentBulls());
                break;
            case R.id.heat:
                fr.replace(R.id.frame_layout, new TeamRosterSubFragmentHeat());
                break;
        }
        fr.replace(R.id.frame_layout, new TeamRosterSubFragmentBulls());
        fr.commit();
    }
*/
}
