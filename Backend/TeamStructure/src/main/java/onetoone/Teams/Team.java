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


    /*
     * @OneToOne creates a relation between the current entity/table(Laptop) with the entity/table defined below it(User)
     * cascade is responsible propagating all changes, even to children of the class Eg: changes made to laptop within a user object will be reflected
     * in the database (more info : https://www.baeldung.com/jpa-cascade-types)
     * @JoinColumn defines the ownership of the foreign key i.e. the user table will have a field called laptop_id
     */
    @OneToMany
    public List<Player> players;

    @OneToMany
    private List<Coach> coaches;

    @OneToMany
    private List<Fan> fans;


    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;


    public Team(String teamName, String password, boolean team_is_private) {
        this.teamName = teamName;
        this.password = password;
        this.team_is_private = team_is_private;
        players = new ArrayList<>();
        coaches = new ArrayList<>();
        fans = new ArrayList<>();
    }

    public Team() {
        players = new ArrayList<>();
        coaches = new ArrayList<>();
        fans = new ArrayList<>();
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

    public List getCoaches(){ return coaches; }

    public void setCoaches(List<Coach> coaches){
        this.coaches = coaches;
    }

    public void addCoach(Coach coach){
        this.coaches.add(coach);
    }

    public List getFans(){ return fans; }

    public void setFans(List<Fan> fans){
        this.fans = fans;
    }

    public void addFan(Fan fan){
        this.fans.add(fan);
    }

    public User getUser(){
        return this.user;
    }

    public void setUser(User user){
        this.user = user;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public boolean getTeamIsPrivate(){
        return team_is_private;
    }

    public void setTeamIsPrivate(boolean team_is_private){
        this.team_is_private = team_is_private;
    }

}

