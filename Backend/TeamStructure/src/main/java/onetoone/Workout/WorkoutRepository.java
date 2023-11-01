package onetoone.Workout;

import onetoone.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkoutRepository extends JpaRepository<Workout, Integer> {


}
