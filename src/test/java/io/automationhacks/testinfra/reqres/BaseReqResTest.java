package io.automationhacks.testinfra.reqres;

import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;

public class BaseReqResTest {
    protected static final String BASE_URI = "https://reqres.in/api";

    @BeforeClass(alwaysRun = true)
    public void setup() {
        RestAssured.baseURI = BASE_URI;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }
}