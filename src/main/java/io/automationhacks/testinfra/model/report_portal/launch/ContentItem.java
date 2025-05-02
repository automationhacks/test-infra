package io.automationhacks.testinfra.model.report_portal.launch;

import java.util.List;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class ContentItem{

	@SerializedName("owner")
	private String owner;

	@SerializedName("metadata")
	private Metadata metadata;

	@SerializedName("analysing")
	private List<Object> analysing;

	@SerializedName("uuid")
	private String uuid;

	@SerializedName("rerun")
	private boolean rerun;

	@SerializedName("mode")
	private String mode;

	@SerializedName("number")
	private int number;

	@SerializedName("hasRetries")
	private boolean hasRetries;

	@SerializedName("name")
	private String name;

	@SerializedName("approximateDuration")
	private Object approximateDuration;

	@SerializedName("startTime")
	private String startTime;

	@SerializedName("attributes")
	private List<AttributesItem> attributes;

	@SerializedName("id")
	private int id;

	@SerializedName("endTime")
	private String endTime;

	@SerializedName("lastModified")
	private String lastModified;

	@SerializedName("status")
	private String status;

	@SerializedName("statistics")
	private Statistics statistics;
}