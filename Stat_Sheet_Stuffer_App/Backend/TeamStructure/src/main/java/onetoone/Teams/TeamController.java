package onetoone.Teams;

import java.util.List;
import java.util.Objects;

import onetoone.Coaches.Coach;
import onetoone.Coaches.CoachRepository;
import onetoone.Fans.FanRepository;
import onetoone.Game.Game;
import onetoone.Game.GameRepository;
import onetoone.users.User;
import onetoone.users.UserRepository;
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
    @Autowired
    UserRepository userRepository;
    @Autowired
    GameRepository gameRepository;

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


    @PutMapping("/teams/{teamId}/players/{playerId}/{password}/{userId}")
    String assignPLayerToTeam(@PathVariable int teamId,@PathVariable int playerId, @PathVariable String password, @PathVariable int userId){
        Team team = teamRepository.findById(teamId);
        Player player = playerRepository.findById(playerId);
        User user = userRepository.findById(userId);
        if(team == null || player == null||user == null){
            return failure;
        }
        if(team.getTeamIsPrivate()){
            if(Objects.equals(password, team.getPassword())){
                player.setTeam(team);
                team.addPlayer(player);
                team.addUser(user);
                //user.addTeam(team);
                teamRepository.save(team);
                userRepository.save(user);
                return success;
            }
            else{
                return "wrong password";
            }
        }
        else {
            player.setTeam(team);
            team.addPlayer(player);
            team.addUser(user);
            //user.addTeam(team);
            teamRepository.save(team);
            userRepository.save(user);
            return success;
        }
    }

    @PutMapping("/teams/{teamId}/coaches/{coachId}/{password}/{userId}")
    String assignCoachToTeam(@PathVariable int teamId,@PathVariable int coachId,@PathVariable String password,@PathVariable int userId){
        Team team = teamRepository.findById(teamId);
        Coach coach = coachRepository.findById(coachId);
        User user = userRepository.findById(userId);
        if(team == null || coach == null) {
            return failure;
        }
        if(team.getTeamIsPrivate()){
            if(Objects.equals(password, team.getPassword())){
                coach.setTeam(team);
                team.addCoach(coach);
                teamRepository.save(team);
                team.addUser(user);
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

    @PutMapping("/teams/{teamId}/fans/{fanId}/{userId}")
    String assignFanToTeam(@PathVariable int teamId, @PathVariable int fanId, @PathVariable int userId){
        Team team = teamRepository.findById(teamId);
        Fan fan = fanRepository.findById(fanId);
        User user = userRepository.findById(userId);
        if(team == null || fan == null||user == null) {
            return failure;
        }
        fan.setTeam(team);
        team.addFan(fan);
        team.addUser(user);
        teamRepository.save(team);
        return success;
    }
    @PutMapping("teams/{teamId}/addGame/{gameId}")
    String addGameToTeam(@PathVariable int teamId, @PathVariable int gameId){
        Team team = teamRepository.findById(teamId);
        Game game = gameRepository.findById(gameId);
        if(team == null || game == null){
            return failure;
        }
        else {
            team.addGame(game);
            game.setTeam(team);
            teamRepository.save(team);
            gameRepository.save(game);
            return success;
        }
    }




    @DeleteMapping("/teams/{team_id}/{user_id}")
    String deleteTeam(@PathVariable int team_id, @PathVariable int user_id){
        User temp = userRepository.findById(user_id);
        temp.removeTeam(teamRepository.findById(team_id));
        teamRepository.deleteById(team_id);
        return success;
    }
}
