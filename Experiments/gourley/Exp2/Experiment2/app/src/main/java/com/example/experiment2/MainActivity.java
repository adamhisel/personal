package com.example.experiment2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private EditText etBaseAmount;
    private SeekBar seekBarTip;
    private TextView tvTipPercentLabel;
    private TextView tvTipAmount;
    private TextView tvTotalAmount;

    private static final String TAG = "Main Activity";
    private static final Integer INITIAL_TIP_PERCENT = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etBaseAmount = findViewById(R.id.etBaseAmount);
        seekBarTip = findViewById(R.id.seekBarTip);
        tvTipPercentLabel = findViewById(R.id.tvTipPercentLabel);
        tvTipAmount = findViewById(R.id.tvTipAmount);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);

        seekBarTip.setProgress(INITIAL_TIP_PERCENT);
        tvTipPercentLabel.setText(String.valueOf(INITIAL_TIP_PERCENT) + '%');


        seekBarTip.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.i(TAG, "onProgressChanged " + progress);
                tvTipPercentLabel.setText(String.valueOf(progress) + '%');
                computeTipAndTotal();

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        etBaseAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                Log.i(TAG, "afterTextChanged " + s);
                computeTipAndTotal();
            }
        });

    }

    private void computeTipAndTotal() {
        if (etBaseAmount.getText().toString().isEmpty()){
            tvTipAmount.setText("");
            tvTotalAmount.setText("");
            return;
        }
        double baseAmount = Double.parseDouble(etBaseAmount.getText().toString());
        int tipPercent = seekBarTip.getProgress();

        double tipAmount = (baseAmount * tipPercent) / 100;
        double totalAmount = baseAmount + tipAmount;

        tvTipAmount.setText(String.format("%.2f", tipAmount));
        tvTotalAmount.setText(String.format("%.2f", totalAmount));
    }
}