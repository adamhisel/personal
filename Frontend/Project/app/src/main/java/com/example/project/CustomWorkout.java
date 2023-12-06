package com.example.project;

public class CustomWorkout {
    private int customWoutId;
    private List<Coordinate> coords; // Change Points to Coordinate to match your frontend structure
    private String workoutName;
    private int userId;

    public CustomWorkout(int customWoutId, String workoutName, List<Coordinate> coords) {
        this.customWoutId = customWoutId;
        this.workoutName = workoutName;
        this.coords = coords;
    }

    // Getters and Setters
    public int getCustomWoutId() {
        return customWoutId;
    }

    public void setCustomWoutId(int customWoutId) {
        this.customWoutId = customWoutId;
    }

    public List<Coordinate> getCoords() {
        return coords;
    }

    public void setCoords(List<Coordinate> coords) {
        this.coords = coords;
    }

    public String getWorkoutName() {
        return workoutName;
    }

    public void setWorkoutName(String workoutName) {
        this.workoutName = workoutName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

}
