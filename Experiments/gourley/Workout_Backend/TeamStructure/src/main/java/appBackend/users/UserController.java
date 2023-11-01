package appBackend.users;
import appBackend.Shots.Shots;
import appBackend.Teams.Team;
import appBackend.Teams.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    private final UserService userService;
    @Autowired
    UserRepository userRepository;

    @Autowired
    TeamRepository teamRepository;



    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/users")
    List<User> getAllUsers(){
        return userRepository.findAll();
    }

    @GetMapping(path = "/users/{id}")
    User getUserById( @PathVariable int id){
        return userRepository.findById(id);
    }

    @PostMapping(path = "/users")
    public User createUser(@RequestBody User user) {
        if (user == null) {
            throw new RuntimeException("User cannot be null");
        }
        return userRepository.save(user);
    }

    @PostMapping("/updateUser/{id}")
    public void updateUser(@PathVariable int id, @RequestBody UserUpdateRequest request) {
        userService.updateUser(id,
                request.getUserName(),
                request.getEmail(),
                request.getPassword(),
                request.getPhoneNumber());
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

