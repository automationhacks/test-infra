package io.automationhacks.testinfra.reqres.users;

import static io.restassured.RestAssured.given;

import com.epam.reportportal.annotations.attribute.Attribute;
import com.epam.reportportal.annotations.attribute.Attributes;

import io.automationhacks.testinfra.attribution.annotations.Flow;
import io.automationhacks.testinfra.attribution.annotations.OnCall;
import io.automationhacks.testinfra.attribution.annotations.Service;
import io.automationhacks.testinfra.constants.*;
import io.restassured.RestAssured;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Random;

@OnCall(Oncalls.RACHIT)
@Flow(Flows.USERS)
public class ReqResDeleteUserTest {

    @BeforeClass(alwaysRun = true)
    public void setup() {
        RestAssured.baseURI = "https://reqres.in/api";
    }

    @Test(
            groups = {Team.IDENTITY, Groups.REGRESSION},
            invocationCount = 5)
    @Service(Services.DELETE_USER)
    @Attributes(attributes = {@Attribute(key = "team", value = "identity")})
    public void testDelete() {
        var statusCodes = List.of(500, 503, 401, 204);
        // TODO: Flaky test example, change to 204 to fix
        given().when()
                .delete("/users/2")
                .then()
                .statusCode(statusCodes.get(new Random().nextInt(statusCodes.size())));
    }
}
