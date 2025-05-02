package io.automationhacks.testinfra.model.report_portal.launch;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Defects{

	@SerializedName("to_investigate")
	private ToInvestigate toInvestigate;
}