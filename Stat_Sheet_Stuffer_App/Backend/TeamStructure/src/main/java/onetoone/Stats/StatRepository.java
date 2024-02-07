package onetoone.Stats;



import onetoone.Teams.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatRepository extends JpaRepository<Stat, Long> {

    Stat findById(int id);

    void deleteById(int id);
}
