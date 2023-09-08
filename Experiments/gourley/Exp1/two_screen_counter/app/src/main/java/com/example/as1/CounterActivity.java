package com.example.as1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CounterActivity extends AppCompatActivity {

    Button makeBtn;
    Button missBtn;
    Button backBtn;
    TextView numberMakesTxt;
    TextView numberMissesTxt;

    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);

        makeBtn = findViewById(R.id.makeBtn);
        missBtn = findViewById(R.id.missBtn);
        backBtn = findViewById(R.id.backBtn);
        numberMakesTxt = findViewById(R.id.numberMakes);
        numberMissesTxt = findViewById(R.id.numberMisses);

        makeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                numberMakesTxt.setText(String.valueOf(++counter));
            }
        });

        missBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { numberMissesTxt.setText(String.valueOf(++counter)); }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(CounterActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }
}