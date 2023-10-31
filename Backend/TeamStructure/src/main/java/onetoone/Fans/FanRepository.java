package onetoone.Fans;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface FanRepository extends JpaRepository<Fan, Long> {

    Fan findById(int id);

    @Transactional
    void deleteById(int id);

}
