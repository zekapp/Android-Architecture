package com.androidarchitecture;

import com.androidarchitecture.data.DataManager;
import com.androidarchitecture.data.vo.Sample;
import com.androidarchitecture.ui.sample.SampleMvpView;
import com.androidarchitecture.ui.sample.SamplePresenter;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import rx.Observable;

import static org.mockito.Mockito.*;

/**
 * Created by Zeki Guler on 29,January,2016
 * ©2015 Appscore. All Rights Reserved
 */
@RunWith(MockitoJUnitRunner.class)
public class SamplePresenterTest {
    @Mock DataManager mMockDataManger;
    @Mock
    SampleMvpView mMockSampleMvpView;

    private SamplePresenter mSamplePresenter;

    @Rule
    public final RxSchedulersOverrideRule mOverrideSchedulersRule = new RxSchedulersOverrideRule();

    @Before
    public void setUp(){
        mSamplePresenter = new SamplePresenter(mMockDataManger);
        mSamplePresenter.attachView(mMockSampleMvpView);
    }

    @After
    public void tearDown(){
        mSamplePresenter.detachView();
    }

    @Test
    public void loadSamplesReturnSamples(){
        List<Sample> samples = TestDataFactory.getSampleList(30);

        when(mMockDataManger.getSamplesFromDbThenUpdateViaApi(0,30))
                .thenReturn(Observable.just(samples));

        mSamplePresenter.loadSamples();
        verify(mMockSampleMvpView).showSamples(samples);
        verify(mMockSampleMvpView,never()).showError();
        verify(mMockSampleMvpView, never()).showSamplesEmpty();
    }

    @Test
    public void loadSamplesReturnEmptyList(){
        when(mMockDataManger.getSamplesFromDbThenUpdateViaApi(0,30))
                .thenReturn(Observable.just(Collections.<Sample>emptyList()));

        mSamplePresenter.loadSamples();
        verify(mMockSampleMvpView,never()).showSamples(anyListOf(Sample.class));
        verify(mMockSampleMvpView,never()).showError();
        verify(mMockSampleMvpView).showSamplesEmpty();
    }

    @Test
    public void loadSamplesReturnError(){
        doReturn(Observable.error(new RuntimeException()))
                .when(mMockDataManger)
                .getSamplesFromDbThenUpdateViaApi(0,30);

        mSamplePresenter.loadSamples();
        verify(mMockSampleMvpView,never()).showSamples(anyListOf(Sample.class));
        verify(mMockSampleMvpView).showError();
        verify(mMockSampleMvpView,never()).showSamplesEmpty();
    }
}
