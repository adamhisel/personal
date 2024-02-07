package onetoone.Stats;

import onetoone.Players.Player;
import onetoone.Players.PlayerRepository;
import onetoone.Teams.Team;
import onetoone.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class StatController {

    @Autowired
    StatRepository statRepository;

    @Autowired
    PlayerRepository playerRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @GetMapping(path = "/stats")
    List<Stat> getAllStats(){
        return statRepository.findAll();
    }

    @GetMapping(path = "/stats/{id}")
    Stat getStatsById( @PathVariable int id){
        return statRepository.findById(id);
    }

    @PostMapping(path = "/stats")
    Stat createStat(@RequestBody Stat stat) {
        if (stat == null) {
            throw new RuntimeException();
        }
        statRepository.save(stat);
        return stat;
    }

    @DeleteMapping(path = "/stats/{id}")
    String deleteStat(@PathVariable int id) {
        statRepository.deleteById(id);
        return success;
    }

    @PutMapping("/Stat/{statId}/Player/{playerId}")
    String assignStatsToPlayer(@PathVariable int statId,@PathVariable int playerId){
        Stat stat = statRepository.findById(statId);
        Player player = playerRepository.findById(playerId);
        if(stat == null || player == null) {
            return failure;
        }
        stat.addPlayer(player);
        player.addStat(stat);
        statRepository.save(stat);
        playerRepository.save(player);
        return success;
    }

}
