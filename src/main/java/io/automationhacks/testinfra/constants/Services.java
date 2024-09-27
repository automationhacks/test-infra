package io.automationhacks.testinfra.constants;

public class Services {

    private static final String BASE_URL = "/api";

    // Users
    public static final String LIST_USERS = "GET " + BASE_URL + "/users?page={pageNo}";
    public static final String SINGLE_USER = "GET " + BASE_URL + "/users/{userId}";
    public static final String CREATE_USER = "POST " + BASE_URL + "/users";
    public static final String UPDATE_USER = "PUT " + BASE_URL + "/users/{userId}";
    public static final String PATCH_USER = "PATCH " + BASE_URL + "/users/{userId}";
    public static final String DELETE_USER = "DELETE " + BASE_URL + "/users/{userId}";

    // Resources
    public static final String LIST_RESOURCES = "GET " + BASE_URL + "/unknown";
    public static final String SINGLE_RESOURCE = "GET " + BASE_URL + "/unknown/{resourceId}";

    // Register and Login
    public static final String REGISTER = "POST " + BASE_URL + "/register";
    public static final String LOGIN = "POST " + BASE_URL + "/login";

    // Delayed Response
    public static final String DELAYED_RESPONSE =
            "GET " + BASE_URL + "/users?delay={delayInSeconds}";
}
