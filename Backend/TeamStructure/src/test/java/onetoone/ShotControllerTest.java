package onetoone;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class ShotControllerTest {

    @LocalServerPort
    int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }

    @Test
    public void getShotTest() {
        Response response = RestAssured.given()
                .when()
                .get("/shots");


        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

    }

    @Test
    public void addShotTest(){
        String mockShot = "{ \"made\": true, " +
                "\"value\": 3, " +
                "\"xCoord\": 14, " +
                "\"yCoord\": 12 }";
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(mockShot)
                .when()
                .post("/shots");
        int statusCode = response.getStatusCode();
        assertEquals(200,statusCode);
    }







}
