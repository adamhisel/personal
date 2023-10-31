package onetoone.Shots;

import onetoone.Teams.Team;
import onetoone.Workout.Workout;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity

public class Shots {
@Id
@GeneratedValue(strategy = GenerationType.AUTO)
public int id;
private int playerId;
private int makemiss;
private int value;
private int xCoord;
private int yCoord;
private int gameId;
private int activityID;
@ManyToOne
@JoinColumn(name = "workoutId")
private Workout workout;


public Shots() {
        // Default (no-argument) constructor
    }

public Shots(int makemiss, int value, int xCoord, int yCoord, int gameId, int playerId, int activityID){
    this.makemiss = makemiss;
    this.value = value;
    this.xCoord = xCoord;
    this.yCoord = yCoord;
    this.gameId = gameId;
    this.playerId = playerId;
    this.activityID = activityID;


}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getMakemiss() {
        return makemiss;
    }

    public void setMakemiss(int makemiss) {
        this.makemiss = makemiss;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getxCoord() {
        return xCoord;
    }

    public void setxCoord(int xCoord) {
        this.xCoord = xCoord;
    }

    public int getyCoord() {
        return yCoord;
    }

    public void setyCoord(int yCoord) {
        this.yCoord = yCoord;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getActivityID() {
        return activityID;
    }

    public void setActivityID(int activityID) {
        this.activityID = activityID;
    }

    public void setWorkout(Workout workout) {
    this.workout = workout;
    }

    public Workout getWorkout() {
        return workout;
    }
}

