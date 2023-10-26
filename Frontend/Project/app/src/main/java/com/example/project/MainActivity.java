package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;


import com.example.project.databinding.ActivityMainBinding;

/**
 * @author Adam Hisel
 * Activity that manages the navigayion bar at the bottom of the fragements.
 * Sets the default fragment to open to the home page when app is loaded.
 */
public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFrag(new HomeFragment());
        /*if (SharedPrefsUtil.getUserType(this).equals("coach")){
            replaceFrag(new HomeFragmentCoach());
        }
        else{
            replaceFrag(new HomeFragmentPlayer());
       }*/

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            if(item.getItemId() == R.id.home){
                replaceFrag(new HomeFragment());
//                if (SharedPrefsUtil.getUserType(this).equals("coach")){
//                    replaceFrag(new HomeFragmentCoach());
//                }
//                else{
//                    replaceFrag(new HomeFragmentPlayer());
//                }
            }
            else if(item.getItemId() == R.id.workout) {
                replaceFrag(new WorkoutFragment());
            }
            else if(item.getItemId() == R.id.game) {
                replaceFrag(new GameFragment());
            }
            else if(item.getItemId() == R.id.stats) {
                replaceFrag(new StatsFragment());
            }
            else if(item.getItemId() == R.id.profile) {
                replaceFrag(new ProfileFragment());
            }

            return true;

        });
    }

    private void replaceFrag(Fragment frag){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, frag);
        fragmentTransaction.commit();
    }
}

