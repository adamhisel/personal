package appBackend.users;

import appBackend.Teams.Team;
import appBackend.Workout.Workout;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int id;
    private String userName;
    private String email;
    private String password;
    private String phoneNumber;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Workout> workouts;

    @OneToMany
    @JsonIgnore
    private List<Team> teams;

    public User(String userName, String email,String password, String phoneNumber){
        this.userName = userName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        teams = new ArrayList<>();
    }

    public User() {
        teams = new ArrayList<>();
    }


    public int getUserID() {
        return id;
    }

    public void setUserID(int userID) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

