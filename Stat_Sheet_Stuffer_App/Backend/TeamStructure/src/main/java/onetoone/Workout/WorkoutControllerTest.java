//package onetoone.Workout;
//
//import io.restassured.RestAssured;
//import io.restassured.response.Response;
//import onetoone.users.User;
//import onetoone.users.UserRepository;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.web.server.LocalServerPort;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import javax.transaction.Transactional;
//
//import static io.restassured.RestAssured.given;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@ActiveProfiles("test")
//@AutoConfigureMockMvc
//public class WorkoutControllerTest {
//
//
//    @LocalServerPort
//    int port;
//
//
//    @Before
//    public void setUp(){
//        RestAssured.port = port;
//        RestAssured.baseURI = "http://localhost";
//    }
//
//    @Test
//    public void testCreateWorkout() throws Exception {
//        // Mock the userService.getUser method to return a user
//        User mockUser = new User();
//        @Autowired
//        UserRepository userRepository;
//
//        Mockito.when(userRepository.getUser(Mockito.anyInt())).thenReturn(mockUser);
//
//        // Perform the POST request
//        mockMvc.perform(post("/workouts")
//                        .param("userId", "1"))  // Replace with the actual user ID
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("userId").value(1));  // Replace with the expected user ID in the response
//    }
//}
