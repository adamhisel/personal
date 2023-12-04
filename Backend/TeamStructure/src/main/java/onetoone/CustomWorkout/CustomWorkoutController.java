package onetoone.CustomWorkout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class CustomWorkoutController {
    @Autowired
    CustomWorkoutRepository customWorkoutRepository;
    @PostMapping(path = "/uploadworkout")
    CustomWorkout createNewWorkout (@RequestBody CustomWorkout customWorkout){
        CustomWorkout c = customWorkout;
        customWorkoutRepository.save(c);
        return c;
    }

    @GetMapping(path = "/getCustomWorkout/{userId}")
    CustomWorkout getCustomWorkout (@PathVariable int userId){
      if (customWorkoutRepository.findByUserId(userId) != null){
          return customWorkoutRepository.findByUserId(userId);
        }else {
          throw new RuntimeException("workout not found");
      }
    }

}
