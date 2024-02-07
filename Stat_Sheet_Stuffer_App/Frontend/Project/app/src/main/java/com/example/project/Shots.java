package com.example.project;

/**
 * Represents a shot attempt in a basketball game, with details about the shot result,
 * point value, and location coordinates where the shot was taken.
 *
 * @author Jagger Gourley
 */
public class Shots {
    private boolean made;
    private int value;
    private float xCoord;
    private float yCoord;

    /**
     * Constructs a Shots instance with detailed information about a shot attempt.
     *
     * @param made   true if the shot was made, false otherwise
     * @param value  the point value of the shot
     * @param xCoord the x-coordinate of the shot location
     * @param yCoord the y-coordinate of the shot location
     */
    public Shots(boolean made, int value, float xCoord, float yCoord) {
        this.made = made;
        this.value = value;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
    }

    /**
     * Provides a string representation of the shot attempt, including its result,
     * value, and location coordinates.
     *
     * @return a string representation of the shot
     */
    @Override
    public String toString() {
        return "Shots{" +
                "made=" + made +
                ", value=" + value +
                ", xCoord=" + xCoord +
                ", yCoord=" + yCoord +
                '}';
    }

    /**
     * Checks if the shot was made.
     *
     * @return true if the shot was made, false otherwise
     */
    public boolean isMade() {
        return made;
    }

    // Simple getters and setters

    public void setMade(boolean made) {
        this.made = made;
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
