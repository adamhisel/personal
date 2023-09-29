package onetoone;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import onetoone.Players.Player;
import onetoone.Players.PlayerRepository;
import onetoone.Teams.Team;
import onetoone.Teams.TeamRepository;

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
    /**
     * 
     * @param teamRepository repository for the User entity
     * @param playerRepository repository for the Laptop entity
     * Creates a commandLine runner to enter dummy data into the database
     * As mentioned in User.java just associating the Laptop object with the User will save it into the database because of the CascadeType
     */
    @Bean
    CommandLineRunner initUser(TeamRepository teamRepository, PlayerRepository playerRepository) {
        return args -> {
            Team user1 = new Team("John", "john@somemail.com");
            Team user2 = new Team("Jane", "jane@somemail.com");
            Team user3 = new Team("Justin", "justin@somemail.com");
            Player laptop1 = new Player( "jake", 4, "C");
            Player laptop2 = new Player( "geoff", 71, "F");
            Player laptop3 = new Player( "jack", 99, "PG");
            user1.setLaptop(laptop1);
            user2.setLaptop(laptop2);
            user3.setLaptop(laptop3);            
            teamRepository.save(user1);
            teamRepository.save(user2);
            teamRepository.save(user3);

        };
    }

}
