package onetoone.CustomWorkout;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomWorkoutRepository extends JpaRepository<CustomWorkout, Integer> {

    List<CustomWorkout> findByUserId(int userId);
}
