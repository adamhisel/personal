package com.example.project;

public class Shots {
    private boolean made;
    private int value;
    private float xCoord;
    private float yCoord;

    public Shots(boolean made, int value, float xCoord, float yCoord) {
        this.made = made;
        this.value = value;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
    }

    public boolean isMade() {
        return made;
    }

    public void setMade(boolean made) {
        this.made = made;
    }

    @Override
    public String toString() {
        return "Shots{" +
                "made=" + made +
                ", value=" + value +
                ", xCoord=" + xCoord +
                ", yCoord=" + yCoord +
                '}';
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public float getxCoord() {
        return xCoord;
    }

    public void setxCoord(float xCoord) {
        this.xCoord = xCoord;
    }

    public float getyCoord() {
        return yCoord;
    }

    public void setyCoord(float yCoord) {
        this.yCoord = yCoord;
    }
}
