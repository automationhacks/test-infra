package io.automationhacks.testinfra.model.report_portal.testitem.get_test_items_response;

import com.google.gson.annotations.SerializedName;

public class Statistics {

    @SerializedName("executions")
    private Executions executions;

    @SerializedName("defects")
    private Defects defects;

    public Executions getExecutions() {
        return executions;
    }

    public Defects getDefects() {
        return defects;
    }
}
