package onetoone.Teams;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import onetoone.Players.Player;
import onetoone.Players.PlayerRepository;

/**
 *
 * @author Vivek Bengre
 *
 */

@RestController
public class TeamController {

    private TeamService teamService;
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

    @GetMapping(path = "/teams/{id}")
    Team getTeamById( @PathVariable int id){
        return teamRepository.findById(id);
    }

    @PostMapping(path = "/teams")
    String createTeam(@RequestBody Team team){
        if (team == null)
            return failure;
        teamRepository.save(team);
        return success;
    }

    @PostMapping("/updateTeam/{id}")
    public void updateTeam(@PathVariable int id, @RequestBody TeamUpdateRequest request) {
        teamService.updateTeam(id,
                request.getTeamName());
    }


    @PutMapping("/teams/{teamId}/players/{playerId}")
    String assignPLayerToTeam(@PathVariable int teamId,@PathVariable int playerId){
        Team team = teamRepository.findById(teamId);
        Player laptop = playerRepository.findById(playerId);
        if(team == null || laptop == null)
            return failure;
        laptop.setTeam(team);
        team.addPlayer(laptop);
        teamRepository.save(team);
        return success;
    }

    @DeleteMapping(path = "/teams/{id}")
    String deleteTeam(@PathVariable int id){
        teamRepository.deleteById(id);
        return success;
    }
}
