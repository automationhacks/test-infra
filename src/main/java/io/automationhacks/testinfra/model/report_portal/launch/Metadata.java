package io.automationhacks.testinfra.model.report_portal.launch;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Metadata{

	@SerializedName("rp.cluster.lastRun")
	private String rpClusterLastRun;
}