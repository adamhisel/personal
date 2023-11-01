package onetoone.Shots;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ShotRepository extends JpaRepository<Shots, Long> {
    List<Shots> findByPlayerIdAndActivityID(int playerID, int activityID);
}
