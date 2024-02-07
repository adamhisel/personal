package onetoone.CustomWorkout;

import onetoone.Points.Points;
import onetoone.Points.PointsRepository;
import onetoone.Shots.Shots;
import org.apache.catalina.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CustomWorkoutController {
    @Autowired
    CustomWorkoutRepository customWorkoutRepository;
    @Autowired
    PointsRepository pointsRepository;

    @PostMapping(path = "/uploadworkout")
    public CustomWorkoutResponse createNewWorkout(@RequestBody CustomWorkout customWorkout) {
        // Save the custom workout to generate its ID
        customWorkoutRepository.save(customWorkout);

        // Create a DTO to represent the response body
        CustomWorkoutResponse response = new CustomWorkoutResponse(
                customWorkout.getCustomWoutId(),
                customWorkout.getCoords(),
                customWorkout.getWorkoutName(),
                customWorkout.getUserId()
        );

        // Return the DTO
        return response;
    }
    @PostMapping("/{workoutId}/addPoints")
    public ResponseEntity<String> addPointsToWorkout(
            @PathVariable int workoutId,
            @RequestBody List<Points> pointsList) {

        // Retrieve the CustomWorkout by workoutId
        CustomWorkout customWorkout = customWorkoutRepository.findById(workoutId)
                .orElseThrow(() -> new RuntimeException("CustomWorkout not found with id: " + workoutId));

        // Associate each point with the CustomWorkout
        for (Points point : pointsList) {
            point.setWorkout(customWorkout);
            pointsRepository.save(point);
        }
        return ResponseEntity.ok("Points added successfully");
    }





    @GetMapping(path = "/getCustomWorkout/{userId}")
    List<CustomWorkout> getCustomWorkout (@PathVariable int userId){
      if (customWorkoutRepository.findByUserId(userId) != null){
          return customWorkoutRepository.findByUserId(userId);
        }else {
          throw new RuntimeException("workout not found");
      }
    }

}
