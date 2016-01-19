package com.androidarchitecture.data;

import com.androidarchitecture.data.job.fetch.FetchSamplesJob;
import com.androidarchitecture.data.local.DatabaseHelper;
import com.androidarchitecture.data.remote.ApiService;
import com.androidarchitecture.data.remote.responses.SampleResponseData;
import com.androidarchitecture.data.vo.Sample;
import com.path.android.jobqueue.JobManager;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import timber.log.Timber;

/**
 * Created by Zeki Guler on 18,January,2016
 * ©2015 Appscore. All Rights Reserved
 */

@Singleton
public class DataManager {

    private final DatabaseHelper mDatabaseHelper;
    private final JobManager mJobHelper;
    private final ApiService mApiService;

    @Inject
    public DataManager(ApiService apiService, DatabaseHelper databaseHelper, JobManager jobManager){
        mDatabaseHelper = databaseHelper;
        mJobHelper = jobManager;
        mApiService = apiService;
    }

    /**
     * Fetch eventually data from api and save them to Db.
     * */
    public Observable<Sample> syncSample(int page, int perPage) {
        return mApiService.getSamples(page,perPage)
                .concatMap(new Func1<SampleResponseData, Observable<Sample>>() {
                    @Override
                    public Observable<Sample> call(SampleResponseData data) {
                        Timber.d("Sample -> Observable<Sample>");
                        return mDatabaseHelper.setSamples(data.getSampleResponse().getSampleList());
                    }
                });
    }

    /**
     * Fetch data from Db
     * */
    public Observable<List<Sample>> getSamples() {
        return Observable.create(new Observable.OnSubscribe<List<Sample>>() {
            @Override
            public void call(Subscriber<? super List<Sample>> subscriber) {
                subscriber.onNext(mDatabaseHelper.sampleListQuery());
            }
        });
    }

    /**
     * Fetch data from db with pagination and hit api and then update db and retrun updated
     *
     * */
    public Observable<List<Sample>> getSamplesAndHitApi(final int page, final int perPage) {
        return Observable.create(new Observable.OnSubscribe<List<Sample>>() {
            @Override
            public void call(Subscriber<? super List<Sample>> subscriber) {
                subscriber.onNext(mDatabaseHelper.sampleListQuery());
            }
        });
    }


    /**
     * Fetch data api and save it eventually to Db.
     *
     * It check internet connection and other issue. If error occured related to
     * */
    public void fetchSamplesAsync(int page, int perPage){
        mJobHelper.addJobInBackground(new FetchSamplesJob(page, perPage));
    }
}
