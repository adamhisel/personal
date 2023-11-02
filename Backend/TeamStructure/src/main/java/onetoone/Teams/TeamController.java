package onetoone.Teams;

import java.util.List;
import java.util.Objects;

import onetoone.Coaches.Coach;
import onetoone.Coaches.CoachRepository;
import onetoone.Fans.FanRepository;
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

import onetoone.Coaches.Coach;
import onetoone.Coaches.CoachRepository;

import onetoone.Fans.Fan;
import onetoone.Fans.FanRepository;

/**
 *
 * @author Vivek Bengre
 *
 */

@RestController
public class TeamController {

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    CoachRepository coachRepository;

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    FanRepository fanRepository;

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
    Team createTeam(@RequestBody Team team){
        if (team == null)
            throw new RuntimeException();
        teamRepository.save(team);
        return team;
    }

    @PostMapping("/updateTeam/{id}")
    public void updateTeam(@PathVariable int id, @RequestBody Team team) {
        Team temp = getTeamById(id);
        temp.setTeamName(team.getTeamName());
        temp.setTeamIsPrivate(team.getTeamIsPrivate());
        temp.setPassword(team.getPassword());
        teamRepository.save(temp);

    }


    @PutMapping("/teams/{teamId}/players/{playerId}/{password}")
    String assignPLayerToTeam(@PathVariable int teamId,@PathVariable int playerId, @PathVariable String password){
        Team team = teamRepository.findById(teamId);
        Player player = playerRepository.findById(playerId);
        if(team == null || player == null){
            return failure;
        }
        if(team.getTeamIsPrivate()){
            if(Objects.equals(password, team.getPassword())){
                player.setTeam(team);
                team.addPlayer(player);
                teamRepository.save(team);
                return success;
            }
            else{
                return "wrong password";
            }
        }
        else {
            player.setTeam(team);
            team.addPlayer(player);
            teamRepository.save(team);
            return success;
        }
    }

    @PutMapping("/teams/{teamId}/coaches/{coachId}/{password}")
    String assignCoachToTeam(@PathVariable int teamId,@PathVariable int coachId,@PathVariable String password){
        Team team = teamRepository.findById(teamId);
        Coach coach = coachRepository.findById(coachId);
        if(team == null || coach == null) {
            return failure;
        }
        if(team.getTeamIsPrivate()){
            if(Objects.equals(password, team.getPassword())){
                coach.setTeam(team);
                //team.addCoach(coach);
                teamRepository.save(team);
                return success;
            }
            else{
                return "wrong password";
            }
        }
        else{
            coach.setTeam(team);
            team.addCoach(coach);
            teamRepository.save(team);
            return success;
        }
    }

    @PostMapping("/teams/{teamId}/fans/{fanId}")
    String assignFanToTeam(@PathVariable int teamId, @PathVariable int fanId){
        Team team = teamRepository.findById(teamId);
        Fan fan = fanRepository.findById(fanId);
        if(team == null || fan == null) {
            return failure;
        }
        fan.setTeam(team);
        team.addFan(fan);
        teamRepository.save(team);
        return success;
    }




    @DeleteMapping("/teams/{id}")
    String deleteTeam(@PathVariable int id){
        teamRepository.deleteById(id);
        return success;
    }
}
