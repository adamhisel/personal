package onetoone.CustomWorkout;

import onetoone.Points.Points;

import java.util.List;

public class CustomWorkoutResponse {
    private int customWoutId;
    private List<Points> coords;
    private String workoutName;
    private int userId;

    public CustomWorkoutResponse(int customWoutId, List<Points> coords, String workoutName, int userId) {
        this.customWoutId = customWoutId;
        this.coords = coords;
        this.workoutName = workoutName;
        this.userId = userId;
    }

    // Getter and setter methods


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getWorkoutName() {
        return workoutName;
    }

    public void setWorkoutName(String workoutName) {
        this.workoutName = workoutName;
    }

    public List<Points> getCoords() {
        return coords;
    }

    public void setCoords(List<Points> coords) {
        this.coords = coords;
    }

    public int getCustomWoutId() {
        return customWoutId;
    }

    public void setCustomWoutId(int customWoutId) {
        this.customWoutId = customWoutId;
    }
}
