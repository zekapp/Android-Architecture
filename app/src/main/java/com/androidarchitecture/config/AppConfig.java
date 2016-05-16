package com.androidarchitecture.config;

import android.content.Context;
import android.content.SharedPreferences;

import com.androidarchitecture.R;
import com.androidarchitecture.di.qualifier.ApplicationContext;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Zeki Guler on 10,March,2016
 * ©2015 Appscore. All Rights Reserved
 */

@Singleton
public class AppConfig {

    private static final String KEY_API_URL = "api_url";
    private static final int PASSWORD_LENGHT = 6;

    private final SharedPreferences mSharedPreferences;
    private Context mContext;


    @Inject
    public AppConfig(@ApplicationContext Context context) {
        mContext = context;
        mSharedPreferences = context.getSharedPreferences("app_cfg", Context.MODE_PRIVATE);
    }

    public String getApiUrl() {
        return mContext.getString(R.string.server_sample_base_url);
    }

    public int getPasswordLength(){
        return PASSWORD_LENGHT;
    }
}
