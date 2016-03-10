package com.androidarchitecture.di.module;


import android.app.AlarmManager;
import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.androidarchitecture.App;
import com.androidarchitecture.config.AppConfig;
import com.androidarchitecture.data.job.BaseJob;
import com.androidarchitecture.data.local.AppDatabase;
import com.androidarchitecture.data.local.PreferencesHelper;
import com.androidarchitecture.data.remote.ApiService;
import com.androidarchitecture.data.remote.oauth.OauthInterceptor;
import com.androidarchitecture.di.qualifier.ApplicationContext;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.JobManager;
import com.path.android.jobqueue.config.Configuration;
import com.path.android.jobqueue.di.DependencyInjector;
import com.raizlabs.android.dbflow.config.FlowManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by Zeki Guler on 10,March,2016
 * ©2015 Appscore. All Rights Reserved
 */

@Module
public class AppModule {
    protected final App mApp;

    public AppModule(App application) {
        mApp = application;
    }

    @Provides
    Application provideApplication() {
        return mApp;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApp;
    }


    /**
     * Build one builder for your api calls. This is a guide for you implementation.
     * */
    @Provides
    @Singleton
    public ApiService apiService(AppConfig appConfig, OauthInterceptor oauthInterceptor) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient httpClient = new OkHttpClient().newBuilder()
                .addInterceptor(logging)
                .addInterceptor(oauthInterceptor)
                .addNetworkInterceptor(new StethoInterceptor())
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(appConfig.getApiUrl())
                .addConverterFactory(JacksonConverterFactory.create(mapper))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(httpClient)
                .build();

        return retrofit.create(ApiService.class);
    }

    @Provides
    @Singleton
    public SQLiteDatabase database() {
        return FlowManager.getDatabase(AppDatabase.NAME).getWritableDatabase();
    }

    /**
     * Job manager is responsible your "eventually" actions.
     * For example: you want to
     * save something to the server but there is no internet connection. Use this library and
     * it will eventually save the things to the server. If you get error while saving it
     * (Server side error) handle the error using eventbus.
     *
     *
     * See : https://github.com/yigit/android-priority-jobqueue
     * See : https://www.youtube.com/watch?v=BlkJzgjzL0c
     * */
    @Provides
    @Singleton
    public JobManager jobManager() {
        Configuration config = new Configuration.Builder(mApp)
                .consumerKeepAlive(45)
                .maxConsumerCount(3)
                .minConsumerCount(1)
                .injector(new DependencyInjector() {
                    @Override
                    public void inject(Job job) {
                        if (job instanceof BaseJob) {
                            ((BaseJob) job).inject(mApp.getComponent());
                        }
                    }
                })
                .build();
        return new JobManager(mApp, config);
    }
}
