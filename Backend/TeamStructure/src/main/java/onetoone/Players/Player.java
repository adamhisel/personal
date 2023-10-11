package onetoone.Players;

import javax.persistence.*;



import com.fasterxml.jackson.annotation.JsonIgnore;

import onetoone.Teams.Team;
import onetoone.users.User;

/**
 * 
 * @author Vivek Bengre
 */ 

@Entity
@Table(name = "player")
public class Player extends User {
    
    /* 
     * The annotation @ID marks the field below as the primary key for the table created by springboot
     * The @GeneratedValue generates a value if not already present, The strategy in this case is to start from 1 and increment for each table
     */


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

    public Player(String userName, String userType, String email, String password, String phoneNumber, int number,  String position) {
        super(userName, userType, email, password, phoneNumber);
        this.number = number;
        this.position = position;
    }

    public Player(String userName, String userType, String email, String password, String phoneNumber, String playerName, String position, int number) {
        super(userName, userType, email, password, phoneNumber);
        this.number = number;
        this.position = position;
        this.playerName=playerName;
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
