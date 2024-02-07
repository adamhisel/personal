package appBackend.Workout;

import appBackend.Shots.Shots;
import appBackend.Shots.ShotsService;
import appBackend.users.User;
import appBackend.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class WorkoutController {

    @Autowired
    WorkoutRepository workoutRepository;
    @Autowired
    private WorkoutService workoutService;
    @Autowired
    private ShotsService shotsService;

    @Autowired
    private UserService userService;


    @GetMapping(path = "/workouts")
    public List<Workout> getAllWorkouts(@RequestParam(name = "userId", required = false) Integer userId) {
        if (userId != null) {
            User user = userService.getUser(userId);
            if (user == null) {
                throw new RuntimeException("User not found with ID: " + userId);
            }
            return workoutRepository.findByUser(user);
        } else {
            return workoutRepository.findAll();
        }
    }

    @PostMapping(path = "/workouts")
    public Workout createWorkout(@RequestParam int userId) {
        User user = userService.getUser(userId);
        if (user == null) {
            throw new RuntimeException("User not found with ID: " + userId);
        }
        Workout workout = new Workout(user);
        return workoutRepository.save(workout);
    }

    @PostMapping(path = "/{workoutId}/bulk-shots")
    public ResponseEntity<Void> addShotsToWorkout(@PathVariable int workoutId, @RequestBody List<Shots> newShots) {
        Workout workout = workoutService.getWorkoutById(workoutId);
        if (workout == null) {
            throw new RuntimeException("Workout not found with ID: " + workoutId);
        }

        for (Shots shot : newShots) {
            shot.setWorkout(workout);
            shotsService.saveShot(shot);
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/{workoutId}/shots")
    public List<Shots> getShotsFromWorkout(@PathVariable int workoutId) {
        Workout workout = workoutService.getWorkoutById(workoutId);
        if (workout == null) {
            throw new RuntimeException("Workout not found with ID: " + workoutId);
        }
        return workout.getShots();
    }
}
