package io.automationhacks.testinfra.model.report_portal.testitem.get_test_items_response;

import com.google.gson.annotations.SerializedName;

public class Executions {

    @SerializedName("total")
    private int total;

    @SerializedName("failed")
    private int failed;

    public int getTotal() {
        return total;
    }

    public int getFailed() {
        return failed;
    }
}
