package io.automationhacks.testinfra.reqres.users;

import static io.automationhacks.testinfra.constants.Oncalls.*;
import static io.restassured.RestAssured.given;

import static org.hamcrest.Matchers.*;

import com.epam.reportportal.annotations.attribute.Attribute;
import com.epam.reportportal.annotations.attribute.Attributes;

import io.automationhacks.testinfra.attribution.annotations.Flow;
import io.automationhacks.testinfra.attribution.annotations.OnCall;
import io.automationhacks.testinfra.attribution.annotations.Service;
import io.automationhacks.testinfra.constants.Flows;
import io.automationhacks.testinfra.constants.Groups;
import io.automationhacks.testinfra.constants.Services;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@OnCall(DISHA)
@Flow(Flows.USERS)
public class ReqResCreateUserTest {

    @BeforeClass(alwaysRun = true)
    public void setup() {
        RestAssured.baseURI = "https://reqres.in/api";
    }

    @Test(groups = {Groups.SMOKE})
    @Service(Services.CREATE_USER)
    @Attributes(attributes = {@Attribute(key = "team", value = "identity")})
    public void testCreate() {
        String requestBody = "{\"name\": \"morpheus\", \"job\": \"leader\"}";

        given().contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/users")
                .then()
                .statusCode(201)
                .body("name", equalTo("morpheus"))
                .body("job", equalTo("leader"))
                .body("id", notNullValue())
                .body("createdAt", notNullValue());
    }
}
