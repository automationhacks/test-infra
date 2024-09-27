package io.automationhacks.testinfra.reqres.delayed_response;

import static io.automationhacks.testinfra.constants.Oncalls.TEST_INFRA;
import static io.restassured.RestAssured.given;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;

import io.automationhacks.testinfra.attribution.annotations.Flow;
import io.automationhacks.testinfra.attribution.annotations.OnCall;
import io.automationhacks.testinfra.attribution.annotations.Service;
import io.automationhacks.testinfra.constants.Flows;
import io.automationhacks.testinfra.constants.Groups;
import io.automationhacks.testinfra.constants.Services;
import io.restassured.RestAssured;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@OnCall(TEST_INFRA)
@Flow(Flows.DELAYED_RESPONSE)
public class ReqResDelayedTest {

    @BeforeClass(alwaysRun = true)
    public void setup() {
        RestAssured.baseURI = "https://reqres.in/api";
    }

    @Test(groups = {Groups.SMOKE, Groups.REGRESSION, Groups.SLOW})
    @Service(Services.DELAYED_RESPONSE)
    public void testDelayedResponse() {
        given().when()
                .get("/users?delay=3")
                .then()
                .statusCode(200)
                .time(greaterThan(3000L))
                .body("data", hasSize(greaterThan(0)));
    }
}
