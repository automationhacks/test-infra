package io.automationhacks.testinfra.reqres.users;

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
@Flow(Flows.USERS)
public class ReqResUpdateUserTest extends BaseReqResTest {

    @Test(groups = { Team.ONBOARDING, Groups.SMOKE })
    @Service(value = Services.UPDATE_USER)
    @Attributes(attributes = { @Attribute(key = "team", value = "onboarding") })
    public void testUpdate() {
        String requestBody = "{\"name\": \"morpheus\", \"job\": \"zion resident\"}";

        getDefaultSpec()
                .body(requestBody)
                .when()
                .put("/users/2")
                .then()
                .statusCode(200)
                .body("name", equalTo("morpheus"))
                .body("job", equalTo("zion resident"))
                .body("updatedAt", notNullValue());
    }

    @Test(groups = { Team.ONBOARDING, Groups.REGRESSION })
    @Service(value = Services.UPDATE_USER)
    @Attributes(attributes = { @Attribute(key = "team", value = "onboarding") })
    public void testPatch() {
        String requestBody = "{\"name\": \"morpheus\", \"job\": \"zion resident\"}";

        getDefaultSpec()
                .body(requestBody)
                .when()
                .patch("/users/2")
                .then()
                .statusCode(200)
                .body("name", equalTo("morpheus"))
                .body("job", equalTo("zion resident"))
                .body("updatedAt", notNullValue());
    }
}
