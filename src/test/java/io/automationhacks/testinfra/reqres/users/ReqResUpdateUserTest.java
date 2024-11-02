package io.automationhacks.testinfra.reqres.users;

import static io.restassured.RestAssured.given;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import com.epam.reportportal.annotations.attribute.Attribute;
import com.epam.reportportal.annotations.attribute.Attributes;

import io.automationhacks.testinfra.attribution.annotations.Flow;
import io.automationhacks.testinfra.attribution.annotations.OnCall;
import io.automationhacks.testinfra.attribution.annotations.Service;
import io.automationhacks.testinfra.constants.*;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@OnCall(Oncalls.AUTOMATION_HACKS)
@Flow(Flows.USERS)
@Test(groups = {Team.IDENTITY})
public class ReqResUpdateUserTest {

    @BeforeClass(alwaysRun = true)
    public void setup() {
        RestAssured.baseURI = "https://reqres.in/api";
    }

    @Test(groups = {Team.IDENTITY, Groups.REGRESSION})
    @Service(Services.UPDATE_USER)
    @Attributes(attributes = {@Attribute(key = "team", value = "identity")})
    public void testUpdate() {
        String requestBody = "{\"name\": \"morpheus\", \"job\": \"zion resident\"}";

        given().contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .put("/users/2")
                .then()
                .statusCode(200)
                .body("name", equalTo("morpheus"))
                .body("job", equalTo("zion resident"))
                .body("updatedAt", notNullValue());
    }

    @Test(groups = {Team.IDENTITY, Groups.REGRESSION})
    @Service(Services.PATCH_USER)
    @Attributes(attributes = {@Attribute(key = "team", value = "identity")})
    public void testPatch() {
        String requestBody = "{\"name\": \"morpheus\", \"job\": \"zion resident\"}";

        given().contentType(ContentType.JSON)
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
