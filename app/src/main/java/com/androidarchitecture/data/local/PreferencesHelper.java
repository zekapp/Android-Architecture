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

    private final SharedPreferences mPref;

    @Inject
    public PreferencesHelper(@ApplicationContext Context context) {
        mPref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
    }
}
