package onetoone;


import onetoone.Teams.Team;
import onetoone.users.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import onetoone.Players.PlayerRepository;

import onetoone.Teams.TeamRepository;

import onetoone.users.UserRepository;

import onetoone.Coaches.CoachRepository;

import onetoone.Fans.FanRepository;
import org.springframework.context.annotation.ComponentScan;


/**
 * 
 * @author Vivek Bengre
 * 
 */ 

@SpringBootApplication
@ComponentScan(basePackages = {"onetoone.websocket"})
@ComponentScan(basePackages = {"onetoone.websocket_liveUpdate.chat"})
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
    CommandLineRunner initUser(TeamRepository teamRepository, PlayerRepository playerRepository,
                               UserRepository userRepository, CoachRepository coachRepository, FanRepository fanRepository) {
        return args -> {


        };
    }


}
