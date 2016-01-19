package com.androidarchitecture.data.remote.responses;

import com.androidarchitecture.data.vo.Sample;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by zeki on 17/01/2016.
 */
public class SampleResponseData {
    @JsonProperty("data")
    private  SampleResponse mSampleResponse;

    public SampleResponse getSampleResponse() {
        return mSampleResponse;
    }
}
