package onetoone.Players;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;



public interface PlayerRepository extends JpaRepository<Player, Long> {
    Player findByName(String name);

    @Transactional
    void deleteByName(String name);
}
