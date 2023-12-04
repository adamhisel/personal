package onetoone.Coaches;

import onetoone.Coaches.CoachRepository;
import onetoone.Fans.Fan;
import onetoone.Players.Player;
import onetoone.Teams.Team;
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
public class CoachController {


    @Autowired
    CoachRepository coachRepository;

    @Autowired
    UserRepository userRepository;


    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @GetMapping(path = "/coaches")
    List<Coach> getAllCoaches(){
        return coachRepository.findAll();
    }

    @GetMapping(path = "/coach/{id}")
    Coach getCoachById(@PathVariable int id){
        return coachRepository.findById(id);
    }

    @GetMapping(path = "/coach_user/{coach_id}")
    User getUserFromCoach(@PathVariable int coach_id){
        Coach temp = getCoachById(coach_id);
        return userRepository.findById(temp.getUser_id());
    }

    @PostMapping(path = "/coaches")
    Coach createCoach(@RequestBody Coach coach){
        if (coach == null)
            throw new RuntimeException();
        coachRepository.save(coach);
        return coach;
    }

    @PostMapping("/updateCoach/{id}")
    public void updateCoach(@PathVariable int id, @RequestBody Coach coach) {
        Coach temp = getCoachById(id);
        temp.setName(coach.getName());
    }

    @DeleteMapping(path = "/coaches/{id}")
    String deleteCoach(@PathVariable int id) {
        coachRepository.deleteById(id);
        return success;
    }


}
