package io.automationhacks.testinfra.model.report_portal.testitem.get_test_items_response;

import com.google.gson.annotations.SerializedName;

public class AttributesItem {

    @SerializedName("value")
    private String value;

    @SerializedName("key")
    private String key;

    public String getValue() {
        return value;
    }

    public String getKey() {
        return key;
    }
}
