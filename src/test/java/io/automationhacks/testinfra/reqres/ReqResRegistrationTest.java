package io.automationhacks.testinfra.reqres;

import io.automationhacks.testinfra.attribution.OnCall;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@OnCall("testinfra")
public class ReqResRegistrationTest {

  @BeforeClass
  public void setup() {
    RestAssured.baseURI = "https://reqres.in/api";
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
}
