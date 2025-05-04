package io.automationhacks.testinfra.model.report_portal.testitem.get_test_items_response;

import com.google.gson.annotations.SerializedName;

public class Page {

    @SerializedName("number")
    private int number;

    @SerializedName("size")
    private int size;

    @SerializedName("totalPages")
    private int totalPages;

    @SerializedName("totalElements")
    private int totalElements;

    public int getNumber() {
        return number;
    }

    public int getSize() {
        return size;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getTotalElements() {
        return totalElements;
    }
}
