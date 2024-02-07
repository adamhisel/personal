package com.example.project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputLayout;

/**
 * This class is used to create a password dialog pop up when called upon.
 *
 * @author Adam Hisel
 */
public class ConfirmDialogFragment extends DialogFragment {

    private ConfirmDialogFragment.ConfirmInputListener listener;


    public ConfirmDialogFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.confirm_dialog, container, false);


        Button noButton = view.findViewById(R.id.btnNo);
        Button yesButton = view.findViewById(R.id.btnYes);


        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onConfirm(true);
                dismiss();
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return view;
    }





    /**
     * listens for specific events
     * @param listener
     */
    public void setListener(ConfirmInputListener listener) {
        this.listener = listener;
    }

    /**
     * listens for password input
     */
    public interface ConfirmInputListener {
        void onConfirm(boolean confirm);
    }
}