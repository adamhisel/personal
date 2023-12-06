package onetoone.users;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class UserControllerTest {

    @LocalServerPort
    int port;


    @Before
    public void setUp(){
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }

//    @Test
//    @Transactional
//    public void test() {
//        User user = new User("jpatel", "jay", "patel", "@gmail.com", "test", "123456789");
//        Response response = given()
//                .contentType("application/json")
//                .body(user)
//                .when()
//                .post("/users");
//
//        int statusCode = response.getStatusCode();
//        assertEquals(200, statusCode);
//
//        User createdUser = response.as(User.class);
//
//        // Compare individual fields instead of using assertEquals
//        assertEquals(user.getUserName(), createdUser.getUserName());
//        assertEquals(user.getFirstName(), createdUser.getFirstName());
//        assertEquals(user.getLastName(), createdUser.getLastName());
//        assertEquals(user.getEmail(), createdUser.getEmail());
//        assertEquals(user.getPassword(), createdUser.getPassword());
//        assertEquals(user.getPhoneNumber(), createdUser.getPhoneNumber());
//    }




}
