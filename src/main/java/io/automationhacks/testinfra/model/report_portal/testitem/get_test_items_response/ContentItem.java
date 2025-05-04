package io.automationhacks.testinfra.model.report_portal.testitem.get_test_items_response;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

import java.util.List;

@Data
public class ContentItem {

    @SerializedName("parent")
    private int parent;

    @SerializedName("testCaseHash")
    private int testCaseHash;

    @SerializedName("issue")
    private Issue issue;

    @SerializedName("codeRef")
    private String codeRef;

    @SerializedName("pathNames")
    private PathNames pathNames;

    @SerializedName("hasChildren")
    private boolean hasChildren;

    @SerializedName("description")
    private String description;

    @SerializedName("type")
    private String type;

    @SerializedName("uuid")
    private String uuid;

    @SerializedName("launchId")
    private int launchId;

    @SerializedName("path")
    private String path;

    @SerializedName("patternTemplates")
    private List<String> patternTemplates;

    @SerializedName("name")
    private String name;

    @SerializedName("attributes")
    private List<AttributesItem> attributes;

    @SerializedName("startTime")
    private String startTime;

    @SerializedName("id")
    private int id;

    @SerializedName("endTime")
    private String endTime;

    @SerializedName("parameters")
    private List<Object> parameters;

    @SerializedName("hasStats")
    private boolean hasStats;

    @SerializedName("uniqueId")
    private String uniqueId;

    @SerializedName("status")
    private String status;

    @SerializedName("statistics")
    private Statistics statistics;

    @SerializedName("testCaseId")
    private String testCaseId;

    public int getParent() {
        return parent;
    }

    public int getTestCaseHash() {
        return testCaseHash;
    }

    public Issue getIssue() {
        return issue;
    }

    public String getCodeRef() {
        return codeRef;
    }

    public PathNames getPathNames() {
        return pathNames;
    }

    public boolean isHasChildren() {
        return hasChildren;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public String getUuid() {
        return uuid;
    }

    public int getLaunchId() {
        return launchId;
    }

    public String getPath() {
        return path;
    }

    public List<String> getPatternTemplates() {
        return patternTemplates;
    }

    public String getName() {
        return name;
    }

    public List<AttributesItem> getAttributes() {
        return attributes;
    }

    public String getStartTime() {
        return startTime;
    }

    public int getId() {
        return id;
    }

    public String getEndTime() {
        return endTime;
    }

    public List<Object> getParameters() {
        return parameters;
    }

    public boolean isHasStats() {
        return hasStats;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public String getStatus() {
        return status;
    }

    public Statistics getStatistics() {
        return statistics;
    }

    public String getTestCaseId() {
        return testCaseId;
    }
}
