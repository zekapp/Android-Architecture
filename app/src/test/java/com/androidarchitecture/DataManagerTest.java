package com.androidarchitecture;

/**
 * Created by Zeki Guler on 25,January,2016
 * ©2015 Appscore. All Rights Reserved
 */

import com.androidarchitecture.data.DataManager;
import com.androidarchitecture.data.local.DatabaseHelper;
import com.androidarchitecture.data.local.PreferencesHelper;
import com.androidarchitecture.data.remote.ApiService;
import com.androidarchitecture.data.remote.responses.SampleResponseData;
import com.androidarchitecture.data.vo.Sample;
import com.path.android.jobqueue.JobManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.functions.Action;
import rx.functions.Action1;
import rx.observers.TestObserver;
import rx.observers.TestSubscriber;

import static org.mockito.Mockito.*;

/**
 * This test class performs local unit tests without dependencies on the Android framework
 * For testing methods in the DataManager follow this approach:
 * 1. Stub mock helper classes that your method relies on. e.g. ApiServices or DatabaseHelper
 * 2. Test the Observable using TestSubscriber
 * 3. Optionally write a SEPARATE test that verifies that your method is calling the right helper
 * using Mockito.verify()
 */
@RunWith(MockitoJUnitRunner.class)
public class DataManagerTest {

    @Mock DatabaseHelper mMockDatabaseHelper;
    @Mock JobManager mJobHelper;
    @Mock ApiService mMockApiService;
    @Mock PreferencesHelper mPreferencesHelper;

    private DataManager mDataManager;

    @Before
    public void setUp() {
        mDataManager = new DataManager(mMockApiService, mMockDatabaseHelper, mJobHelper, mPreferencesHelper);
    }

    @Test
    public void syncSamplesEmitValues(){
        int page = 1;
        int perPage = 30;

        SampleResponseData sampleResponseData = TestDataFactory.getSampleResponseData(page,perPage);
        stubSyncSamplesHelperCalls(sampleResponseData,page, perPage);

        TestSubscriber<Sample> result = new TestSubscriber<>();
        mDataManager.syncSample(page, perPage).subscribe(result);
        result.assertNoErrors();
        result.assertReceivedOnNext(sampleResponseData.getSampleResponse().getSampleList());
    }

    @Test
    public void syncSamplesCallsApiAndDatabase(){
        int page = 1;
        int perPage = 30;

        SampleResponseData sampleResponseData = TestDataFactory.getSampleResponseData(page,perPage);
        stubSyncSamplesHelperCalls(sampleResponseData,page, perPage);

        mDataManager.syncSample(page, perPage).subscribe();
        // Verify right calls to helper methods
        verify(mMockApiService).getSamples(page, perPage);
        verify(mMockDatabaseHelper).setSamples(sampleResponseData.getSampleResponse().getSampleList());

    }

    @Test
    public void syncSamplesDoesNotCallDatabaseWhenApiFails(){
        int page =1;
        int perPage = 30;
        when(mMockApiService.getSamples(page,perPage)).thenReturn(Observable.<SampleResponseData>error(new RuntimeException()));

        mDataManager.syncSample(page, perPage).subscribe(new TestSubscriber<Sample>());

        verify(mMockApiService).getSamples(page,perPage);
        verify(mMockDatabaseHelper, never()).setSamples(anyListOf(Sample.class));
    }

    private void stubSyncSamplesHelperCalls(SampleResponseData sampleResponseData, int page, int perPage) {
        List<Sample> samples = sampleResponseData.getSampleResponse().getSampleList();

        when(mMockApiService.getSamples(page,perPage))
                .thenReturn(Observable.just(sampleResponseData));
        when(mMockDatabaseHelper.setSamples(samples))
                .thenReturn(Observable.from(samples));
    }


    @Test
    public void getAllSamplesEmitValues(){
        List<Sample> samples = TestDataFactory.getSampleList(300);

        when(mMockDatabaseHelper.sampleListQuery())
                .thenReturn(samples);

        TestSubscriber<List<Sample>> testObserver = new TestSubscriber<>();
        mDataManager.getAllSamples().subscribe(testObserver);
        testObserver.assertNoErrors();
        testObserver.assertValue(samples);
    }

