package io.automationhacks.testinfra.model.report_portal.launch;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

import java.util.List;

@Data
public class GetLatestLaunchByProjectResponse {

    @SerializedName("page")
    private Page page;

    @SerializedName("content")
    private List<ContentItem> content;
}
