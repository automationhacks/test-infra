package io.automationhacks.testinfra.model.report_portal.testitem.get_test_items_response;

import com.google.gson.annotations.SerializedName;

public class LaunchPathName {

    @SerializedName("number")
    private int number;

    @SerializedName("name")
    private String name;

    public int getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }
}
