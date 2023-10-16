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

//     Create 3 users with their machines
    /**
     *
     * @param teamRepository repository for the User entity
     * @param playerRepository repository for the Laptop entity
     * Creates a commandLine runner to enter dummy data into the database
     * As mentioned in User.java just associating the Laptop object with the User will save it into the database because of the CascadeType
     */
    @Bean
    CommandLineRunner initUser(TeamRepository teamRepository, PlayerRepository playerRepository, PlayerService playerService, TeamService teamService,
    UserRepository userRepository) {
        return args -> {
            User user1 = new User("mharsh", "mharsh@gmail.com", "sdgasfa", "123456789");
            userRepository.save(user1);


            Team team1 = new Team("John");
            teamRepository.save(team1);

            Player player1 = new Player( "jake", 4, "C", user1.id);
            playerRepository.save(player1);
            Player player2 = new Player( "geoff", 71, "F", user1.id);
            playerRepository.save(player2);
            Player player3 = new Player( "jack", 99, "PG", user1.id);
            playerRepository.save(player3);
            team1.addPlayer(playerRepository.findById(1));
            team1.addPlayer(playerRepository.findById(2));
            team1.addPlayer(playerRepository.findById(3));

            teamRepository.save(team1);


        };
    }


}
