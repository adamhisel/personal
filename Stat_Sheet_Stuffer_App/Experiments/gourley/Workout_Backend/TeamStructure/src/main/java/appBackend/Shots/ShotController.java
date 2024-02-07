package appBackend.Shots;

import appBackend.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
public class ShotController {
    @Autowired
    ShotRepository shotRepository;
    @PostMapping
    public Shots createShot(@RequestBody Shots shot) {
        if (shot == null) {
            throw new RuntimeException("Shot cannot be null");
        }
        return shotRepository.save(shot);
    }

    @GetMapping(path = "/shots")
    List<Shots> getAllShots(){
        return shotRepository.findAll();
    }

}
