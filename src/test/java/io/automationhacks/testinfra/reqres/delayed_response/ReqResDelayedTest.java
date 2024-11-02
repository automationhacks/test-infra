package io.automationhacks.testinfra.reqres.delayed_response;

import static io.restassured.RestAssured.given;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;

import com.epam.reportportal.annotations.attribute.Attribute;
import com.epam.reportportal.annotations.attribute.Attributes;

import io.automationhacks.testinfra.attribution.annotations.Flow;
import io.automationhacks.testinfra.attribution.annotations.OnCall;
import io.automationhacks.testinfra.attribution.annotations.Service;
import io.automationhacks.testinfra.constants.*;
import io.restassured.RestAssured;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@OnCall(Oncalls.DISHA)
@Flow(Flows.DELAYED_RESPONSE)
public class ReqResDelayedTest {

    @BeforeClass(alwaysRun = true)
    public void setup() {
        RestAssured.baseURI = "https://reqres.in/api";
    }

    @Test(groups = {Team.PERFORMANCE, Groups.SMOKE, Groups.REGRESSION, Groups.SLOW})
    @Attributes(attributes = {@Attribute(key = "team", value = "performance")})
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
