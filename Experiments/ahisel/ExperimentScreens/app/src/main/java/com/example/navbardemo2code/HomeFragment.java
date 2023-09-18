package com.example.navbardemo2code;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.navbardemo2code.databinding.ActivityMainBinding;

public class HomeFragment extends Fragment {

    ActivityMainBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ImageButton team_roster = (ImageButton)view.findViewById(R.id.bulls);

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

    }