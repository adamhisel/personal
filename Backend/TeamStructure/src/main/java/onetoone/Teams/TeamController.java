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

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    PlayerRepository playerRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @GetMapping(path = "/users")
    List<Team> getAllUsers(){
        return teamRepository.findAll();
    }

    @GetMapping(path = "/users/{id}")
    Team getUserById( @PathVariable int id){
        return teamRepository.findById(id);
    }

    @PostMapping(path = "/users")
    String createUser(Team user){
        if (user == null)
            return failure;
        teamRepository.save(user);
        return success;
    }

    @PutMapping("/users/{id}")
    Team updateUser(@PathVariable int id, @RequestBody Team request){
        Team user = teamRepository.findById(id);
        if(user == null)
            return null;
        teamRepository.save(request);
        return teamRepository.findById(id);
    }   
    
    @PutMapping("/users/{userId}/laptops/{laptopId}")
    String assignLaptopToUser(@PathVariable int userId,@PathVariable int laptopId){
        Team user = teamRepository.findById(userId);
        Player laptop = playerRepository.findById(laptopId);
        if(user == null || laptop == null)
            return failure;
        laptop.setUser(user);
        user.setLaptop(laptop);
        teamRepository.save(user);
        return success;
    }

    @DeleteMapping(path = "/users/{id}")
    String deleteUser(@PathVariable int id){
        teamRepository.deleteById(id);
        return success;
    }
}
