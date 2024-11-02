package io.automationhacks.testinfra.reqres.register;

import static io.restassured.RestAssured.given;

import static org.hamcrest.Matchers.*;

import com.epam.reportportal.annotations.attribute.Attribute;
import com.epam.reportportal.annotations.attribute.Attributes;

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

@OnCall(Oncalls.AUTOMATION_HACKS)
@Flow(Flows.REGISTER)
public class ReqResRegistrationTest {

    @BeforeClass(alwaysRun = true)
    public void setup() {
        RestAssured.baseURI = "https://reqres.in/api";
    }

    @Test(groups = {Groups.SMOKE})
    @Service(Services.REGISTER)
    @Attributes(attributes = {@Attribute(key = "team", value = "onboarding")})
    public void testRegisterSuccessful() {
        String requestBody = "{\"email\": \"eve.holt@reqres.in\", \"password\": \"pistol\"}";

        given().contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/register")
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("token", notNullValue());
    }

    @Test(groups = {Groups.REGRESSION})
    @Service(Services.REGISTER)
    @Attributes(attributes = {@Attribute(key = "team", value = "onboarding")})
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
