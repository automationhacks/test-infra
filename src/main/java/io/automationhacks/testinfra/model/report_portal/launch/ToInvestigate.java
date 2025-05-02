package io.automationhacks.testinfra.model.report_portal.launch;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class ToInvestigate {

    @SerializedName("total")
    private int total;

    @SerializedName("ti001")
    private int ti001;
}
