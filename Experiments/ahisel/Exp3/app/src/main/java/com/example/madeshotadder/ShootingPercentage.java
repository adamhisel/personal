package com.example.madeshotadder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ShootingPercentage extends AppCompatActivity {

    TextView shootingPercent;
    double made;

    double miss;

    double total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shooting_percentage);
        Intent i = getIntent();
        shootingPercent = findViewById(R.id.percent);

        made = i.getIntExtra("madeNum", 0);
        miss = i.getIntExtra("missNum",0);
        total = (made/miss) * 100;
        shootingPercent.setText(String.valueOf(total) + "%");


    }
}