package io.automationhacks.testinfra.reqres.delete;

import static io.automationhacks.testinfra.constants.Oncalls.TEST_INFRA;
import static io.restassured.RestAssured.given;

import io.automationhacks.testinfra.attribution.OnCall;
import io.restassured.RestAssured;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@OnCall(TEST_INFRA)
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
