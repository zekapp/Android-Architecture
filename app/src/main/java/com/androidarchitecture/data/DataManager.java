package com.androidarchitecture.data;

import com.androidarchitecture.config.AppConfig;
import com.androidarchitecture.data.job.fetch.FetchSamplesJob;
import com.androidarchitecture.data.local.DatabaseHelper;
import com.androidarchitecture.data.local.PreferencesHelper;
import com.androidarchitecture.data.remote.ApiService;
import com.androidarchitecture.data.remote.responses.SampleResponseData;
import com.androidarchitecture.data.vo.Sample;
import com.path.android.jobqueue.JobManager;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Func1;
import timber.log.Timber;

/**
 * Created by Zeki Guler on 18,January,2016
 * ©2015 Appscore. All Rights Reserved
 */

@Singleton
public class DataManager {

    private final DatabaseHelper mDatabaseHelper;
    private final JobManager mJobManagerHelper;
    private final ApiService mApiService;
    private final PreferencesHelper mPreferencesHelper;
    private final AppConfig mConfigHelper;

    @Inject
    public DataManager(ApiService apiService, DatabaseHelper databaseHelper,
                       JobManager jobManager, PreferencesHelper preferencesHelper,
                       AppConfig configHelper) {
        mDatabaseHelper = databaseHelper;
        mJobManagerHelper = jobManager;
        mApiService = apiService;
        mPreferencesHelper = preferencesHelper;
        mConfigHelper = configHelper;
    }

    /**
     * Fetch eventually data from api and save them to Db.
     */
    public Observable<Sample> syncSample(int page, int perPage) {
        return mApiService.getSamples(page, perPage)
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
     */
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
     * <p/>
     * This function finish the job eventually even if there is no internet connection.
     *
     * It puts the job in to the job queue (It manages the job in own database). If there is error
     * related network communication it flag the job as not sent and when network connection back
     * it send/receive the packet
     *
     * Use eventbus to get the response if necessary.
     *
     *
     */
    public void fetchSamplesAsync(int page, int perPage) {
        mJobManagerHelper.addJobInBackground(new FetchSamplesJob(page, perPage));
    }


    /**
     * This function demonstrate the case such as
     *
     * - Get data from Db (if no data hits api)
     * - Update the Ui with stored data.
     * - Then hits the api to refresh the data in db
     * - Then updates the Ui with up to date data.
     *
     */
    public Observable<List<Sample>> getSamplesFromDbThenUpdateViaApi(final int page, final int perPage) {
        return Observable.create(new Observable.OnSubscribe<List<Sample>>() {
            @Override
            public void call(final Subscriber<? super List<Sample>> subscriber) {
                subscriber.onNext(mDatabaseHelper.sampleListQuery());

                mApiService.getSamples(page, perPage).subscribe(new Observer<SampleResponseData>() {
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
}
