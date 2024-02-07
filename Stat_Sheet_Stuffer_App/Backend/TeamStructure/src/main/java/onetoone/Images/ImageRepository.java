package onetoone.Images;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image,Integer> {

    Optional<Image> findByUserId(int userId);

    Optional<Image> findByTeamId(int teamId);
}
