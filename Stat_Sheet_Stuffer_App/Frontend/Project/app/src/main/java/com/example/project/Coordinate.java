package com.example.project;

public class Coordinate {
    private int xCoord;
    private int yCoord;

    public Coordinate(int xCoord, int yCoord) {
        this.xCoord = xCoord;
        this.yCoord = yCoord;
    }

    @Override
    public String toString() {
        return "Coordinate{" +
                "xCoord=" + xCoord +
                ", yCoord=" + yCoord +
                '}';
    }

    // Getters and Setters
    public int getXCoord() {
        return xCoord;
    }

    public void setXCoord(int xCoord) {
        this.xCoord = xCoord;
    }

    public int getYCoord() {
        return yCoord;
    }

    public void setYCoord(int yCoord) {
        this.yCoord = yCoord;
    }

}
