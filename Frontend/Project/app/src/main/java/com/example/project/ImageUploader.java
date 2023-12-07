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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ImageUploader {
    private static final String BASE_URL = "http://coms-309-018.class.las.iastate.edu:8080/";
    private static final String LOCAL_URL = "http://10.0.2.2:8080/";

    public void uploadUserImage(Context context, int userId, Bitmap imageBitmap) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        Bitmap circularBitMap = ImageHelper.getCircularBitmap(imageBitmap);

        File imageFile = bitmapToFile(context, circularBitMap, "image.png");

        if (imageFile != null && imageFile.exists()) {
            Map<String, String> stringParams = new HashMap<>();
            stringParams.put("userId", String.valueOf(userId));

            MultipartRequest multipartRequest = new MultipartRequest(
                    BASE_URL + "user/" + userId + "/upload",
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(context, "Error uploading image", Toast.LENGTH_SHORT).show();
                        }
                    },
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(context, "Image uploaded successfully!", Toast.LENGTH_SHORT).show();
                        }
                    },
                    imageFile,
                    stringParams
            );

            requestQueue.add(multipartRequest);
        } else {
            Toast.makeText(context, "Image file not found!", Toast.LENGTH_SHORT).show();
        }
    }

    public void uploadTeamImage(Context context, int teamId, Bitmap imageBitmap) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        Bitmap circularBitMap = ImageHelper.getCircularBitmap(imageBitmap);

        File imageFile = bitmapToFile(context, circularBitMap, "image.png");

        if (imageFile != null && imageFile.exists()) {
            Map<String, String> stringParams = new HashMap<>();
            stringParams.put("teamId", String.valueOf(teamId));

            MultipartRequest multipartRequest = new MultipartRequest(
                    BASE_URL + "team/" + teamId + "/upload",
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(context, "Error uploading image", Toast.LENGTH_SHORT).show();
                        }
                    },
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(context, "Image uploaded successfully!", Toast.LENGTH_SHORT).show();
                        }
                    },
                    imageFile,
                    stringParams
            );

            requestQueue.add(multipartRequest);
        } else {
            Toast.makeText(context, "Image file not found!", Toast.LENGTH_SHORT).show();
        }
    }

    public static File bitmapToFile(Context context, Bitmap bitmap, String fileName) {

        File imagesDir = getImagesDirectory(context);


        File imageFile = new File(imagesDir, fileName);


        try (FileOutputStream fos = new FileOutputStream(imageFile)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            return imageFile;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static File getImagesDirectory(Context context) {
        File mediaStorageDir = new File(context.getFilesDir(), "images");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        return mediaStorageDir;
    }
}
