package onetoone.Teams;


import javax.persistence.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;


import com.fasterxml.jackson.annotation.JsonIgnore;
import onetoone.Coaches.Coach;
import onetoone.Fans.Fan;
import onetoone.Players.Player;
import onetoone.users.User;
import onetoone.Game.Game;

import java.util.ArrayList;
import java.util.List;



/**
 *
 * @author Vivek Bengre
 *
 */

@Entity
public class Team {

    /*
     * The annotation @ID marks the field below as the primary key for the table created by springboot
     * The @GeneratedValue generates a value if not already present, The strategy in this case is to start from 1 and increment for each table
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String teamName;

    private boolean team_is_private;

    private String password;


    @OneToMany
    public List<Player> players;

    @OneToMany
    private List<Coach> coaches;

    @OneToMany
    private List<Fan> fans;

    @OneToMany(mappedBy = "team")
    @JsonIgnore
    private List<Game> games;



    @ManyToMany(mappedBy = "teams")
    @JsonIgnore
    private List<User> users;


    public Team(String teamName, String password, boolean team_is_private) {
        this.teamName = teamName;
        this.password = password;
        this.team_is_private = team_is_private;
        players = new ArrayList<>();
        coaches = new ArrayList<>();
        fans = new ArrayList<>();
        users = new ArrayList<>();
    }

    public Team() {
        players = new ArrayList<>();
        coaches = new ArrayList<>();
        fans = new ArrayList<>();
        users = new ArrayList<>();
    }

    // =============================== Getters and Setters for each field ================================== //

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getTeamName() { return teamName; }

    public void setTeamName(String teamName) { this.teamName = teamName; }

    public List getPlayers(){ return players; }

    public void setPlayers(List<Player> players){
        this.players = players;
    }

    public void addPlayer(Player player){
        this.players.add(player);
    }

    public void deletePlayer(Player player){
      this.players.remove(player);
    }

    public List getCoaches(){ return coaches; }

    public void setCoaches(List<Coach> coaches){
        this.coaches = coaches;
    }

    public void addCoach(Coach coach){
        this.coaches.add(coach);
    }

    public void deleteCoach(Coach coach){
        this.coaches.remove(coach);
    }

    public List getFans(){ return fans; }

    public void setFans(List<Fan> fans){
        this.fans = fans;
    }

    public void addFan(Fan fan){
        this.fans.add(fan);
    }

    public void deleteFan(Fan fan){
        this.fans.remove(fan);
    }

    public List getUsers(){
        return this.users;
    }

    public void setUsers(List<User> users){
        this.users = users;
    }

    public void addUser(User user){ users.add(user);}

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    // Getter for games
    public List<Game> getGames() {
        return games;
    }


    // Setter for games
    public void setGames(List<Game> games) {
        this.games = games;
    }

    public void addGame(Game game){games.add(game);}


    public boolean getTeamIsPrivate(){
        return team_is_private;
    }

    public void setTeamIsPrivate(boolean team_is_private){
        this.team_is_private = team_is_private;
    }

}

