package com.androidarchitecture.data.remote.responses;

import com.androidarchitecture.data.vo.Sample;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Zeki Guler on 19,January,2016
 * ©2015 Appscore. All Rights Reserved
 */
public class SampleResponse {
    private int total;
    @JsonProperty("per_page")
    private int perPage;
    @JsonProperty("current_page")
    private int currentPage;
    @JsonProperty("last_page")
    private int lastPage;
    private int from;
    private int to;
    @JsonProperty("items")
    List<Sample> sampleList;

    public int getTotal() {
        return total;
    }

    public int getPerPage() {
        return perPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getLastPage() {
        return lastPage;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    public List<Sample> getSampleList() {
        return sampleList;
    }
}
