package com.androidarchitecture.di.component;

import com.androidarchitecture.config.AppConfig;
import com.androidarchitecture.data.DataManager;
import com.androidarchitecture.data.job.fetch.FetchSamplesJob;
import com.androidarchitecture.data.local.PreferencesHelper;
import com.androidarchitecture.di.module.AppModule;
import com.androidarchitecture.gcm.RegistrationIntentService;

import javax.inject.Singleton;
import dagger.Component;
/**
 * Created by Zeki Guler on 18,January,2016
 * ©2015 Appscore. All Rights Reserved
 */
@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
    PreferencesHelper preferencesHelper();
    DataManager getDataManager();

    AppConfig getAppConfig();

    void inject(FetchSamplesJob getFetchSamplesJob);
    void inject(RegistrationIntentService getAlarmIntentService);
}
