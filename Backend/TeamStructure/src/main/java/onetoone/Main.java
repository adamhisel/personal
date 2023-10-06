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
import onetoone.users.User;
import onetoone.users.UserRepository;
import onetoone.users.UserService;


import java.time.temporal.TemporalAmount;

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

    // Create 3 users with their machines
//    /**
//     *
//     * @param teamRepository repository for the User entity
//     * @param playerRepository repository for the Laptop entity
//     * Creates a commandLine runner to enter dummy data into the database
//     * As mentioned in User.java just associating the Laptop object with the User will save it into the database because of the CascadeType
//     */
//    @Bean
//    CommandLineRunner initUser(TeamRepository teamRepository, PlayerRepository playerRepository, PlayerService playerService, TeamService teamService) {
//        return args -> {
//            Team team1 = new Team("John");
//            Team team2 = new Team("Jane");
//            Team team3 = new Team("Justin");
//            Player player1 = new Player( "jake", 4, "C");
//            Player player2 = new Player( "geoff", 71, "F");
//            Player player3 = new Player( "jack", 99, "PG");
//            team1.setPlayer(player1);
//            team2.setPlayer(player2);
//            team3.setPlayer(player3);
//            player1.setTeam(team1);
//            player2.setTeam(team2);
//            player3.setTeam(team3);
//            teamRepository.save(team1);
//            teamRepository.save(team2);
//            teamRepository.save(team3);


//          Player p = new Player("jay", 80, "PG");
//          playerService.createPlayer(p);

//          playerService.updatePlayer(1, "jose", 8, "PF");



  //      };
   // }

}
