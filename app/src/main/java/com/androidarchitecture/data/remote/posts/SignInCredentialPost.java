package com.androidarchitecture.data.remote.posts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Zeki Guler on 16,May,2016
 * ©2015 Appscore. All Rights Reserved
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SignInCredentialPost {

    public SignInCredential data;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SignInCredential {
        public String email;
        public String password;
    }

    private SignInCredentialPost(Builder builder){
        data = builder.mSignInCredential;
    }

    public static class Builder {
        final SignInCredential mSignInCredential;

        public Builder(String email, String password) {
            mSignInCredential = new SignInCredential();
            mSignInCredential.email = email;
            mSignInCredential.password = password;
        }

        public SignInCredentialPost build() {
            return new SignInCredentialPost(this);
        }
    }
}
