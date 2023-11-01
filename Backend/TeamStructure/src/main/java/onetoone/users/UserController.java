package onetoone.users;
import onetoone.Teams.Team;
import onetoone.Teams.TeamRepository;
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

    @PostMapping(path = "/users")
    User createUser(@RequestBody User user) {
        if (user == null) {
            throw new RuntimeException();
        }
        userRepository.save(user);
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
        team.setUser(user);
        user.addTeam(team);
        userRepository.save(user);
        return success;
    }

}

