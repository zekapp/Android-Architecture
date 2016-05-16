package com.androidarchitecture.data.remote.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Zeki Guler on 29,April,2016
 * ©2015 Appscore. All Rights Reserved
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class APIError {
    public ErrorData error;

    @JsonIgnoreProperties(ignoreUnknown = true)
    static public class ErrorData {
        @JsonProperty("code")
        public int serverCode;
        public List<String> messages;
    }
}
