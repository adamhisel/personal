package onetoone.Teams;

        import org.springframework.data.jpa.repository.JpaRepository;
        import org.springframework.data.jpa.repository.Query;
        import org.springframework.data.repository.query.Param;

/**
 *
 * @author Vivek Bengre
 *
 */

public interface TeamRepository extends JpaRepository<Team, Long> {

    Team findById(int id);

    void deleteById(int id);


//    Team findByPlayer_Id(int id);

//    Team findByPlayer_Id(int id);

}
