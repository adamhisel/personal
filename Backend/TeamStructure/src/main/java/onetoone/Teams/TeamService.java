package onetoone.Teams;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class TeamService {
    private final TeamRepository teamRepository;

    @Autowired
    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @Transactional
    public void updateTeam(int id, String teamName) {
        teamRepository.updateTeamById(id, teamName);
    }



    @Transactional
    public void createTeam(Team team) {
        teamRepository.save(team);
    }



}

