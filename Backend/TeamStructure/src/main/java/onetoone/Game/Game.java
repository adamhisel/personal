package onetoone.Game;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import onetoone.Shots.Shots;
import onetoone.Players.Player;
import onetoone.Teams.Team;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Entity
public class Game {


    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;


    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;


    @OneToMany(mappedBy = "game")
    @JsonBackReference(value = "game-shots")
    private List<Shots> teamShots = new ArrayList<>();


    @ManyToMany(mappedBy = "games")
    private List<Player> players = new ArrayList<>();


    public Game() {
        // Default (no-argument) constructor
    }


    public Game(List<Player> playerList, List<Shots> shotsList) {
        this.players = playerList;
        this.teamShots = shotsList;
        // Initialize game-specific properties
    }


    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }


    public List<Shots> getTeamShots() {
        return teamShots;
    }


    public void setTeamShots(List<Shots> teamShots) {
        this.teamShots = teamShots;
    }


    public List<Player> getPlayers() {
        return players;
    }


    public void setPlayers(List<Player> players) {
        this.players = players;
    }


    // Method to get shots for a specific team within this game
    public List<Shots> getTeamShots(int teamId) {
        return this.teamShots.stream()
                .filter(shot -> shot.getTeam() != null && shot.getTeam().getId() == teamId)
                .collect(Collectors.toList());
    }


    // Method to get shots for a specific player within this game
    public List<Shots> getPlayerShots(int playerId) {
        return this.teamShots.stream()
                .filter(shot -> shot.getPlayer() != null && shot.getPlayer().getId() == playerId)
                .collect(Collectors.toList());
    }


    public void addPlayer(Player player) {
        this.players.add(player);
        player.getGames().add(this);
    }


    public void removePlayer(Player player) {
        this.players.remove(player);
        player.getGames().remove(this);
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
