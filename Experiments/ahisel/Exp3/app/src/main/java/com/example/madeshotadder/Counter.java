package com.example.madeshotadder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Counter extends AppCompatActivity {

    Button make;

    Button miss;

    Button finish;

    TextView makeCount;

    TextView missedCount;

    int counter1 = 0;
    int counter2 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);

        make = findViewById(R.id.makeButton);
        miss = findViewById(R.id.missButton);
        finish = findViewById(R.id.finishButton);
        makeCount = findViewById(R.id.madeCount);
        missedCount = findViewById(R.id.missCount);



        make.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                makeCount.setText(String.valueOf(++counter1));

            }

        });

        miss.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                missedCount.setText(String.valueOf(++counter2));
            }
        });

        finish.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view){
                Intent i = new Intent(Counter.this, ShootingPercentage.class);
                i.putExtra("madeNum", counter1);
                i.putExtra("missNum", counter2);
                startActivity(i);
            }


        });





    }
}