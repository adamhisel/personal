package onetoone.Players;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


/**
 * 
 * @author Vivek Bengre
 * 
 */ 

public interface PlayerRepository extends JpaRepository<Player, Long>  {
    Player findById(int id);

    @Transactional
    void deleteById(int id);

    @Modifying
    @Query("UPDATE Player p SET p.playerName = :playerName, p.number = :number, p.position = :position WHERE p.id = :id")
    void updatePlayerById(@Param("id") int id, @Param("playerName") String userName, @Param("position") String position, @Param("number") int number);




}
