package appBackend.Workout;

import appBackend.Shots.Shots;
import appBackend.users.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Workout {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int workoutId;


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
        this.shots = new ArrayList<>();
    }

    public int getWorkoutId() {
        return workoutId;
    }

    public void setWorkoutId(int workoutId) {
        this.workoutId = workoutId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
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


