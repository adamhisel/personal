package onetoone.users;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.transaction.Transactional;

@SpringBootApplication
public class Main {
    public static void main(String[]args){
        SpringApplication.run(Main.class, args);
    }
    @Bean
    @Transactional
    CommandLineRunner initUser(UserRepository userRepo, UserService userServe) {
        return args -> {
                User user1 = new User("test", "Tester", "jaypatel@gmail.com", "jpatel08", "2143765482");
            userServe.createUser(user1);
            User user2 = new User("test2", "Tester", "jaypatel@gmail.com", "jpatel08", "2143765482");
            userServe.createUser(user2);
            User user3 = new User("test3", "Tester", "jaypatel@gmail.com", "jpatel08", "2143765482");
            userServe.createUser(user3);

            // Update a user within a transaction
//            userServe.updateUser(2, "luke", "testUp", "passtest", "123431", "23453234");
//            userRepo.deleteById(1);

          //  userServe.getUser(2);
        };
    }

}
