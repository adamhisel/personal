package onetoone.Players;

import javax.persistence.*;



import com.fasterxml.jackson.annotation.JsonIgnore;

import onetoone.Teams.Team;

/**
 *
 * @author Vivek Bengre
 */

@Entity
@Table(name = "player")
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
    private String userName;
    private String userType;
    private String email;
    private String password;
    private String phoneNumber;


    /*
     * @OneToOne creates a relation between the current entity/table(Laptop) with the entity/table defined below it(User)
     * @JsonIgnore is to assure that there is no infinite loop while returning either user/laptop objects (laptop->user->laptop->...)
     */
    @OneToOne
    @JsonIgnore
    private Team team;

    public Player( String userName, String userType, String email,String password, String phoneNumber,String playerName, int number, String position) {
        this.userName = userName;
        this.userType = userType;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
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
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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
