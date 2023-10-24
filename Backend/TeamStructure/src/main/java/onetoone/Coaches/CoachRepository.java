package onetoone.Coaches;

import onetoone.Players.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


public interface CoachRepository extends JpaRepository<Coach, Long> {

    Coach findById(int id);

    @Transactional
    void deleteById(int id);
}
