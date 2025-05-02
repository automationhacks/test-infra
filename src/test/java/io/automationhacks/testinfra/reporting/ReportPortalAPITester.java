package io.automationhacks.testinfra.reporting;

import java.io.IOException;

import org.testng.annotations.Test;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ReportPortalAPITester {
    @Test
    public void getLatestLaunchId() throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("http://localhost:8080/api/v1/test_infra/launch/latest")
                .get()
                .addHeader("Accept", "application/json")
                .addHeader("Authorization",
                        "Bearer test-infra_iKPprGYfS96dyOQDD1tJfi7cRGhSu6zMrUolU9olbfnOdCrb8qWDE4O8CvxloIPc")
                .build();
        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());
    }
}
