package com.example.madeshotadder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ShootingPercentage extends AppCompatActivity {

    TextView shootingpercent;
    double made;

    double miss;

    double total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shooting_percentage);
        Intent i = getIntent();

        made = Integer.parseInt(i.getStringExtra("madeNum"));
        miss = Integer.parseInt(i.getStringExtra("missNum"));
        total = (made/miss) * 100;
        shootingpercent.setText(String.valueOf(total));


    }
}