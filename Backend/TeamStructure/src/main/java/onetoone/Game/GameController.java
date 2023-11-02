package onetoone.Game;
import onetoone.Players.Player;
import onetoone.Players.PlayerRepository;
import onetoone.Shots.Shots;
import onetoone.Teams.Team;
import onetoone.Shots.ShotRepository;
import onetoone.Teams.TeamRepository;


import org.apache.catalina.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class GameController {
    @Autowired
    GameRepository gameRepository;
    @Autowired
    ShotRepository shotRepository;
    @Autowired
    PlayerRepository playerRepository;

    @PostMapping(path = "/game")
    Game createGame(@RequestBody Game game){
        if (game == null)
            throw new RuntimeException();
        gameRepository.save(game);
        return game;
    }
    @PostMapping(path = "/game/addShot")
    public Game addShotToGame(@RequestParam int playerId, @RequestParam int gameId, @RequestBody Shots newShot) {
        Game game = gameRepository.findById(gameId);
        Player player = playerRepository.findById(playerId);

        // Check if the player belongs to the game's team (you may need to modify this based on your data model)
        if (player.getTeam() != game.getTeam()) {
            throw new RuntimeException("Player doesn't belong to the game's team");
        }

        newShot = shotRepository.save(newShot); // Save the new shot

        game.addShot(newShot); // Add the shot to the game's shots list
        gameRepository.save(game); // Save the updated game

        return game;
    }
}





