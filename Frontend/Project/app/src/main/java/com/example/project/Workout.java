package com.example.project;

import java.util.List;

public class Workout {
    private int workoutId;
    private List<Shots> shots;

    // Constructor
    public Workout(int workoutId, List<Shots> shots) {
        this.workoutId = workoutId;
        this.shots = shots;
    }

    // Getters and setters
    public int getWorkoutId() {
        return workoutId;
    }

    public void setWorkoutId(int workoutId) {
        this.workoutId = workoutId;
    }

    public List<Shots> getShots() {
        return shots;
    }

    public void setShots(List<Shots> shots) {
        this.shots = shots;
    }

    public void addShot(Shots shot) {
        if (shots != null) {
            shots.add(shot);
        }
    }

    public void removeShot(Shots shot) {
        if (shots != null) {
            shots.remove(shot);
        }
    }
}
