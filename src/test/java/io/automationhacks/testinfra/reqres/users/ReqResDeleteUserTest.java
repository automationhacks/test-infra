package io.automationhacks.testinfra.reqres.users;

import static io.restassured.RestAssured.given;

import io.automationhacks.testinfra.attribution.annotations.Flow;
import io.automationhacks.testinfra.attribution.annotations.OnCall;
import io.automationhacks.testinfra.attribution.annotations.Service;
import io.automationhacks.testinfra.constants.Flows;
import io.automationhacks.testinfra.constants.Groups;
import io.automationhacks.testinfra.constants.Oncalls;
import io.automationhacks.testinfra.constants.Services;
import io.restassured.RestAssured;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@OnCall(Oncalls.ENGINEER_R)
@Flow(Flows.USERS)
public class ReqResDeleteUserTest {

    @BeforeClass(alwaysRun = true)
    public void setup() {
        RestAssured.baseURI = "https://reqres.in/api";
    }

    @Test(groups = {Groups.REGRESSION})
    @Service(Services.DELETE_USER)
    public void testDelete() {
        given().when().delete("/users/2").then().statusCode(204);
    }
}
