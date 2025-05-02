package io.automationhacks.testinfra.model.report_portal.launch;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Executions{

	@SerializedName("total")
	private int total;

	@SerializedName("passed")
	private int passed;

	@SerializedName("failed")
	private int failed;
}