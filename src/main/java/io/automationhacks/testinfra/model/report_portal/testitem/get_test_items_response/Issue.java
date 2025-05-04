package io.automationhacks.testinfra.model.report_portal.testitem.get_test_items_response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Issue {

    @SerializedName("issueType")
    private String issueType;

    @SerializedName("autoAnalyzed")
    private boolean autoAnalyzed;

    @SerializedName("ignoreAnalyzer")
    private boolean ignoreAnalyzer;

    @SerializedName("externalSystemIssues")
    private List<Object> externalSystemIssues;

    public String getIssueType() {
        return issueType;
    }

    public boolean isAutoAnalyzed() {
        return autoAnalyzed;
    }

    public boolean isIgnoreAnalyzer() {
        return ignoreAnalyzer;
    }

    public List<Object> getExternalSystemIssues() {
        return externalSystemIssues;
    }
}
