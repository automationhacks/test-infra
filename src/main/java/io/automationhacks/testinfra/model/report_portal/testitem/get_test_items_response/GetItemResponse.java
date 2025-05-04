package io.automationhacks.testinfra.model.report_portal.testitem.get_test_items_response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetItemResponse {

    @SerializedName("page")
    private Page page;

    @SerializedName("content")
    private List<ContentItem> content;

    public Page getPage() {
        return page;
    }

    public List<ContentItem> getContent() {
        return content;
    }
}
