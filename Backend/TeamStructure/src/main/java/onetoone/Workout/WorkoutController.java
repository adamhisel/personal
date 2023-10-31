package onetoone.Workout;
import onetoone.Shots.Shots;
import onetoone.Shots.ShotsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public class WorkoutController {

@Autowired
WorkoutRepository workoutRepository;
@Autowired
private WorkoutService workoutService;
@Autowired
private ShotsService shotsService;


    @PostMapping(path ="/workout")
    void createWorkout(@RequestBody Workout workout) {
        if (workout == null) {
            throw new RuntimeException();
        }
        workoutRepository.save(workout);
    }

    @PostMapping("/workout/{workoutId}/shots")
    public ResponseEntity<String> addShotToWorkout(
            @PathVariable int workoutId,
            @RequestBody Shots newShot
    ) {
        Workout workout = workoutService.getWorkoutById(workoutId);
        if (workout == null) {
            return ResponseEntity.notFound().build();
        }

        newShot.setWorkout(workout);
        Shots savedShot = shotsService.saveShot(newShot);

        return ResponseEntity.ok("Shot added to workout with ID: " + workoutId);
    }
}
