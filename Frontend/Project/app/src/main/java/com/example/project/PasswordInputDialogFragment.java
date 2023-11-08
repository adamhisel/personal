package com.example.project;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * @author Adam Hisel
 * This class is used to create a password dialog pop up when called upon
 */
public class PasswordInputDialogFragment extends DialogFragment {

    private EditText passwordEditText;
    private Button okButton;
    private PasswordInputListener listener;

    public PasswordInputDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.password_dialog_join, container, false);

        passwordEditText = view.findViewById(R.id.passwordEditText);
        okButton = view.findViewById(R.id.okButton);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = passwordEditText.getText().toString();
                listener.onPasswordEntered(password);
                dismiss();
            }
        });

        return view;
    }

    /**
     * listens for specific events
     * @param listener
     */
    public void setListener(PasswordInputListener listener) {
        this.listener = listener;
    }

    /**
     * listens for password input
     */
    public interface PasswordInputListener {
        void onPasswordEntered(String password);
    }
}