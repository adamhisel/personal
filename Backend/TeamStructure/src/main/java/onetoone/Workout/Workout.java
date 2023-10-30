package onetoone.Workout;

import onetoone.Shots.Shots;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Workout {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int workoutId;

    private int playerId;

    // Create a one-to-many relationship with Shots
    @OneToMany(mappedBy = "workout")
    private List<Shots> shots;

    public Workout() {
        // Default (no-argument) constructor
    }

    public Workout(int playerId) {
        this.playerId = playerId;
        this.shots = new ArrayList<>();
    }

    public int getWorkoutId() {
        return workoutId;
    }

    public void setWorkoutId(int workoutId) {
        this.workoutId = workoutId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public List<Shots> getShots() {
        return shots;
    }

    public void setShots(List<Shots> shots) {
        this.shots = shots;
    }

    // Helper method to add a shot to the workout
    public void addShot(Shots shot) {
        shots.add(shot);
        shot.setWorkout(this); // Set the bidirectional relationship
    }
}


