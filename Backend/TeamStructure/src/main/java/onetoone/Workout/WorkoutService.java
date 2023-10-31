package onetoone.Workout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkoutService {
    @Autowired
    private WorkoutRepository workoutRepository;

    public Workout getWorkoutById(int workoutId) {
        return workoutRepository.findById(workoutId).orElse(null);
    }
}