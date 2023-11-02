package onetoone.Game;

import onetoone.Players.Player;
import onetoone.Shots.Shots;
import onetoone.Teams.Team;

import javax.persistence.*;
import java.util.List;

@Entity
public class Game {
   @OneToMany
    List<Player> player;

    @OneToMany
    List<Shots> shots;


    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private int gameId;

    public Game(){

    }
    public Game(Team team, List<Shots> shots){
        this.team = team;
        this.shots = shots;

    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public List<Shots> getShots() {
        return shots;
    }

    public void setShots(List<Shots> shots) {
        this.shots = shots;
    }

    public void addShot(Shots newShot) {
    }


}
