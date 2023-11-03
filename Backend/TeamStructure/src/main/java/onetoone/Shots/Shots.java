package onetoone.Shots;


import com.fasterxml.jackson.annotation.JsonBackReference;
import onetoone.Game.Game;
import onetoone.Players.Player;
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
    private boolean made;
    private int value;
    private int xCoord;
    private int yCoord;
    @ManyToOne
    @JoinColumn(name = "gameId")
    @JsonBackReference(value = "game-shots")
    private Game game;


    @ManyToOne
    @JoinColumn(name = "playerId")
    private Player player;


    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;


    @ManyToOne
    @JoinColumn(name = "workoutId")
    @JsonBackReference(value = "workout-shots")
    private Workout workout;




    public Shots() {
        // Default (no-argument) constructor
    }


    public Shots(Boolean made, int value, int xCoord, int yCoord) {
        this.made = made;
        this.value = value;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
    }


    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }


    public boolean getMade() {
        return made;
    }


    public void setMade(boolean made) {
        this.made = made;
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


    public void setWorkout(Workout workout) {
        this.workout = workout;
    }


    public Workout getWorkout() {
        return workout;
    }


    public void setGame(Game game) {
        this.game = game;
    }


    public Game getGame() {
        return game;
    }


    public void setPlayer(Player player) {
        this.player = player;
    }


    public Player getPlayer() {
        return player;
    }


    public Team getTeam() { return team;
    }
}
