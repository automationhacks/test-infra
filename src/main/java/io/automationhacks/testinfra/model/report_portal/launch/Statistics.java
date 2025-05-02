package io.automationhacks.testinfra.model.report_portal.launch;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Statistics{

	@SerializedName("executions")
	private Executions executions;

	@SerializedName("defects")
	private Defects defects;
}