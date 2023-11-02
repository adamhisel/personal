package onetoone.Players;

import javax.persistence.*;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import onetoone.Game.Game;
import onetoone.Teams.Team;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 *
 * @author Vivek Bengre
 */

@Entity
public class Player {

    /*
     * The annotation @ID marks the field below as the primary key for the table created by springboot
     * The @GeneratedValue generates a value if not already present, The strategy in this case is to start from 1 and increment for each table
     */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    int id;

    private String playerName;

    private int number;

    private String position;

    private int user_id;


    /*
     * @OneToOne creates a relation between the current entity/table(Laptop) with the entity/table defined below it(User)
     * @JsonIgnore is to assure that there is no infinite loop while returning either user/laptop objects (laptop->user->laptop->...)
     */
    @ManyToOne
    @JoinColumn(name = "team_id")
    @JsonIgnore
    private Team team;
    @ManyToMany(mappedBy = "players")
    @JsonIgnore
    private List<Game> games = new ArrayList<>();

    public Player( String playerName, int number, String position, int user_id, int team_id) {
        this.playerName = playerName;
        this.number = number;
        this.position = position;
        this.user_id = user_id;
    }

    public Player(){

    }


    // =============================== Getters and Setters for each field ================================== //

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getPlayerName() { return playerName; }

    public void setPlayerName(String playerName) { this.playerName = playerName; }

    public int getNumber() { return number; }

    public void setNumber(int number) { this.number = number; }

    public String getPosition() { return position; }

    public void setPosition(String position) { this.position = position; }

    public int getUser_id(){
        return user_id;
    }

    public void setUser_id(int user_id){
        this.user_id = user_id;
    }

    public Team getTeam(){
        return team;
    }

    public void setTeam(Team team){
        this.team = team;
    }

    public List<Game> getGames() {
        return games;
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }

    public void addGame(Game game) {
        if (games == null) {
            games = new ArrayList<>();
        }
        games.add(game);
        if (!game.getPlayers().contains(this)) {
            game.addPlayer(this);
        }
    }


}

