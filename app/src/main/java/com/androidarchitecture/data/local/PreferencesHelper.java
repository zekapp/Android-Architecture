package com.androidarchitecture.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import com.androidarchitecture.di.qualifier.ApplicationContext;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Zeki Guler on 10,March,2016
 * ©2015 Appscore. All Rights Reserved
 */
@Singleton
public class PreferencesHelper {
    public static final String PREF_FILE_NAME = "android_architecture_pref_file";
    private static final String GCM_TOKEN = "GCM_TOKEN";
    private static final String GUM_TOKEN_SAVED_TO_SERVER_SUCCESSFULLY = "GCM_TOKEN_SAVED_TO_SERVER_SUCCESSFULLY";

    private final SharedPreferences mPref;

    @Inject
    public PreferencesHelper(@ApplicationContext Context context) {
        mPref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
    }

    public void saveGCMToken(String token) {
        mPref.edit().putString(GCM_TOKEN, token).apply();
    }

    public String getGCMToken() {
       return  mPref.getString(GCM_TOKEN,"");
    }

    public void setGCMTokenSavedToServer(boolean isSuccess) {
        mPref.edit().putBoolean(GUM_TOKEN_SAVED_TO_SERVER_SUCCESSFULLY, isSuccess).apply();
    }

    public boolean isGCMTokenSavedToServer() {
        return  mPref.getBoolean(GUM_TOKEN_SAVED_TO_SERVER_SUCCESSFULLY,false);
    }
}
