package com.androidarchitecture.di.module;


import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.androidarchitecture.App;
import com.androidarchitecture.config.AppConfig;
import com.androidarchitecture.data.DataManager;
import com.androidarchitecture.data.job.BaseJob;
import com.androidarchitecture.data.local.AppDatabase;
import com.androidarchitecture.data.remote.ApiService;
import com.androidarchitecture.di.qualifier.ApplicationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.JobManager;
import com.path.android.jobqueue.config.Configuration;
import com.path.android.jobqueue.di.DependencyInjector;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.JacksonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

/**
 * Created by zeki on 17/01/2016.
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


    @Provides
    @Singleton
    public ApiService apiService(AppConfig appConfig) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient httpClient = new OkHttpClient();
        httpClient.interceptors().add(logging);

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
