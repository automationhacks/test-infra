package io.automationhacks.testinfra.reqres;

import static io.automationhacks.testinfra.constants.Oncalls.TEST_INFRA;
import static io.restassured.RestAssured.given;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;

import io.automationhacks.testinfra.attribution.annotations.OnCall;
import io.restassured.RestAssured;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@OnCall(TEST_INFRA)
public class ReqResDelayedTest {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://reqres.in/api";
    }

    @Test
    public void testDelayedResponse() {
        given().when()
                .get("/users?delay=3")
                .then()
                .statusCode(200)
                .time(greaterThan(3000L))
                .body("data", hasSize(greaterThan(0)));
    }
}
