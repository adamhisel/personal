package onetoone.Game;
import onetoone.Players.Player;
import onetoone.Players.PlayerRepository;
import onetoone.Shots.Shots;
import onetoone.Teams.Team;
import onetoone.Shots.ShotRepository;
import onetoone.Teams.TeamRepository;


import org.apache.catalina.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import onetoone.Shots.ShotsService;

import java.util.List;

@RestController
public class GameController {
    @Autowired
    GameRepository gameRepository;
    @Autowired
    ShotRepository shotRepository;

    @Autowired
    ShotsService shotsService;

    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    TeamRepository teamRepository;

    @GetMapping(path = "/games")
    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }


    @PostMapping("/games/{teamId}")
    public Game createGame(@PathVariable int teamId) {
        // Initialize a new game without shots or players yet
        Game game = new Game();
        Team team  = teamRepository.findById(teamId);
        List <Player> playersList = team.players;
        for (Player player : playersList) {
            player.addGame(game);
            playerRepository.save(player);
        }

        game.setTeam(team);
        team.addGame(game);
        teamRepository.save(team);
        return gameRepository.save(game);

    }


    @PostMapping("/games/{gameId}/team-shots")
    public ResponseEntity<Void> addTeamShotsToGame(@PathVariable int gameId, @RequestBody List<Shots> teamShots) {
        Game game = gameRepository.findById(gameId);


        for (Shots shot : teamShots) {
            shot.setGame(game);
            shotsService.saveShot(shot);
        }


        return ResponseEntity.ok().build();
    }


    @PostMapping("/games/{gameId}/players/{playerId}/shots")
    public ResponseEntity<Void> addPlayerShotsToGame(@PathVariable int gameId, @PathVariable int playerId, @RequestBody List<Shots> playerShots) {
        Game game = gameRepository.findById(gameId);
        Player player = playerRepository.findById(playerId);


        for (Shots shot : playerShots) {
            shot.setGame(game); // Link shot to game
            shot.setPlayer(player); // Link shot to player
            shotsService.saveShot(shot);
        }


        return ResponseEntity.ok().build();
    }


    @GetMapping("/games/{gameId}/shots")
    public List<Shots> getShotsFromGame(@PathVariable int gameId) {
        Game game = gameRepository.findById(gameId);
        return game.getTeamShots();
    }


    @GetMapping("/games/{gameId}/teams/{teamId}/shots")
    public List<Shots> getShotsByTeamInGame(@PathVariable int gameId, @PathVariable int teamId) {
        Game game = gameRepository.findById(gameId);
        return game.getTeamShots(teamId);
    }


    @GetMapping("/games/{gameId}/players/{playerId}/shots")
    public List<Shots> getShotsByPlayerInGame(@PathVariable int gameId, @PathVariable int playerId) {
        Game game = gameRepository.findById(gameId);
        return game.getPlayerShots(playerId);
    }


}






