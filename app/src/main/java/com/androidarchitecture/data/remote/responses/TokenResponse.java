package com.androidarchitecture.data.remote.responses;

/**
 * Created by Zeki Guler on 10,March,2016
 * ©2015 Appscore. All Rights Reserved
 */
public class TokenResponse {
    public String access_token;
    public int expires_in;
    public String refresh_token;
    public String scope;
    public String token_type;
    public String user_id;

    public String getAccess_token() {
        return access_token;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public String getScope() {
        return scope;
    }

    public String getToken_type() {
        return token_type;
    }

    public String getUser_id() {
        return user_id;
    }


    public String getTokenAsString() {
        return  getAccess_token()  + ":" +
                getRefresh_token() + ":" +
                getUser_id();
    }
}
