package com.example.project;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author Adam Hisel
 * Fragment that displays a teams overall stats from previous games. Also has a
 * leaderboard that displays the teams leading scorers.
 */
public class StatsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stats, container, false);




        return view;

    }




}
