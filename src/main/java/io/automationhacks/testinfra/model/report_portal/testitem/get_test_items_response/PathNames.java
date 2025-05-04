package io.automationhacks.testinfra.model.report_portal.testitem.get_test_items_response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PathNames {

    @SerializedName("launchPathName")
    private LaunchPathName launchPathName;

    @SerializedName("itemPaths")
    private List<ItemPathsItem> itemPaths;

    public LaunchPathName getLaunchPathName() {
        return launchPathName;
    }

    public List<ItemPathsItem> getItemPaths() {
        return itemPaths;
    }
}
