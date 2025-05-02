package io.automationhacks.testinfra.model.report_portal.launch;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Page{

	@SerializedName("number")
	private int number;

	@SerializedName("size")
	private int size;

	@SerializedName("totalPages")
	private int totalPages;

	@SerializedName("totalElements")
	private int totalElements;
}