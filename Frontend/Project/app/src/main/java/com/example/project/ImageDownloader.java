package com.example.project;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

public class ImageDownloader {
    private static final String BASE_URL = "http://coms-309-018.class.las.iastate.edu:8080/";
    private static final String LOCAL_URL = "http://10.0.2.2:8080/";

    public void downloadUserImage(Context context, int userId, ImageView imageView) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        String imageUrl = BASE_URL + "getimage/user/" + userId;

        ImageRequest imageRequest = new ImageRequest(
                imageUrl,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        imageView.setImageBitmap(response);
                    }
                },
                0, 0,
                null,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       // Toast.makeText(context, "Error downloading image", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        requestQueue.add(imageRequest);
    }

    public void downloadTeamImage(Context context, int teamId, ImageView imageView) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        String imageUrl = BASE_URL + "getimage/team/" + teamId;

        ImageRequest imageRequest = new ImageRequest(
                imageUrl,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        imageView.setImageBitmap(response);
                    }
                },
                0, 0,
                null,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       // Toast.makeText(context, "Error downloading image", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        requestQueue.add(imageRequest);
    }
}
