package io.automationhacks.testinfra.reqres.register;

import static io.restassured.RestAssured.given;

import static org.hamcrest.Matchers.*;

import io.automationhacks.testinfra.attribution.annotations.Flow;
import io.automationhacks.testinfra.attribution.annotations.OnCall;
import io.automationhacks.testinfra.attribution.annotations.Service;
import io.automationhacks.testinfra.constants.Flows;
import io.automationhacks.testinfra.constants.Groups;
import io.automationhacks.testinfra.constants.Oncalls;
import io.automationhacks.testinfra.constants.Services;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@OnCall(Oncalls.ENGINEER_AH)
@Flow(Flows.REGISTER)
public class ReqResRegistrationTest {

    @BeforeClass(alwaysRun = true)
    public void setup() {
        RestAssured.baseURI = "https://reqres.in/api";
    }

    @Test(groups = {Groups.SMOKE})
    @Service(Services.REGISTER)
    public void testRegisterSuccessful() {
        String requestBody = "{\"email\": \"eve.holt@reqres.in\", \"password\": \"pistol\"}";

        given().contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/register")
                .then()
                // TODO: Change this back to 200 once you've verified the negative scenario
                .statusCode(400)
                .body("id", notNullValue())
                .body("token", notNullValue());
    }

    @Test(groups = {Groups.REGRESSION})
    @Service(Services.REGISTER)
    public void testRegisterUnsuccessful() {
        String requestBody = "{\"email\": \"sydney@fife\"}";

        given().contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/register")
                .then()
                .statusCode(400)
                .body("error", equalTo("Missing password"));
    }
}
