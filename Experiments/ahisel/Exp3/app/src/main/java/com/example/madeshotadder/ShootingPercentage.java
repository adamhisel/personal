package com.example.madeshotadder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ShootingPercentage extends AppCompatActivity {

    TextView shootingPercent;
    double made;

    double miss;

    double total;

    Button b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shooting_percentage);
        Intent i = getIntent();
        shootingPercent = findViewById(R.id.percent);
        b = findViewById(R.id.button);

        made = i.getIntExtra("madeNum", 0);
        miss = i.getIntExtra("missNum",0);
        total = (made/(miss+made)) * 100;
        shootingPercent.setText(String.valueOf(total) + "%");


        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view){

                Intent i = new Intent(ShootingPercentage.this, MainActivity.class);
                startActivity(i);
            }


        });


    }
}