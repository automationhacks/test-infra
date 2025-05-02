package io.automationhacks.testinfra.model.report_portal.launch;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class AttributesItem{

	@SerializedName("value")
	private String value;

	@SerializedName("key")
	private String key;
}