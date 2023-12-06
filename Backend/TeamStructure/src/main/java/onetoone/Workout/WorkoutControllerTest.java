package onetoone.Workout;

import io.restassured.RestAssured;
import onetoone.Shots.ShotRepository;
import onetoone.users.User;
import onetoone.users.UserRepository;
import onetoone.users.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class WorkoutControllerTest {
@Mock
    UserRepository userRepository;
@Mock
WorkoutRepository workoutRepository;
@Mock
    ShotRepository shotRepository;
@Mock
    UserService userService;
@Mock
WorkoutController workoutController;

    @LocalServerPort
    int port;


    @Before
    public void setUp(){
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }

    @Test
    public void testCreateWorkout() {
        // Mocking the userService to return a user when getUser is called
        User mockUser = new User(); // Create a mock user
        Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn(mockUser);

        // Mocking the workoutRepository to return the saved workout
        Workout mockWorkout = new Workout(mockUser); // Create a mock workout
        Mockito.when(workoutRepository.save(any(Workout.class))).thenReturn(mockWorkout);

        // Perform the mocked request
        Mockito.when(workoutController.createWorkout(Mockito.anyInt())).thenCallRealMethod();

        // Verifying the interactions
        workoutController.createWorkout(1);

        // Asserting the interactions
        Mockito.verify(userRepository).findById(1);
        Mockito.verify(workoutRepository).save(any(Workout.class));
    }
}





