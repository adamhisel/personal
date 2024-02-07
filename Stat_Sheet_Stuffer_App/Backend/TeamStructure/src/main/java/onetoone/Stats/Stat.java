package onetoone.Stats;

import com.fasterxml.jackson.annotation.JsonIgnore;
import onetoone.Players.Player;

import javax.persistence.*;

@Entity
public class Stat {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private int assist;

    private int rebounds;

    private  int blocks;

    private int steals;

    private int gameID;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "player_id")
    private Player player;



    public Stat(int assists, int rebounds, int blocks, int steals){
        this.assist = assists;
        this.rebounds = rebounds;
        this.blocks = blocks;
        this.steals = steals;
    }

    public Stat(){

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAssist() {
        return assist;
    }

    public void setAssist(int assist) {
        this.assist = assist;
    }

    public int getRebounds() {
        return rebounds;
    }

    public void setRebounds(int rebounds) {
        this.rebounds = rebounds;
    }

    public int getBlocks() {
        return blocks;
    }

    public void setBlocks(int blocks) {
        this.blocks = blocks;
    }

    public int getSteals() {
        return steals;
    }

    public void setSteals(int steals) {
        this.steals = steals;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void addPlayer(Player player) {
        this.player = player;
    }
}
