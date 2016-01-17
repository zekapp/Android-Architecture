package com.androidarchitecture.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by zeki on 17/01/2016.
 */
public class MData<T> {
    @JsonProperty("data")
    private T data;

    public T getData() {
        return data;
    }
}
