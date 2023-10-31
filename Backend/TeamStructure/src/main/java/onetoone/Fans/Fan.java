package onetoone.Fans;

import javax.persistence.*;



import com.fasterxml.jackson.annotation.JsonIgnore;
import onetoone.Teams.Team;

@Entity
public class Fan {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;

    private String name;

    private int user_id;

    @ManyToOne
    @JoinColumn(name = "team_id")
    @JsonIgnore
    private Team team;


    public Fan(String name, int user_id){
        this.name = name;
        this.user_id = user_id;
    }

    public Fan(){

    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

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

}
