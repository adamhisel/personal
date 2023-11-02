package onetoone.Game;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import onetoone.Shots.Shots;
import onetoone.Players.Player;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Game {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;

    // Other game-specific properties can be added here

    @OneToMany(mappedBy = "game", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    @JsonManagedReference("gameShots")

    private List<Shots> shots = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "game_players",
            joinColumns = @JoinColumn(name = "game_id"),
            inverseJoinColumns = @JoinColumn(name = "player_id"))
    private List<Player> players = new ArrayList<>();

    public Game() {
        // Default (no-argument) constructor
    }

    public Game(List<Player> playerList, List<Shots> shotsList) {
        this.players = playerList;
        this.shots = shotsList;
        // Initialize game-specific properties
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Shots> getShots() {
        return shots;
    }

    public void setShots(List<Shots> shots) {
        this.shots = shots;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public void addShot(Shots shot) {
        this.shots.add(shot);
    }

    public void removeShot(Shots shot) {
        this.shots.remove(shot);
        shot.setGame(null);
    }

    public void addPlayer(Player player) {
        this.players.add(player);
        player.getGames().add(this);
    }

    public void removePlayer(Player player) {
        this.players.remove(player);
        player.getGames().remove(this);
    }



}
