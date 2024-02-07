package onetoone.Fans;

import onetoone.Players.Player;
import onetoone.Teams.Team;
import onetoone.Teams.TeamRepository;
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

import java.util.List;

@RestController
public class FanController {

    @Autowired
    FanRepository fanRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TeamRepository teamRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";


    @GetMapping(path = "/fans")
    List<Fan> getAllFans(){
        return fanRepository.findAll();
    }

    @GetMapping(path = "/fan/{id}")
    Fan getFanById(@PathVariable int id){
        return fanRepository.findById(id);
    }

    @GetMapping(path = "/fan_user/{fan_id}")
    User getUserFromFan(@PathVariable int fan_id){
        Fan temp = fanRepository.findById(fan_id);
        return userRepository.findById(temp.getUser_id());
    }

    @PostMapping(path = "/fans")
    Fan createFan(@RequestBody Fan fan){
        if (fan == null)
           throw new RuntimeException();
        fanRepository.save(fan);
        return fan;
    }

    @PostMapping("/updateFan/{id}")
    public void updateFan(@PathVariable int id, @RequestBody Fan fan) {
        Fan temp = getFanById(id);
        temp.setName(fan.getName());
    }

    @DeleteMapping(path = "/fans/{id}")
    String deleteFan(@PathVariable int id) {
        fanRepository.deleteById(id);
        return success;
    }

    @DeleteMapping(path = "/fans/{fan_id}/{team_id}")
    String deleteCoachFromTeam(@PathVariable int fan_id, @PathVariable int team_id) {
        Team temp = teamRepository.findById(team_id);
        temp.deleteFan(fanRepository.findById(fan_id));
        fanRepository.deleteById(fan_id);
        return success;
    }


}
