package com.androidarchitecture.di.component;

import com.androidarchitecture.config.AppConfig;
import com.androidarchitecture.data.DataManager;
import com.androidarchitecture.data.job.fetch.FetchSamplesJob;
import com.androidarchitecture.di.module.AppModule;

import javax.inject.Singleton;
import dagger.Component;
/**
 * Created by Zeki Guler on 18,January,2016
 * ©2015 Appscore. All Rights Reserved
 */
@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

    DataManager getDataManager();

    AppConfig getAppConfig();

    void inject(FetchSamplesJob getFetchSamplesJob);
}
