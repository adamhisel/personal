package onetoone.Teams;

        import org.springframework.data.jpa.repository.JpaRepository;
        import org.springframework.data.jpa.repository.Query;
        import org.springframework.data.repository.query.Param;
        import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Vivek Bengre
 *
 */

public interface TeamRepository extends JpaRepository<Team, Long> {

    Team findById(int id);

    @Transactional
    void deleteById(int id);

    Team findByTeamName(String teamName);


//    Team findByPlayer_Id(int id);

//    Team findByPlayer_Id(int id);

}
