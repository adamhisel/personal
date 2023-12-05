package com.example.project;

import okhttp3.MediaType;

public class DataPart {
    private String fileName;
    private byte[] data;
    private String mimeType;

    public DataPart(String fileName, byte[] data, String mimeType) {
        this.fileName = fileName;
        this.data = data;
        this.mimeType = mimeType;
    }

    public String getFileName() {
        return fileName;
    }

    public byte[] getData() {
        return data;
    }

    public MediaType getMediaType() {
        return MediaType.parse(mimeType);
    }
}