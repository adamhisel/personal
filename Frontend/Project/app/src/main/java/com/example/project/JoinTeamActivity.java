package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JoinTeamActivity extends AppCompatActivity implements PasswordInputDialogFragment.PasswordInputListener {

    private RequestQueue mQueue;
    private ArrayList<String> teamArr;
    private ArrayList<String> idArr;

    private TextInputLayout name;
    private TextInputLayout number;
    private TextInputLayout position;


    private int teamId;
    private int playerId;
    private int fanId;

    private AutoCompleteTextView teamNameAutoComplete;
    private AutoCompleteTextView typeAutoComplete;
    private AutoCompleteTextView positionAutoComplete;

    private String teamPassword;
    private boolean isPrivate = false;
    private boolean isPlayer = false;
    private String selectedPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_team);

        mQueue = Volley.newRequestQueue(this);

        Button joinButton = findViewById(R.id.btnJoin);

        teamNameAutoComplete = findViewById(R.id.tvTeamName);

        typeAutoComplete = findViewById(R.id.tvUserType);

        positionAutoComplete = findViewById(R.id.tvPosition);

        name = findViewById(R.id.name);
        number = findViewById(R.id.number);
        position = findViewById(R.id.position);

        String[] posArr = new String[5];
        posArr[0] = "PG";
        posArr[1] = "SG";
        posArr[2] = "SF";
        posArr[3] = "PF";
        posArr[4] = "C";

        String[] typeArr = new String[2];
        typeArr[0] = "Player";
        typeArr[1] = "Fan";

        fillTeamList(new TeamListCallback() {

            @Override
            public void onTeamListReceived(ArrayList<String> teamList) {
                teamArr = teamList;

                ArrayAdapter<String> adapter1 = new ArrayAdapter<>(JoinTeamActivity.this, android.R.layout.simple_dropdown_item_1line, teamArr);
                teamNameAutoComplete.setAdapter(adapter1);

            }
        });

        fillIdList(new TeamListCallback() {
            @Override
            public void onTeamListReceived(ArrayList<String> teamList) {
                idArr = teamList;
            }
        });


        teamNameAutoComplete.setOnItemClickListener((parent, view, position, id) -> {
            String selected = (String) parent.getItemAtPosition(position);
            for(int i = 0; i < teamArr.size(); i++){
                if(selected.equals(teamArr.get(i))){
                    teamId = Integer.parseInt(idArr.get(i));
                }
            }
        });


        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(JoinTeamActivity.this, android.R.layout.simple_dropdown_item_1line, typeArr);
        typeAutoComplete.setAdapter(adapter2);


        typeAutoComplete.setOnItemClickListener((parent, view, position, id) -> {
            String selected = (String) parent.getItemAtPosition(position);

            if(selected.equals("Player")){
                isPlayer = true;
                name.setVisibility(View.VISIBLE);
                this.position.setVisibility(View.VISIBLE);
                number.setVisibility(View.VISIBLE);
            }
            else{
                isPlayer = false;
                name.setVisibility(View.INVISIBLE);
                this.position.setVisibility(View.INVISIBLE);
                number.setVisibility(View.INVISIBLE);
            }
        });



        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(JoinTeamActivity.this, android.R.layout.simple_dropdown_item_1line, posArr);
        positionAutoComplete.setAdapter(adapter3);

        positionAutoComplete.setOnItemClickListener((parent, view, position, id) -> {
            String selected = (String) parent.getItemAtPosition(position);
            selectedPos = selected;
        });

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPlayer == true) {
                    boolean isValidName = validateName();
                    boolean isValidNumber = validateNumber();

                    if (!isValidName || !isValidNumber) {
                        return;
                    }
                    postPlayer(new TeamIdCallback() {
                        @Override
                        public void onTeamIdReceived(int id) {
                            playerId = id;
                            getChosenTeam(new TeamStringCallback() {
                                @Override
                                public void onTeamStringReceived(String s) {
                                    teamPassword = s;
                                    showPasswordInputDialog();
                                }
                            });
                        }
                    });
                }
                else{
                    joinTeamUser();
                    Intent intent = new Intent(JoinTeamActivity.this, MainActivity.class);
                    startActivity(intent);
                }

            }
        });

    }

    private void fillTeamList(final TeamListCallback callback) {
        String url = "http://10.0.2.2:8080/teams";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            ArrayList<String> temp1 = new ArrayList<>();

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject team = response.getJSONObject(i);
                                String name = team.getString("teamName");
                                //teamId = team.getInt("id");
                                temp1.add(name);

                            }

                            callback.onTeamListReceived(temp1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mQueue.add(request);
    }

    private void fillIdList(final TeamListCallback callback) {
        String url = "http://10.0.2.2:8080/teams";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            ArrayList<String> temp1 = new ArrayList<>();

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject team = response.getJSONObject(i);
                                String id = team.getString("id");
                                temp1.add(id);
                            }

                            callback.onTeamListReceived(temp1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mQueue.add(request);
    }

    private void getChosenTeam(final TeamStringCallback callback) {
        String url = "http://10.0.2.2:8080/teams/" + teamId;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            isPrivate = response.getBoolean("teamIsPrivate");
                            teamPassword = response.getString("password");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        callback.onTeamStringReceived(teamPassword);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mQueue.add(request);
    }


    private void joinTeamUser(){
        String userId = SharedPrefsUtil.getUserId(this);

        String url = "http://10.0.2.2:8080/User/" + userId + "/teams/" + teamId;

        StringRequest putRequest = new StringRequest(Request.Method.PUT, url,
                response -> {
                    if ("success".equals(response)) {

                    } else if ("failure".equals(response)) {
                    }
                },
                error -> {

                    if (error.networkResponse != null) {
                        String errorMessage = new String(error.networkResponse.data);
                        Log.e("JoinTeam", "Error in request: " + errorMessage);
                    }
                }
        );

        mQueue.add(putRequest);


    }

    private void joinTeamPlayer(){

        String url = "http://10.0.2.2:8080/teams/" + teamId + "/players/" + playerId + "/" + teamPassword;

        StringRequest putRequest = new StringRequest(Request.Method.PUT, url,
                response -> {
                    if ("success".equals(response)) {

                    } else if ("failure".equals(response)) {
                    }
                },
                error -> {

                    if (error.networkResponse != null) {
                        String errorMessage = new String(error.networkResponse.data);
                        Log.e("JoinTeam", "Error in request: " + errorMessage);
                    }
                }
        );

        mQueue.add(putRequest);
    }

    private void joinTeamFan(){

        String url = "http://10.0.2.2:8080/teams/" + teamId + "/fans/" + fanId;

        StringRequest putRequest = new StringRequest(Request.Method.PUT, url,
                response -> {
                    if ("success".equals(response)) {

                    } else if ("failure".equals(response)) {
                    }
                },
                error -> {

                    if (error.networkResponse != null) {
                        String errorMessage = new String(error.networkResponse.data);
                        Log.e("JoinTeam", "Error in request: " + errorMessage);
                    }
                }
        );

        mQueue.add(putRequest);
    }

    private void postPlayer(final TeamIdCallback callback) {
        String url = "http://10.0.2.2:8080/players";

        JSONObject postData = new JSONObject();
        try {
            postData.put("playerName", name.getEditText().getText().toString());
            postData.put("number", number.getEditText().getText().toString());
            postData.put("position", selectedPos);
            postData.put("user_id", "2");


        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            playerId = response.getInt("id");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                        callback.onTeamIdReceived(playerId);
                        Log.d("PostPlayer", "Response received: " + response.toString());

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle the error, e.g., display an error message
                Log.e("PostPlayer", "Error in request: " + error.getMessage());
            }
        });

        mQueue.add(jsonObjectRequest);
    }

    private void postFan(final TeamIdCallback callback) {
        String url = "http://10.0.2.2:8080/fans";

        JSONObject postData = new JSONObject();
        try {
            postData.put("playerName", "Adam Hisel");
            postData.put("user_id", "2");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            fanId = response.getInt("id");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                        callback.onTeamIdReceived(fanId);
                        Log.d("PostFan", "Response received: " + response.toString());

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle the error, e.g., display an error message
                Log.e("PostFan", "Error in request: " + error.getMessage());
            }
        });

        mQueue.add(jsonObjectRequest);
    }


    private Boolean validateName() {
        String tilName = name.getEditText().getText().toString().trim();

        if (tilName.isEmpty()) {
            name.setError("Field cannot be empty");
            return false;
        } else {
            name.setError(null);
            name.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateNumber() {
        String tilNumber = number.getEditText().getText().toString().trim();


        if (tilNumber.isEmpty()) {
            number.setError("Field cannot be empty");
            return false;
        }
        if (!isInteger(tilNumber)){
            number.setError("Field has to be a number");
            return false;
        }
        else{
            number.setError(null);
            number.setErrorEnabled(false);
            return true;
        }

    }

    private static boolean isInteger (String str){
        try {
            // Attempt to parse the string as an integer
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            // If an exception is thrown, the string is not an integer
            return false;
        }
    }

    public interface TeamListCallback {
        void onTeamListReceived(ArrayList<String> teamList);
    }

    public interface TeamIdCallback {
        void onTeamIdReceived(int id);
    }

    public interface TeamStringCallback {
        void onTeamStringReceived(String s);
    }



    private void showPasswordInputDialog() {
        PasswordInputDialogFragment passwordDialog = new PasswordInputDialogFragment();
        passwordDialog.setListener(this);
        passwordDialog.show(getSupportFragmentManager(), "passwordDialog");
    }

    @Override
    public void onPasswordEntered(String password) {
        if(password.equals(this.teamPassword)){
            Toast.makeText(this, "Password Is Correct! ", Toast.LENGTH_SHORT).show();
            joinTeamPlayer();
            joinTeamUser();
            Intent intent = new Intent(JoinTeamActivity.this, MainActivity.class);
            startActivity(intent);
        }
        else{
            Toast.makeText(this, "Password Is Incorrect! Please retry", Toast.LENGTH_SHORT).show();
            showPasswordInputDialog();
        }
        ;
    }


}