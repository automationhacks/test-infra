package io.automationhacks.testinfra.model.report_portal.testitem.get_test_items_response;

import com.google.gson.annotations.SerializedName;

public class ToInvestigate {

    @SerializedName("total")
    private int total;

    @SerializedName("ti001")
    private int ti001;

    public int getTotal() {
        return total;
    }

    public int getTi001() {
        return ti001;
    }
}
