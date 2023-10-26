package onetoone;


import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import onetoone.Players.Player;
import onetoone.Players.PlayerRepository;
import onetoone.Players.PlayerService;

import onetoone.Teams.Team;
import onetoone.Teams.TeamRepository;
import onetoone.Teams.TeamService;

import onetoone.users.User;
import onetoone.users.UserRepository;

import onetoone.Coaches.Coach;
import onetoone.Coaches.CoachRepository;

import onetoone.Fans.Fan;
import onetoone.Fans.FanRepository;





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
                               UserRepository userRepository, CoachRepository coachRepository, FanRepository fanRepository) {
        return args -> {
            Team team1 = new Team("cavs", "password", true);
            teamRepository.save(team1);

            Coach coach1 = new Coach("mason",0);
            coachRepository.save(coach1);










        };
    }


}
