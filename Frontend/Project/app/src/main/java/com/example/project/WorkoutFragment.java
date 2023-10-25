package com.example.project;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.example.project.databinding.FragmentProfileBinding;
import com.example.project.databinding.FragmentWorkoutBinding;


public class WorkoutFragment extends Fragment {

    private static final String BASE_URL = "http://coms-309-018.class.las.iastate.edu:8080/";
    private static final String LOCAL_URL = "http://10.0.2.2:8080/";
    private FragmentWorkoutBinding binding;
    private static RequestQueue mQueue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentWorkoutBinding.inflate(inflater, container, false);

        setupButtonListeners();

        return binding.getRoot();
    }

    private void setupButtonListeners() {
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireActivity(), WorkoutActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}