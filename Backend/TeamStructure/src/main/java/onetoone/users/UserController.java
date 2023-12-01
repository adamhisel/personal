package onetoone.users;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import onetoone.Players.Player;
import onetoone.Players.PlayerRepository;

import onetoone.Teams.Team;
import onetoone.Teams.TeamRepository;

import onetoone.Fans.Fan;

import onetoone.Coaches.Coach;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    UserRepository userRepository;

    @Autowired

    TeamRepository teamRepository;

    PlayerRepository playerRepository;




    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @GetMapping(path = "/users")
    List<User> getAllUsers(){
        return userRepository.findAll();
    }

    @GetMapping(path = "/users/{id}")
    User getUserById( @PathVariable int id){
        return userRepository.findById(id);
    }

    @GetMapping(path = "/users/{id}/{team_id}")
    String getRoleOfUser(@PathVariable int id, @PathVariable int team_id){
        String result = "";
        User user = userRepository.findById(id);
        Team team = teamRepository.findById(team_id);

        List<Player> players = team.getPlayers();
        List<Fan> fans = team.getFans();
        List<Coach> coaches = team.getCoaches();

        for(int i = 0; i < players.size(); i++){
            Player temp = players.get(i);
            if(temp.getUser_id() == id){
                result = "player";
                break;
            }
        }
        for(int i = 0; i < coaches.size(); i++){
            Coach temp = coaches.get(i);
            if(temp.getUser_id() == id){
                result = "coach";
                break;
            }
        }
        for(int i = 0; i < fans.size(); i++){
            Fan temp = fans.get(i);
            if(temp.getUser_id() == id){
                result = "fan";
                break;
            }
        }
        return result;
    }



    @PostMapping(path = "/users")
    User createUser(@RequestBody User user) {
        if (user == null) {
            throw new RuntimeException();
        }
        User userTempName = userRepository.findByUserName(user.getUserName());
        User userTempEmail = userRepository.findByEmail(user.getEmail());
        if(userTempName == null && userTempEmail == null){
            userRepository.save(user);
        }
        else{
            throw new RuntimeException();
        }
        return user;

    }

    @PostMapping("/updateUser/{id}")
    public void updateUser(@PathVariable int id, @RequestBody User user) {
        User temp = getUserById(id);
        temp.setUserName(user.getUserName());
        temp.setFirstName(user.getFirstName());
        temp.setLastName(user.getLastName());
        temp.setEmail(user.getEmail());
        temp.setPassword(user.getPassword());
        temp.setPhoneNumber(user.getPhoneNumber());
        userRepository.save(temp);
    }

    @DeleteMapping(path = "/users/{id}")
    String deleteUser(@PathVariable int id) {
        userRepository.deleteById(id);
        return success;
    }

    @GetMapping("loginUser/{userName}/{password}")
    User loginUser(@PathVariable String userName, @PathVariable String password){
        return userRepository.findByuserNameAndPassword(userName,password);
    }

    @PutMapping("/User/{userId}/teams/{teamId}")
    String assignTeamToUser(@PathVariable int userId,@PathVariable int teamId){
        User user = userRepository.findById(userId);
        Team team = teamRepository.findById(teamId);
        if(team == null || user == null)
            return failure;
        team.addUser(user);
        user.addTeam(team);
        userRepository.save(user);
        return success;
    }

}

