package com.androidarchitecture.data.remote.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Zeki Guler on 24,March,2016
 * ©2015 Appscore. All Rights Reserved
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class SuccessResponse {
    public SuccessResponseData data;

    @JsonIgnoreProperties(ignoreUnknown=true)
    static public class SuccessResponseData {
        public int id;
        public String message;
    }
}
