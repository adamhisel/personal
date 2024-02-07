package onetoone.Workout;

import onetoone.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkoutRepository extends JpaRepository<Workout, Integer> {

    List<Workout> findByUser(User user);

}
