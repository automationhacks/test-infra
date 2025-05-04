package io.automationhacks.testinfra.model.report_portal.testitem.get_test_items_response;

import com.google.gson.annotations.SerializedName;

public class Defects {

    @SerializedName("to_investigate")
    private ToInvestigate toInvestigate;

    public ToInvestigate getToInvestigate() {
        return toInvestigate;
    }
}
