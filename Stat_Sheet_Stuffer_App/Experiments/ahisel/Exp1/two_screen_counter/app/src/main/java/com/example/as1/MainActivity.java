package com.example.as1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button button;

    Button russian;
    Button spanish;
    Button french;
    Button dutch;
    Button japanese;
    Button german;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        russian = findViewById(R.id.TopLButton);
        spanish = findViewById(R.id.MiddleTButton);
        french = findViewById(R.id.TopRButton);
        dutch = findViewById(R.id.BottomLButton);
        japanese = findViewById(R.id.MiddleBButton);
        german = findViewById(R.id.BottomRButton);

        russian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                Intent intent = new Intent(MainActivity.this, RussianActivity.class);
                startActivity(intent);
            }
        });

        spanish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, SpanishActivity.class);
                startActivity(intent);
            }
        });

        french.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, FrenchActivity.class);
                startActivity(intent);
            }
        });

        dutch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, DutchActivity.class);
                startActivity(intent);
            }
        });

        japanese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, JapaneseActivity.class);
                startActivity(intent);
            }
        });

        german.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, GermanActivity.class);
                startActivity(intent);
            }
        });


    }


}