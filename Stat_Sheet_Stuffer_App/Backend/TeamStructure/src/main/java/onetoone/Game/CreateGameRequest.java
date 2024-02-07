package onetoone.Game;

import onetoone.Players.Player;
import onetoone.Shots.Shots;

import java.util.List;

public class CreateGameRequest {
    private List<Player> players;
    private List<Shots> shots;

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public List<Shots> getShots() {
        return shots;
    }

    public void setShots(List<Shots> shots) {
        this.shots = shots;
    }
}