package io.automationhacks.testinfra.reqres.users;

import static io.automationhacks.testinfra.constants.Oncalls.*;

import static org.hamcrest.Matchers.*;

import com.epam.reportportal.annotations.attribute.Attribute;
import com.epam.reportportal.annotations.attribute.Attributes;

import io.automationhacks.testinfra.attribution.annotations.Flow;
import io.automationhacks.testinfra.attribution.annotations.OnCall;
import io.automationhacks.testinfra.attribution.annotations.Service;
import io.automationhacks.testinfra.constants.Flows;
import io.automationhacks.testinfra.constants.Groups;
import io.automationhacks.testinfra.constants.Services;
import io.automationhacks.testinfra.constants.Team;
import io.automationhacks.testinfra.reqres.BaseReqResTest;

import org.testng.annotations.Test;

@OnCall(DISHA)
@Flow(Flows.USERS)
public class ReqResCreateUserTest extends BaseReqResTest {
    @Test(groups = {Team.IDENTITY, Groups.SMOKE, Groups.FAILING})
    @Service(Services.CREATE_USER)
    @Attributes(attributes = {@Attribute(key = "team", value = "identity")})
    public void testCreate() {
        String requestBody = "{\"name\": \"morpheus\", \"job\": \"leader\"}";

        getDefaultSpec()
                .body(requestBody)
                .when()
                .post("/users")
                .then()
                // Changed to 200 to make test fail, API returns 201
                .statusCode(200)
                .body("name", equalTo("morpheus"))
                .body("job", equalTo("leader"))
                .body("id", notNullValue())
                .body("createdAt", notNullValue());
    }
}
