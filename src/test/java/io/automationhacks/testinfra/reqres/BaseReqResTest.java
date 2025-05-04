package io.automationhacks.testinfra.reqres;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;

public class BaseReqResTest {
    protected static final String BASE_URI = "https://reqres.in/api";
    protected static final String API_KEY = "reqres-free-v1";

    @BeforeClass(alwaysRun = true)
    public void setup() {
        RestAssured.baseURI = BASE_URI;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    protected RequestSpecification getDefaultSpec() {
        return RestAssured.given()
                .header("x-api-key", API_KEY) // Changed to use header instead of query parameter
                .contentType("application/json");
    }
}