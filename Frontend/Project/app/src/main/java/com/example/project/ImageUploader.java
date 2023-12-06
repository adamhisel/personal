package com.example.project;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class ImageUploader {
    private static final String BASE_URL = "http://coms-309-018.class.las.iastate.edu:8080/";
    private static final String LOCAL_URL = "http://10.0.2.2:8080/";
    public void uploadImage(Context context, int userId, Bitmap imageBitmap) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        Map<String, DataPart> params = new HashMap<>();
        params.put("file", new DataPart("image.png", getFileDataFromBitmap(imageBitmap), "image/png"));

        MultipartRequest multipartRequest = new MultipartRequest(
                LOCAL_URL + userId + "/upload",
                null,
                params,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(context, "Image uploaded successfully!", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(context, "Error uploading image", Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue.add(multipartRequest);
    }

    private byte[] getFileDataFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}