package io.automationhacks.testinfra.reqres.users;

import static io.automationhacks.testinfra.constants.Oncalls.TEST_INFRA;
import static io.restassured.RestAssured.given;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import io.automationhacks.testinfra.attribution.annotations.OnCall;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@OnCall(TEST_INFRA)
public class ReqResUpdateUserTest {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://reqres.in/api";
    }

    @Test()
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

    @Test
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
