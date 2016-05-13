package com.androidarchitecture.data.remote.posts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Zeki Guler on 19,April,2016
 * ©2015 Appscore. All Rights Reserved
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateGcmTokenPost {
    public UpdateGcmToken data;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UpdateGcmToken {
        public String devType;
        public String deviceId;
        public String token;
    }

    private UpdateGcmTokenPost(Builder builder) {
        data = builder.mUpdateGcmToken;
    }

    public static class Builder {
        final UpdateGcmToken mUpdateGcmToken;

        public Builder(String deviceId, String token) {
            mUpdateGcmToken = new UpdateGcmToken();
            mUpdateGcmToken.deviceId = deviceId;
            mUpdateGcmToken.token = token;
            mUpdateGcmToken.devType = "android";
        }

        public UpdateGcmTokenPost build() {
            return new UpdateGcmTokenPost(this);
        }
    }
}
