package onetoone.Shots;

import onetoone.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
public class ShotController {
    @Autowired
    onetoone.Shots.ShotRepository shotRepository;
    @PostMapping(path ="/shots")
    void createShots(@RequestBody Shots shot) {
        if (shot == null) {
            throw new RuntimeException();
        }
        shotRepository.save(shot);
    }

    @GetMapping(path = "/shots")
    List<Shots> getAllShots(){
        return shotRepository.findAll();
    }
    @GetMapping(path = "/shots/{playerID}/{activityID}")
    public List<Shots> findShots(@PathVariable int playerID, @PathVariable int activityID) {
        return shotRepository.findByPlayerIdAndActivityID(playerID, activityID);
    }
}
