package onetoone.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import onetoone.Players.Player;

import onetoone.Teams.Team;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int id;
    private String userName;

    private String firstName;

    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;



    @ManyToMany
    @JoinTable(name = "user_team",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "team_id"))
    private List<Team> teams;


//    @JsonIgnore
//    @OneToOne
//    private User user;



    public User(String userName, String firstName, String lastName, String email,String password, String phoneNumber){
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        teams = new ArrayList<>();
    }

    public User() {
        teams = new ArrayList<>();
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public List getTeams(){ return teams; }

    public void setTeams(List<Team> teams){
        this.teams = teams;
    }

    public void addTeam(Team team){
        this.teams.add(team);
    }

}

