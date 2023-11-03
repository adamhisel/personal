package onetoone.Shots;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ShotRepository extends JpaRepository<Shots, Long> {
    List<Shots> findByGame_IdAndTeam_Id(int gameId, int teamId);
    List<Shots> findByGame_IdAndPlayer_Id(int gameId, int playerId);
}
