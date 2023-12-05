package com.example.project;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.IOException;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okio.Buffer;

public class MultipartRequest extends Request<String> {

    private final Response.Listener<String> mListener;
    private final Map<String, String> mHeaders;
    private final Map<String, DataPart> mByteData;

    public MultipartRequest(String url, Map<String, String> headers, Map<String, DataPart> byteData,
                            Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);
        this.mListener = listener;
        this.mHeaders = headers;
        this.mByteData = byteData;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return mHeaders != null ? mHeaders : super.getHeaders();
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        return Response.success("Uploaded", HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
    }

    @Override
    public byte[] getBody() {
        Buffer buffer = new Buffer();
        MultipartBody.Builder multipartBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        try {
            for (Map.Entry<String, DataPart> entry : mByteData.entrySet()) {
                DataPart dataPart = entry.getValue();
                multipartBuilder.addFormDataPart(entry.getKey(), dataPart.getFileName(),
                        RequestBody.create(dataPart.getMediaType(), dataPart.getData()));
            }

            multipartBuilder.build().writeTo(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return buffer.readByteArray();
    }

    @Override
    public String getBodyContentType() {
        return "multipart/form-data; boundary=" + "YOUR_BOUNDARY_HERE";
    }
}