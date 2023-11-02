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

    @PostMapping(path = "/game")
    Game createGame(@RequestBody Game game) {
        if (game == null) {
            throw new RuntimeException("Invalid request");
        }

        // Save the game to establish its ID
        game = gameRepository.save(game);

        // Ensure the Shots are associated with the game
        List<Shots> shots = game.getShots();
        if (shots != null) {
            for (Shots shot : shots) {
                shot.setGame(game);
            }
        }

        // Save the Shots (assuming you have a shotRepository)
        if (shots != null) {
            shotRepository.saveAll(shots);
        }

        return game;
    }
    @PostMapping(path = "/game/addShot")
    public Game addShotToGame(@RequestParam int playerId, @RequestParam int gameId, @RequestBody Shots newShot) {
        Game game = gameRepository.findById(gameId);
        Player player = playerRepository.findById(playerId);

        // Check if the player belongs to the game's team (you may need to modify this based on your data model)
        newShot.setGame(game); // Save the new shot

        game.addShot(newShot); // Add the shot to the game's shots list
        gameRepository.save(game); // Save the updated game

        return game;
    }
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

}






