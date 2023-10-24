package onetoone.Players;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PlayerService {
    private final PlayerRepository playerRepository;

    @Autowired
    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

//    @Transactional
//    public void updatePlayer(int id, String playerName, int number, String position) {
//        playerRepository.updatePlayerById(id, playerName, position, number);
//    }



    @Transactional
    public void createPlayer(Player player) {
        playerRepository.save(player);
    }


}
