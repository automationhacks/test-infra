package io.automationhacks.testinfra.reqres.login;

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

@OnCall(Oncalls.RACHIT)
@Flow(Flows.LOGIN)
public class ReqResLoginTest {

    @BeforeClass(alwaysRun = true)
    public void setup() {
        RestAssured.baseURI = "https://reqres.in/api";
    }

    @Test(groups = {Groups.SMOKE})
    @Service(Services.LOGIN)
    public void testLoginSuccessful() {
        String requestBody = "{\"email\": \"eve.holt@reqres.in\", \"password\": \"cityslicka\"}";

        given().contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/login")
                .then()
                .statusCode(200)
                .body("token", notNullValue());
    }

    @Test(groups = {Groups.REGRESSION})
    @Service(Services.LOGIN)
    public void testLoginUnsuccessful() {
        String requestBody = "{\"email\": \"peter@klaven\"}";

        given().contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/login")
                .then()
                .statusCode(400)
                .body("error", equalTo("Missing password"));
    }
}
