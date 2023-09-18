package com.example.navbardemo2code;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.navbardemo2code.databinding.ActivityMainBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TeamRosterSubFragmentHeat#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TeamRosterSubFragmentHeat extends Fragment {
    ActivityMainBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        replaceFrag(new HomeFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            if(item.getItemId() == R.id.bulls){
                replaceFrag(new TeamRosterSubFragmentBulls());
            }
            else if(item.getItemId() == R.id.heat) {
                replaceFrag(new TeamRosterSubFragmentHeat());
            }


            return true;

        });
    }

    private void replaceFrag(Fragment frag){

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, frag);
        fragmentTransaction.commit();

    }
}


