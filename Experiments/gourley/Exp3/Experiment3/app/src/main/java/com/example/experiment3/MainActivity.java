package com.example.experiment3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText etNumber;
    Button btnSecondScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etNumber = findViewById(R.id.etNumber);
        btnSecondScreen = findViewById(R.id.btnSecondScreen);

        btnSecondScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = etNumber.getText().toString();
                if (input.isEmpty()) {
                    input = "0";
                }
                int number = Integer.parseInt(input);
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                intent.putExtra("EXTRA_NUMBER", number);
                startActivity(intent);
            }
        });

    }
}