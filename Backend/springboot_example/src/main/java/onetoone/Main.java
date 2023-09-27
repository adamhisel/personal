package onetoone;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import onetoone.Players.Player;
import onetoone.Players.PlayerRepository;
import onetoone.Teams.Team;
import onetoone.Teams.TeamRepository;

/**
 * 
 * @author Vivek Bengre
 * 
 */ 

@SpringBootApplication
//@EnableJpaRepositories
class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    // Create 3 users with their machines
    /**
     * 
     * @param userRepository repository for the User entity
     * @param laptopRepository repository for the Laptop entity
     * Creates a commandLine runner to enter dummy data into the database
     * As mentioned in Team.java just associating the Laptop object with the User will save it into the database because of the CascadeType
     */
    @Bean
    CommandLineRunner initUser(TeamRepository teamRepository, PlayerRepository playerRepository) {
        return args -> {
            Team team1 = new Team("John");
            Team team2 = new Team("Jane");
            Team team3 = new Tema("Justin");
            Player player1 = new Player("jeff", 87, "C");
            Player player2 = new Player("chris", 90, "F");
            Laptop player3 = new Player("jake", 3, "PF");
            team1.setPlayer(player1);
            team2.setPlayer(player2);
            team3.setPlayer(player3);
            teamRepository.save(team1);
            teamRepository.save(team2);
            teamRepository.save(team3);

        };
    }

}
