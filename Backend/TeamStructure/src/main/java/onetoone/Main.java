package onetoone;

import onetoone.Players.PlayerService;
import onetoone.Teams.TeamService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import onetoone.Players.Player;
import onetoone.Players.PlayerRepository;
import onetoone.Teams.Team;
import onetoone.Teams.TeamRepository;
//import onetoone.users.User;
//import onetoone.users.UserRepository;
//import onetoone.users.UserService;




/**
 * 
 * @author Vivek Bengre
 * 
 */ 

@SpringBootApplication
class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }


    @Bean
    CommandLineRunner initUser(TeamRepository teamRepository, PlayerRepository playerRepository, PlayerService playerService, TeamService teamService) {
        return args -> {
            Team team1 = new Team("John");
            teamRepository.save(team1);

            Player player1 = new Player( "jake", 4, "C");
            playerRepository.save(player1);
            Player player2 = new Player( "geoff", 71, "F");
            playerRepository.save(player2);
            Player player3 = new Player( "jack", 99, "PG");
            playerRepository.save(player3);
            team1.addPlayer(playerRepository.findById(1));






        };
    }

}
