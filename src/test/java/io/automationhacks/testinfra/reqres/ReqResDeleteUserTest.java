package io.automationhacks.testinfra.reqres;

import io.automationhacks.testinfra.attribution.OnCall;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@OnCall("testinfra")
public class ReqResDeleteUserTest {

  @BeforeClass
  public void setup() {
    RestAssured.baseURI = "https://reqres.in/api";
  }

  @Test
  public void testDelete() {
    given().when().delete("/users/2").then().statusCode(204);
  }
}
