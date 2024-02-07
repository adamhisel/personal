package appBackend;

import appBackend.Players.PlayerService;
import appBackend.Teams.TeamService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import appBackend.Players.Player;
import appBackend.Players.PlayerRepository;
import appBackend.Teams.Team;
import appBackend.Teams.TeamRepository;
import appBackend.users.User;
import appBackend.users.UserRepository;
import appBackend.Shots.Shots;
import appBackend.Shots.ShotRepository;
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

//     Create 3 users with their machines
    /**
     *
     * @param teamRepository repository for the User entity
     * @param playerRepository repository for the Laptop entity
     * Creates a commandLine runner to enter dummy data into the database
     * As mentioned in User.java just associating the Laptop object with the User will save it into the database because of the CascadeType
     */
//    @Bean
//    CommandLineRunner initUser(TeamRepository teamRepository, PlayerRepository playerRepository, PlayerService playerService, TeamService teamService,
//    UserRepository userRepository) {
//        return args -> {
//            User user1 = new User("test1", "e@gmail.com", "pass", "123456789");
//            userRepository.save(user1);
//
//
//            Team team1 = new Team("Team1");
//            teamRepository.save(team1);
//
//            Player player1 = new Player( "jake", 4, "C", user1.id, 0);
//            playerRepository.save(player1);
//            Player player2 = new Player( "geoff", 71, "F", user1.id, 0);
//            playerRepository.save(player2);
//            Player player3 = new Player( "jack", 99, "PG", user1.id, 0);
//            playerRepository.save(player3);
//            team1.addPlayer(playerRepository.findById(1));
//            team1.addPlayer(playerRepository.findById(2));
//            team1.addPlayer(playerRepository.findById(3));
//
//            teamRepository.save(team1);
//
//            user1.addTeam(team1);
//
//            Team team2 = new Team("Bulls");
//            teamRepository.save(team2);
//            user1.addTeam(team2);
//            userRepository.save(user1);
//
//        };
//    }


}
