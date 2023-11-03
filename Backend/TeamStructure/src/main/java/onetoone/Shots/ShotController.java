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
    public Shots createShots(@RequestBody Shots shot) {
        if (shot == null) {
            throw new RuntimeException();
        }
        return shotRepository.save(shot);
    }

    @GetMapping(path = "/shots")
    List<Shots> getAllShots(){
        return shotRepository.findAll();
    }

}
