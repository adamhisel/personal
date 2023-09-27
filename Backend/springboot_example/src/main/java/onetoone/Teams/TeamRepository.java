package onetoone.Teams;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;


/**
 * 
 * @author Vivek Bengre
 * 
 */ 

public interface TeamRepository extends JpaRepository<Team, Long> {
    
    Team findByName(String name);

    void deleteByName(String name);

    Team findByPlayer_Name(String name);
}
