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
public class StatsControllerTest {

    @LocalServerPort
    int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }
    @Test
    public void getStatsTest() {
        Response response = RestAssured.given()
                .when()
                .get("/stats");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

    }
    @Test
    public void getStatsByIdTest() {
        Response response = RestAssured.given()
                .when()
                .get("/stats/1");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

    }

    @Test
    public void deleteStatsTest() {
        String stats = "{ " +
                "\"assists\": 5, " +
                "\"rebounds\": 8, " +
                "\"blocks\": 2, " +
                "\"steals\": 3" +
                "}";
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(stats)
                .when()
                .post("/stats");



        Response response1 = RestAssured.given()
                .when()
                .delete("/stats/1");
        int responseCode = response1.getStatusCode();
        assertEquals(200, responseCode);
    }


    @Test
    public void addStatsToPlayerTest(){
        String playerJson = "{ " +
                "\"playerName\": \"John Doe\", " +
                "\"number\": 23, " +
                "\"position\": \"Guard\", " +
                "\"user_id\": 1" +
                "}";

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(playerJson)
                .when()
                .post("/players");

        Response response1 = RestAssured.given()
                .when()
                .put("/Stat/1/Player/1");
        int responseCode = response1.getStatusCode();
        assertEquals(200,responseCode);



    }
}
