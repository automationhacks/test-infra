package reqres;

import io.automationhacks.testinfra.attribution.OnCall;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@OnCall("testinfra")
public class ReqResApiTest {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://reqres.in/api";
    }

    @Test
    @OnCall("rob@example.com")
    public void testListUsers() {
        given()
                .when()
                .get("/users?page=2")
                .then()
                .statusCode(200)
                .body("page", equalTo(2))
                .body("data", hasSize(greaterThan(0)));
    }

    @Test
    @OnCall("jane@example.com")
    public void testSingleUser() {
        given()
                .when()
                .get("/users/2")
                .then()
                .statusCode(200)
                .body("data.id", equalTo(2))
                .body("data.email", notNullValue());
    }

    @Test
    @OnCall("jane@example.com")
    public void testSingleUserNotFound() {
        given()
                .when()
                .get("/users/23")
                .then()
                .statusCode(404);
    }

    @Test
    @OnCall("rob@example.com")
    public void testListResource() {
        given()
                .when()
                .get("/unknown")
                .then()
                .statusCode(200)
                .body("data", hasSize(greaterThan(0)));
    }

    @Test
    public void testSingleResource() {
        given()
                .when()
                .get("/unknown/2")
                .then()
                .statusCode(200)
                .body("data.id", equalTo(2))
                .body("data.name", notNullValue());
    }

    @Test
    public void testSingleResourceNotFound() {
        given()
                .when()
                .get("/unknown/23")
                .then()
                .statusCode(404);
    }

    @Test
    public void testCreate() {
        String requestBody = "{\"name\": \"morpheus\", \"job\": \"leader\"}";

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/users")
                .then()
                .statusCode(201)
                .body("name", equalTo("morpheus"))
                .body("job", equalTo("leader"))
                .body("id", notNullValue())
                .body("createdAt", notNullValue());
    }

    @Test
    public void testUpdate() {
        String requestBody = "{\"name\": \"morpheus\", \"job\": \"zion resident\"}";

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .put("/users/2")
                .then()
                .statusCode(200)
                .body("name", equalTo("morpheus"))
                .body("job", equalTo("zion resident"))
                .body("updatedAt", notNullValue());
    }

    @Test
    public void testPatch() {
        String requestBody = "{\"name\": \"morpheus\", \"job\": \"zion resident\"}";

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .patch("/users/2")
                .then()
                .statusCode(200)
                .body("name", equalTo("morpheus"))
                .body("job", equalTo("zion resident"))
                .body("updatedAt", notNullValue());
    }

    @Test
    public void testDelete() {
        given()
                .when()
                .delete("/users/2")
                .then()
                .statusCode(204);
    }

    @Test
    public void testRegisterSuccessful() {
        String requestBody = "{\"email\": \"eve.holt@reqres.in\", \"password\": \"pistol\"}";

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/register")
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("token", notNullValue());
    }

    @Test
    public void testRegisterUnsuccessful() {
        String requestBody = "{\"email\": \"sydney@fife\"}";

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/register")
                .then()
                .statusCode(400)
                .body("error", equalTo("Missing password"));
    }

    @Test
    public void testLoginSuccessful() {
        String requestBody = "{\"email\": \"eve.holt@reqres.in\", \"password\": \"cityslicka\"}";

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/login")
                .then()
                .statusCode(200)
                .body("token", notNullValue());
    }

    @Test
    public void testLoginUnsuccessful() {
        String requestBody = "{\"email\": \"peter@klaven\"}";

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/login")
                .then()
                .statusCode(400)
                .body("error", equalTo("Missing password"));
    }

    @Test
    public void testDelayedResponse() {
        given()
                .when()
                .get("/users?delay=3")
                .then()
                .statusCode(200)
                .time(greaterThan(3000L))
                .body("data", hasSize(greaterThan(0)));
    }
}