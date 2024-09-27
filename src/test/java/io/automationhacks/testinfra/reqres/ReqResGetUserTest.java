package io.automationhacks.testinfra.reqres;

import io.automationhacks.testinfra.attribution.OnCall;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@OnCall("testinfra")
public class ReqResGetUserTest {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://reqres.in/api";
    }

    @Test
    @OnCall("rob@example.com")
    public void testListUsers() {
        given()
                .when()
                .get("/users?page=2")
                .then()
                .statusCode(200)
                .body("page", equalTo(2))
                .body("data", hasSize(greaterThan(0)));
    }

    @Test
    @OnCall("jane@example.com")
    public void testSingleUser() {
        given()
                .when()
                .get("/users/2")
                .then()
                .statusCode(200)
                .body("data.id", equalTo(2))
                .body("data.email", notNullValue());
    }

    @Test
    @OnCall("jane@example.com")
    public void testSingleUserNotFound() {
        given()
                .when()
                .get("/users/23")
                .then()
                .statusCode(404);
    }

    @Test
    @OnCall("rob@example.com")
    public void testListResource() {
        given()
                .when()
                .get("/unknown")
                .then()
                .statusCode(200)
                .body("data", hasSize(greaterThan(0)));
    }

    @Test
    public void testSingleResource() {
        given()
                .when()
                .get("/unknown/2")
                .then()
                .statusCode(200)
                .body("data.id", equalTo(2))
                .body("data.name", notNullValue());
    }

    @Test
    public void testSingleResourceNotFound() {
        given()
                .when()
                .get("/unknown/23")
                .then()
                .statusCode(404);
    }
}