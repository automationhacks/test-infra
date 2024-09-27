package io.automationhacks.testinfra.reqres.users;

import static io.automationhacks.testinfra.constants.Oncalls.*;
import static io.automationhacks.testinfra.constants.Services.*;
import static io.restassured.RestAssured.given;

import static org.hamcrest.Matchers.*;

import io.automationhacks.testinfra.attribution.annotations.Flow;
import io.automationhacks.testinfra.attribution.annotations.OnCall;
import io.automationhacks.testinfra.attribution.annotations.Service;
import io.automationhacks.testinfra.constants.Flows;
import io.restassured.RestAssured;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@OnCall(TEST_INFRA)
@Flow(Flows.USERS)
public class ReqResGetUserTest {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://reqres.in/api";
    }

    @Test
    @OnCall(ROB)
    @Service(LIST_USERS)
    public void testListUsers() {
        given().when()
                .get("/users?page=2")
                .then()
                .statusCode(200)
                .body("page", equalTo(2))
                .body("data", hasSize(greaterThan(0)));
    }

    @Test
    @OnCall(JANE)
    @Service(LIST_USERS)
    public void testSingleUser() {
        given().when()
                .get("/users/2")
                .then()
                .statusCode(200)
                .body("data.id", equalTo(2))
                .body("data.email", notNullValue());
    }

    @Test
    @OnCall(JANE)
    @Service(LIST_USERS)
    public void testSingleUserNotFound() {
        given().when().get("/users/23").then().statusCode(404);
    }

    @Test
    @OnCall(ROB)
    @Service(LIST_RESOURCES)
    public void testListResource() {
        given().when().get("/unknown").then().statusCode(200).body("data", hasSize(greaterThan(0)));
    }

    @Test
    @Service(SINGLE_RESOURCE)
    public void testSingleResource() {
        given().when()
                .get("/unknown/2")
                .then()
                .statusCode(200)
                .body("data.id", equalTo(2))
                .body("data.name", notNullValue());
    }

    @Test
    @Service(LIST_RESOURCES)
    public void testSingleResourceNotFound() {
        given().when().get("/unknown/23").then().statusCode(404);
    }
}
