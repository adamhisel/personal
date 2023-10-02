package onetoone.Players;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import onetoone.Teams.Team;

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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String playerName;

    private int number;

    private String position;

    /*
     * @OneToOne creates a relation between the current entity/table(Laptop) with the entity/table defined below it(User)
     * @JsonIgnore is to assure that there is no infinite loop while returning either user/laptop objects (laptop->user->laptop->...)
     */
    @OneToOne
    @JsonIgnore
    private Team team;

    public Player( String playerName, int number, String position) {
        this.playerName = playerName;
        this.number = number;
        this.position = position;
    }

    public Player() {
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

    public Team getTeam(){
        return team;
    }

    public void setTeam(Team team){
        this.team = team;
    }



}
