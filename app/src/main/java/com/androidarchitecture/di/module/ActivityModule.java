package com.androidarchitecture.di.module;

import android.app.Activity;
import android.content.Context;

import com.androidarchitecture.di.qualifier.ActivityContext;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Zeki Guler on 29,March,2016
 * ©2015 Appscore. All Rights Reserved
 */
@Module
public class ActivityModule {

    private Activity mActivity;

    public ActivityModule(Activity activity) {
        mActivity = activity;
    }

    @Provides
    Activity provideActivity() {
        return mActivity;
    }

    @Provides
    @ActivityContext
    Context providesContext() {
        return mActivity;
    }

}
