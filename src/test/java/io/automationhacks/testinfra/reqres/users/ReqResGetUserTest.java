package io.automationhacks.testinfra.reqres.users;

import static io.automationhacks.testinfra.constants.Oncalls.*;
import static io.automationhacks.testinfra.constants.Services.*;
import static io.restassured.RestAssured.given;

import static org.hamcrest.Matchers.*;

import io.automationhacks.testinfra.attribution.annotations.Flow;
import io.automationhacks.testinfra.attribution.annotations.OnCall;
import io.automationhacks.testinfra.attribution.annotations.Service;
import io.automationhacks.testinfra.constants.Flows;
import io.automationhacks.testinfra.constants.Groups;
import io.restassured.RestAssured;

import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@OnCall(TEST_INFRA)
@Flow(Flows.USERS)
public class ReqResGetUserTest {

    @BeforeClass(alwaysRun = true)
    public void setup() {
        RestAssured.baseURI = "https://reqres.in/api";
    }

    @Test(groups = {Groups.REGRESSION})
    @OnCall(AUTOMATION_HACKS)
    @Service(LIST_USERS)
    public void testListUsers() {
        given().when()
                .get("/users?page=2")
                .then()
                .statusCode(200)
                .body("page", equalTo(2))
                .body("data", hasSize(greaterThan(0)));
    }

    @Test(groups = {Groups.REGRESSION})
    @OnCall(RACHIT)
    @Service(LIST_USERS)
    public void testSingleUser() {
        given().when()
                .get("/users/2")
                .then()
                .statusCode(200)
                .body("data.id", equalTo(2))
                .body("data.email", notNullValue());
    }

    @Test(groups = {Groups.REGRESSION})
    @OnCall(DISHA)
    @Service(LIST_USERS)
    public void testSingleUserNotFound() {
        given().when().get("/users/23").then().statusCode(404);
    }

    @Test(groups = {Groups.REGRESSION})
    @OnCall(RACHIT)
    @Service(LIST_RESOURCES)
    public void testListResource() {
        given().when().get("/unknown").then().statusCode(200).body("data", hasSize(greaterThan(0)));
    }

    @Test(groups = {Groups.REGRESSION})
    @OnCall(ROJA)
    @Service(SINGLE_RESOURCE)
    public void testSingleResource() {
        given().when()
                .get("/unknown/2")
                .then()
                .statusCode(200)
                .body("data.id", equalTo(2))
                .body("data.name", notNullValue());
    }

    @Test(groups = {Groups.REGRESSION})
    @OnCall(AUTOMATION_HACKS)
    @Service(LIST_RESOURCES)
    public void testSingleResourceNotFound() {
        given().when().get("/unknown/23").then().statusCode(404);
    }

    // TODO: Remove this test if you want to see non skipped counts
    @Test(groups = {Groups.REGRESSION})
    @OnCall(AUTOMATION_HACKS)
    @Service(LIST_RESOURCES)
    public void testThatAlwaysGetsSkipped() {
        throw new SkipException(
                "This test is used to check if slack notification includes skipped tests or not");
    }
}
