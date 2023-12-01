package onetoone.Players;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import onetoone.Teams.Team;
import onetoone.Teams.TeamRepository;

/**
 *
 * @author Vivek Bengre
 *
 */

@RestController
public class PlayerController {
    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    TeamRepository userRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";


    @GetMapping(path = "/player/{id}")
    Player getPlayerById(@PathVariable int id){
        return playerRepository.findById(id);
    }


    @GetMapping(path = "/players")
    List<Player> getAllPlayers(){
        return playerRepository.findAll();
    }


    @PostMapping(path = "/players")
    Player createPlayer(@RequestBody Player player){
        if (player == null)
            throw new RuntimeException();
        playerRepository.save(player);
        return player;
    }

    @PostMapping("/updatePlayer/{id}")
    public void updatePlayer(@PathVariable int id, @RequestBody Player player) {
        Player temp = getPlayerById(id);
        temp.setPlayerName(player.getPlayerName());
        temp.setNumber(player.getNumber());
        temp.setPosition(player.getPosition());
        playerRepository.save(temp);
    }


    @DeleteMapping(path = "/players/{id}")
    String deletePlayer(@PathVariable int id) {
        playerRepository.deleteById(id);
        return success;
    }



}