    @Test
    public void getSamplesFromDbThenUpdateViaApiEmitValues(){
        int page =1;
        int perPage = 30;

        SampleResponseData sampleResponseData = TestDataFactory.getSampleResponseData(0,30);

        List<Sample> samplesFromDb          = TestDataFactory.getSampleList(30);
        List<Sample> samplesFromUpdatedDb   = sampleResponseData.getSampleResponse().getSampleList();

        stubGetSamplesFromDbThenUpdateViaApi(samplesFromDb,sampleResponseData);

        TestSubscriber<List<Sample>> testSubscriber = new TestSubscriber<>();
        mDataManager.getSamplesFromDbThenUpdateViaApi(page, perPage).subscribe(testSubscriber);
        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(2);
        testSubscriber.assertValues(samplesFromDb,samplesFromUpdatedDb);
    }

    @Test
    public void getSamplesFromDbThenUpdateViaCallApiAndDb(){
        int page =1;
        int perPage = 30;

        SampleResponseData sampleResponseData = TestDataFactory.getSampleResponseData(0,30);

        List<Sample> samplesFromDb          = TestDataFactory.getSampleList(30);
        List<Sample> samplesFromUpdatedDb   = sampleResponseData.getSampleResponse().getSampleList();

        stubGetSamplesFromDbThenUpdateViaApi(samplesFromDb,sampleResponseData);

        TestSubscriber<List<Sample>> testSubscriber = new TestSubscriber<>();
        mDataManager.getSamplesFromDbThenUpdateViaApi(page, perPage).subscribe(testSubscriber);

        verify(mMockApiService).getSamples(page,perPage);
        verify(mMockDatabaseHelper,atMost(1)).setSamples(samplesFromUpdatedDb);
        verify(mMockDatabaseHelper, times(2)).sampleListQuery();
    }


    @Test
    public void getSamplesFromDbThenDontUpdateIfApiFails(){
        int page =1;
        int perPage = 30;

        SampleResponseData sampleResponseData = TestDataFactory.getSampleResponseData(0,30);

        List<Sample> samplesFromDb          = TestDataFactory.getSampleList(30);
        List<Sample> samplesFromUpdatedDb   = sampleResponseData.getSampleResponse().getSampleList();
        List<Sample> samplesFromApi         = sampleResponseData.getSampleResponse().getSampleList();

        stubGetSamplesFromDbThenUpdateViaFailedApi(samplesFromDb,sampleResponseData);

        TestSubscriber<List<Sample>> testSubscriber = new TestSubscriber<>();
        mDataManager.getSamplesFromDbThenUpdateViaApi(page, perPage).subscribe(testSubscriber);
        testSubscriber.assertValueCount(1);
        testSubscriber.assertValue(samplesFromDb);

        verify(mMockDatabaseHelper, never()).setSamples(samplesFromApi);

    }

    private void stubGetSamplesFromDbThenUpdateViaApi(List<Sample> samplesFromDb, SampleResponseData sampleDataFromApi) {
        List<Sample> sampleFromApi      = sampleDataFromApi.getSampleResponse().getSampleList();
        List<Sample> sampleFromUpdateDb = sampleDataFromApi.getSampleResponse().getSampleList();

        when(mMockDatabaseHelper.sampleListQuery())
                .thenReturn(samplesFromDb)
                .thenReturn(sampleFromUpdateDb);

        when(mMockApiService.getSamples(anyInt(),anyInt())).thenReturn(Observable.just(sampleDataFromApi));

        when(mMockDatabaseHelper.setSamples(sampleFromApi)).thenReturn(Observable.from(sampleFromApi));
    }

    private void stubGetSamplesFromDbThenUpdateViaFailedApi(List<Sample> samplesFromDb, SampleResponseData sampleDataFromApi) {
        List<Sample> sampleFromApi      = sampleDataFromApi.getSampleResponse().getSampleList();
        List<Sample> sampleFromUpdateDb = sampleDataFromApi.getSampleResponse().getSampleList();

        when(mMockDatabaseHelper.sampleListQuery())
                .thenReturn(samplesFromDb)
                .thenReturn(sampleFromUpdateDb);

        when(mMockApiService.getSamples(anyInt(),anyInt())).thenReturn(Observable.<SampleResponseData>error(new RuntimeException()));

        when(mMockDatabaseHelper.setSamples(sampleFromApi)).thenReturn(Observable.from(sampleFromApi));
    }
}
