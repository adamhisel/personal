package onetoone.Players;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import onetoone.Teams.Team;
import onetoone.Teams.TeamRepository;

@RestController
public class PlayerController {

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    TeamRepository teamRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @GetMapping(path = "players")
    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    @GetMapping("/players/{name}")
    public Player getPlayerByName(@PathVariable String name) {
        return playerRepository.findByName(name);
    }

    @PostMapping(path = "/players")
    public String createPlayer(Player player) {
        if (player == null)
            return failure;
        playerRepository.save(Player);
        return success;
    }

    @PutMapping("/players/{name}")
    public Player updatePlayer(@PathVariable String name, @RequestBody Player request) {
        Player player = playerRepository.findByName(name);
        if (player == null)
            return null;
        // Update player properties here
        player.setPlayerName(request.getPlayerName());
        player.setPosition(request.getPosition());
        // Save the updated player
        playerRepository.save(player);
        return player;
    }

    @DeleteMapping(path = "/players/{name}")
    String deletePlayer(@PathVariable String name){

        // Check if there is an object depending on user and then remove the dependency
        Team team = teamRepository.findByPlayer_Name(name);
        team.setPlayer(null);
        teamRepository.save(team);

        // delete the laptop if the changes have not been reflected by the above statement
        playerRepository.deleteByName(name);
        return success;
    }
}