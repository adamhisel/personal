package onetoone.Workout;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import onetoone.Shots.Shots;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import onetoone.users.User;

@Entity
public class Workout {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int workoutId;

    private int playerId;

    // Create a one-to-many relationship with Shots
    @OneToMany(mappedBy = "workout")
    @JsonManagedReference
    private List<Shots> shots;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    public Workout() {
        // Default (no-argument) constructor
    }

    public Workout(User user) {
        this.user = user;


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

    public User getUser(){
        return user;
    }

    public void setUser(User user){
        this.user = user;
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


