package com.androidarchitecture.config;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by zeki on 17/01/2016.
 */
public class AppConfig {

    private static final String KEY_API_URL = "api_url";

    private final SharedPreferences mSharedPreferences;
    public AppConfig(Context context) {
        mSharedPreferences = context.getSharedPreferences("app_cfg", Context.MODE_PRIVATE);
    }

    public String getApiUrl() {
        return mSharedPreferences.getString(KEY_API_URL, "http://polls.apiblueprint.org/api/v1/");
    }
}
