package onetoone;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.restassured.http.ContentType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
//import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class WorkoutControllerTest {

    @LocalServerPort
    int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }

    @Test
    public void postTest(){
        String jsonBody = "{ \"userName\": \"mockUserName\", " +
                "\"firstName\": \"Jay\", " +
                "\"lastName\": \"Doe\", " +
                "\"email\": \"JayP23.doe@example.com\", " +
                "\"password\": \"mockPassword123\", " +
                "\"phoneNumber\": \"1234567890\" }";
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body( jsonBody) // Set the JSON body
                .when()
                .post("/users");

            int userId = 1;
        Response response1 = RestAssured.given()
                .contentType(ContentType.JSON)
                .queryParam("userId", userId)
                .when()
                .post("/workouts");

        int statusCode = response1.getStatusCode();
        assertEquals(200, statusCode);
    }




        @Test
        public void postShotsToWorkout(){
            String jsonBody = "{ \"userName\": \"mock23Use3rName\", " +
                    "\"firstName\": \"Jay\", " +
                    "\"lastName\": \"Doe\", " +
                    "\"email\": \"JayP2345.doe@example.com\", " +
                    "\"password\": \"mockPassword123\", " +
                    "\"phoneNumber\": \"1234567890\" }";
            Response response = RestAssured.given()
                    .contentType(ContentType.JSON)
                    .body( jsonBody) // Set the JSON body
                    .when()
                    .post("/users");

            int userId = 1;
            Response response1 = RestAssured.given()
                    .contentType(ContentType.JSON)
                    .queryParam("userId", userId)
                    .when()
                    .post("/workouts");


            String shots = "{ \"made\": true, " +
                    "\"value\": 3, " +
                    "\"xCoord\": 14, " +
                    "\"yCoord\": 12 }";


            Response response2 = RestAssured.given()
                    .contentType(ContentType.JSON)
                    .body(shots)
                    .when()
                    .post("/workouts/5/shots");


            String responseBody = response2.getBody().asString();
            System.out.println("Response Body: " + responseBody);

            int statusCode = response2.getStatusCode();
            assertEquals(200, statusCode);
        }



    }
