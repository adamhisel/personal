package com.example.project;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class TeamRosterSubFragmentBulls extends Fragment {

    TableLayout tl;
    private RequestQueue mQueue;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_team_roster_sub, container, false);

        tl =  view.findViewById(R.id.tableLayout);

        Button add = view.findViewById(R.id.button);

        mQueue = Volley.newRequestQueue(requireContext());

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditRosterActivity.class);
                startActivity(intent);
            }
        });

        jsonParseArray();

        return view;
    }

    public void jsonParseArray() {
        String url = "https://5a183357-b941-4d66-b21b-3b4961c7a63e.mock.pstmn.io/roster/";

        // "https://jsonplaceholder.typicode.com/users";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject user = response.getJSONObject(i);

                                /*
                                String id = user.getString("id");
                                String name = user.getString("name");
                                String username = user.getString("username");
                                String email = user.getString("email");
                                */

                        String id = user.getString("number");
                        String name = user.getString("name");
                        String username = user.getString("position");
                        String email = user.getString("height");

                        android.widget.TableRow.LayoutParams trparams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);

                        TableRow tableRow = new TableRow(requireContext());

                        Resources resources = getResources();
                        Drawable drawable = resources.getDrawable(R.drawable.textbox_borders);

                        TextView textView = new TextView(requireContext());
                        textView.setPadding(10, 10, 10, 10);
                        textView.setLayoutParams(trparams);
                        textView.setTextSize(25);
                        textView.setTypeface(null, android.graphics.Typeface.BOLD);
                        textView.setBackground(drawable);
                        textView.setText(id);
                        tableRow.addView(textView);


                        TextView textView2 = new TextView(requireContext());
                        textView2.setPadding(10, 10, 10, 10);
                        textView2.setLayoutParams(trparams);
                        textView2.setTextSize(25);
                        textView2.setTypeface(null, android.graphics.Typeface.BOLD);
                        textView2.setBackground(drawable);
                        textView2.setText(name);
                        tableRow.addView(textView2);

                        TextView textView3 = new TextView(requireContext());
                        textView3.setPadding(10, 10, 10, 10);
                        textView3.setLayoutParams(trparams);
                        textView3.setTextSize(25);
                        textView3.setTypeface(null, android.graphics.Typeface.BOLD);
                        textView3.setBackground(drawable);
                        textView3.setText(username);
                        tableRow.addView(textView3);

                        TextView textView4 = new TextView(requireContext());
                        textView4.setPadding(10, 10, 10, 10);
                        textView4.setLayoutParams(trparams);
                        textView4.setTextSize(25);
                        textView4.setTypeface(null, android.graphics.Typeface.BOLD);
                        textView4.setBackground(drawable);
                        textView4.setText(email);
                        tableRow.addView(textView4);

                        tl.addView(tableRow);
                        //mTextViewResult.append(id + ", " + name + ", " + username + "\n\n");
                    }

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

}
