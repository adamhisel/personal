package onetoone.Teams;

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

import onetoone.Players.Player;
import onetoone.Players.PlayerRepository;






@RestController
public class TeamController {

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    PlayerRepository playerRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @GetMapping(path = "/teams")
    List<Team> getAllTeams(){
        return teamRepository.findAll();
    }

    @GetMapping("/teams/{name}")
    Team getTeamByName(@PathVariable String name){
        return teamRepository.findByName(name);
    }

    @PostMapping(path = "/teams")
    String createTeam(@RequestBody Team team){
        if (team == null)
            return failure;
        teamRepository.save(team);
        return success;
    }

    @PutMapping("/teams/{name}")
    Team updateTeam(@PathVariable String name, @RequestBody Team request){
        Team team = teamRepository.findByName(name);
        if(team == null)
            return null;
        teamRepository.save(request);
        return teamRepository.findByName(name);
    }

    @PutMapping("/teams/{teamName}/players/{playerName}")
    String assignPlayerToTeam(@PathVariable String teamName, @PathVariable String playerName){
        Team team = teamRepository.findByName(teamName);
        Player player = playerRepository.findByName(playerName);
        if(team == null || player == null)
            return failure;
        player.setTeam(team);
        team.setPlayer(player);
        playerRepository.save(player); // Save the player with the updated team
        return success;
    }

    @DeleteMapping("/teams/{name}")
    String deleteTeam(@PathVariable String name){
        teamRepository.deleteByName(name);
        return success;
    }
}