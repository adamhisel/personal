package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.example.project.databinding.FragmentGameBinding;

/**
 * Fragment representing the game options in the application.
 * Allows the user to navigate to activities where they can create a game
 * or view an active game via WebSocket.
 *
 * @author Jagger Gourley
 */
public class GameFragment extends Fragment {
    private static final String BASE_URL = "http://coms-309-018.class.las.iastate.edu:8080/";
    private static final String LOCAL_URL = "http://10.0.2.2:8080/";
    private static RequestQueue mQueue;
    private FragmentGameBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGameBinding.inflate(inflater, container, false);

        setupButtonListeners();
        return binding.getRoot();
    }

    // Sets up click listeners for the buttons within the fragment.
    private void setupButtonListeners() {
        if (SharedPrefsTeamUtil.getIsCoach(getContext()).equals("true")) {
            binding.btnBegin.setVisibility(View.VISIBLE);
            binding.btnBegin.setOnClickListener(v -> {
                Intent intent = new Intent(requireActivity(), GameActivity.class);
                startActivity(intent);
            });
        }
        else{
            binding.btnBegin.setVisibility(View.GONE);
        }


        binding.btnView.setOnClickListener(view -> {
            Intent intent = new Intent(requireActivity(), GameWebsocketActivity.class);
            startActivity(intent);
        });
    }

    // Called when the view previously created by onCreateView has been detached from the fragment.
    // The binding is cleaned up to prevent memory leaks.
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Nullify the binding to avoid memory leaks
        binding = null;
    }
}