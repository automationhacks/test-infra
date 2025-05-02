package io.automationhacks.testinfra.reqres.login;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import com.epam.reportportal.annotations.attribute.Attribute;
import com.epam.reportportal.annotations.attribute.Attributes;
import io.automationhacks.testinfra.attribution.annotations.Flow;
import io.automationhacks.testinfra.attribution.annotations.OnCall;
import io.automationhacks.testinfra.attribution.annotations.Service;
import io.automationhacks.testinfra.constants.*;
import io.automationhacks.testinfra.reqres.BaseReqResTest;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

@OnCall(Oncalls.RACHIT)
@Flow(Flows.LOGIN)
public class ReqResLoginTest extends BaseReqResTest {

    @Test(groups = { Team.ONBOARDING, Groups.SMOKE })
    @Service(Services.LOGIN)
    @Attributes(attributes = { @Attribute(key = "team", value = "onboarding") })
    public void testLoginSuccessful() {
        String requestBody = "{\"email\": \"eve.holt@reqres.in\", \"password\": \"cityslicka\"}";

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/login")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("token", notNullValue());
    }

    @Test(groups = { Team.ONBOARDING, Groups.REGRESSION })
    @Service(Services.LOGIN)
    @Attributes(attributes = { @Attribute(key = "team", value = "onboarding") })
    public void testLoginUnsuccessful() {
        String requestBody = "{\"email\": \"peter@klaven\"}";

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/login")
                .then()
                .statusCode(400)
                .contentType(ContentType.JSON)
                .body("error", equalTo("Missing password"));
    }
}
