package com.example.project;

import android.content.Intent;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


public class ProfileFragment extends Fragment {

    private static RequestQueue mQueue;

    public ProfileFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        mQueue = Volley.newRequestQueue(getActivity());
        TextView tvUsername = rootView.findViewById(R.id.tvUsername);
        TextView tvEmail = rootView.findViewById(R.id.tvEmail);
        TextView tvPhoneNumber = rootView.findViewById(R.id.tvPhoneNumber);
        Button btnLogout = rootView.findViewById(R.id.btnLogout);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle logout logic here.
                // For example, clear any saved user data redirect to LoginActivity.
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });


        return rootView;
    }

}



