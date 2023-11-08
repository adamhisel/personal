package com.example.project;

import java.util.List;

/**
 * Represents a workout session for a user, containing a list of shot attempts made during the session.
 *
 * @author Jagger Gourley
 */
public class Workout {
    private int workoutId;
    private List<Shots> shots;

    /**
     * Constructs a Workout instance with a unique identifier and an initial list of shots.
     *
     * @param workoutId the unique identifier for the workout session
     * @param shots a list of shots representing the attempts made during the workout
     */
    public Workout(int workoutId, List<Shots> shots) {
        this.workoutId = workoutId;
        this.shots = shots;
    }

    /**
     * Adds a shot attempt to the list of shots in the workout session.
     *
     * @param shot the shot to add to the workout
     */
    public void addShot(Shots shot) {
        if (shots != null) {
            shots.add(shot);
        }
    }

    /**
     * Removes a shot attempt from the list of shots in the workout session.
     *
     * @param shot the shot to remove from the workout
     */
    public void removeShot(Shots shot) {
        if (shots != null) {
            shots.remove(shot);
        }
    }

    // Simple getters and setters

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
}
