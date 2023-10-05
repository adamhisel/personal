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

    private PlayerService playerService;
    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    TeamRepository userRepository;
    
    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";


    public PlayerController(PlayerService playerService){
        this.playerService = playerService;
    }


    @GetMapping(path = "/player/{id}")
    Player getPlayerById(@PathVariable int id){
        return playerService.(id);
    }


    @GetMapping(path = "/players")
    List<Player> getAllPlayers(){
        return playerRepository.findAll();
    }

//    @GetMapping(path = "/players/{id}")
//    Player getPlayerById(@PathVariable int id){
//        return playerRepository.findById(id);
//    }

    @PostMapping(path = "/players")
    String createPlayer(Player player){
        if (player == null)
            return failure;
        playerRepository.save(player);
        return success;
    }

    @PostMapping("/updatePlayer/{id}")
    public void updatePlayer(@PathVariable int id, @RequestBody PlayerUpdateRequest playerUpdateRequest) {

    }


    @PutMapping(path = "/players/{id}")
    Player updatePlayer(@PathVariable int id, @RequestBody Player request){
        Player player = playerRepository.findById(id);
        if(player == null)
            return null;
        playerRepository.save(request);
        return playerRepository.findById(id);
    }

    @DeleteMapping(path = "/players/{id}")
    String deletePlayer(@PathVariable int id){

        // Check if there is an object depending on user and then remove the dependency
        Team user = userRepository.findByPlayer_Id(id);
        user.setPlayer(null);
        userRepository.save(user);

        // delete the laptop if the changes have not been reflected by the above statement
        playerRepository.deleteById(id);
        return success;
    }



}
