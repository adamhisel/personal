package onetoone.CustomWorkout;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomWorkoutRepository extends JpaRepository<CustomWorkout, Long> {

    CustomWorkout findByUserId(int userId);
}
