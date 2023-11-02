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

import java.util.List;

@RestController
public class GameController {
    @Autowired
    GameRepository gameRepository;
    @Autowired
    ShotRepository shotRepository;
    @Autowired
    PlayerRepository playerRepository;

    @GetMapping(path = "/games")
    List<Game> getAllGames(){
        return gameRepository.findAll();
    }


    @PostMapping("/create")
    public Game createGame(@RequestBody CreateGameRequest request) {
        // Create a new game
        Game game = new Game();

        // Add players to the game
        List<Player> players = request.getPlayers();
        if (players != null) {
            for (Player player : players) {
                game.addPlayer(player);
            }
        }

        // Save the game to the database
        game = gameRepository.save(game);

        // Add shots to the game and associate them with the saved game
        List<Shots> shots = request.getShots();
        if (shots != null) {
            for (Shots shot : shots) {
                shot.setGame(game);
                game.addShot(shot);
            }
        }

        // Save the game and shots to the database using saveAndFlush
        gameRepository.saveAndFlush(game);

        // Return the saved game
        return game;
    }

    @PostMapping("/game/addshot/{gameId}")
    public Game addShot(@RequestBody Shots newShot, @PathVariable int gameId){
        Game game = gameRepository.findById(gameId);
        game.addShot(newShot);
        shotRepository.save(newShot);
        return game;

    }

}






