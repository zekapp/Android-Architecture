package com.androidarchitecture.data;

import com.androidarchitecture.data.job.fetch.FetchSamplesJob;
import com.androidarchitecture.data.local.DatabaseHelper;
import com.androidarchitecture.data.remote.ApiService;
import com.androidarchitecture.data.remote.responses.SampleResponseData;
import com.androidarchitecture.data.vo.Sample;
import com.path.android.jobqueue.JobManager;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func0;
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
     * Fetch all samples from db
     * */
    public Observable<List<Sample>> getAllSamples() {
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


    /**
     * Update UI with old data from db.
     * Then fetch new data from Api and update Db.
     * Then Update UI with fresh data again.
    * */
    public Observable<List<Sample>> getSamplesFromDbThenUpdateViaApi(final int page, final int perPage){
        return Observable.create(new Observable.OnSubscribe<List<Sample>>() {
            @Override
            public void call(final Subscriber<? super List<Sample>> subscriber) {
                subscriber.onNext(mDatabaseHelper.sampleListQuery());

                mApiService.getSamples(page,perPage).subscribe(new Observer<SampleResponseData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e("mApiService: %s", e.getMessage());
                        subscriber.onError(e);
                    }

                    @Override
                    public void onNext(SampleResponseData sampleResponseData) {

                        mDatabaseHelper.setSamples(sampleResponseData.getSampleResponse().getSampleList()).subscribe(new Observer<Sample>() {
                            @Override
                            public void onCompleted() {
                                subscriber.onNext(mDatabaseHelper.sampleListQuery());
                            }

                            @Override
                            public void onError(Throwable e) {
                                Timber.e("mDatabaseHelper: %s", e.getMessage());
                                subscriber.onError(e);
                            }

                            @Override
                            public void onNext(Sample sample) {

                            }
                        });
                    }
                });
            }
        });
    }
/*
*
* new Action1<SampleResponseData>() {
                    @Override
                    public void call(SampleResponseData sampleResponseData) {
                        mDatabaseHelper.setSamples(sampleResponseData.getSampleResponse().getSampleList())
                        .doOnCompleted(new Action0() {
                            @Override
                            public void call() {
                                subscriber.onNext(mDatabaseHelper.sampleListQuery());
                            }
                        }).subscribe();
                    }
                }*/
}
