package io.automationhacks.testinfra.reqres.users;

import static io.restassured.RestAssured.given;

import com.epam.reportportal.annotations.attribute.Attribute;
import com.epam.reportportal.annotations.attribute.Attributes;

import io.automationhacks.testinfra.attribution.annotations.Flow;
import io.automationhacks.testinfra.attribution.annotations.OnCall;
import io.automationhacks.testinfra.attribution.annotations.Service;
import io.automationhacks.testinfra.constants.*;
import io.automationhacks.testinfra.reqres.BaseReqResTest;

import org.testng.annotations.Test;

@OnCall(Oncalls.RACHIT)
@Flow(Flows.USERS)
public class ReqResDeleteUserTest extends BaseReqResTest {

    @Test(groups = { Team.ONBOARDING, Groups.SMOKE })
    @Service(value = Services.DELETE_USER)
    @Attributes(attributes = { @Attribute(key = "team", value = "onboarding") })
    public void testDelete() {
        getDefaultSpec()
                .when()
                .delete("/users/2")
                .then()
                .statusCode(204);
    }
}
