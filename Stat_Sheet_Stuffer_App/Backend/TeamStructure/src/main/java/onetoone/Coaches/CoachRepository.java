package onetoone.Coaches;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;


public interface CoachRepository extends JpaRepository<Coach, Long> {

    Coach findById(int id);

    @Transactional
    void deleteById(int id);
}
