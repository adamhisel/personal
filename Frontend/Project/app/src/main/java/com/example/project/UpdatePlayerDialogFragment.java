package com.example.project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputLayout;

/**
 * This class is used to create a password dialog pop up when called upon.
 *
 * @author Adam Hisel
 */
public class UpdatePlayerDialogFragment extends DialogFragment {

    private AutoCompleteTextView positionAutoComplete;

    private TextInputLayout number;
    private TextInputLayout position;

    private UpdatePlayerInputListener listener;

    private String selectedPos;


    public UpdatePlayerDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.update_player_dialog, container, false);

        Button updateButton = view.findViewById(R.id.btnUpdate);
        positionAutoComplete = view.findViewById(R.id.tvPosition);

        number = view.findViewById(R.id.number);
        position = view.findViewById(R.id.position);

        String[] posArr = new String[5];
        posArr[0] = "PG";
        posArr[1] = "SG";
        posArr[2] = "SF";
        posArr[3] = "PF";
        posArr[4] = "C";

        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, posArr);
        positionAutoComplete.setAdapter(adapter3);

        positionAutoComplete.setOnItemClickListener((parent, v, position, id) -> {
            String selected = (String) parent.getItemAtPosition(position);
            selectedPos = selected;
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num = number.getEditText().getText().toString();
                listener.onUpdate(num, selectedPos, "1");
                dismiss();
            }
        });

        return view;
    }

    /**
     * listens for specific events
     * @param listener
     */
    public void setListener(UpdatePlayerInputListener listener) {
        this.listener = listener;
    }

    /**
     * listens for password input
     */
    public interface UpdatePlayerInputListener {
        void onUpdate(String number, String pos, String pid);
    }
}