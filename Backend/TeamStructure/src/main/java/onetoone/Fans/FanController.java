package onetoone.Fans;

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

    @PostMapping(path = "/fans")
    Fan createFan(@RequestBody Fan fan){
        if (fan == null)
           throw new RuntimeException();
        fanRepository.save(fan);
        return fan;
    }

    @DeleteMapping(path = "/fans/{id}")
    String deleteFan(@PathVariable int id) {
        fanRepository.deleteById(id);
        return success;
    }


}
