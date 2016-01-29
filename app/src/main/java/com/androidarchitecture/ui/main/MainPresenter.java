package com.androidarchitecture.ui.main;

import com.androidarchitecture.data.DataManager;
import com.androidarchitecture.data.vo.Sample;
import com.androidarchitecture.ui.base.BasePresenter;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class MainPresenter extends BasePresenter<MainMvpView> {

    private final DataManager mDataManager;
    private Subscription mSubscription;

    @Inject
    public MainPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(MainMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscription != null) mSubscription.unsubscribe();
    }

    public void loadRibots() {
        checkViewAttached();
        mSubscription = mDataManager.getSamplesFromDbThenUpdateViaApi(0, 30)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<Sample>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "There was an error loading the samples.");
                        getMvpView().showError();
                    }

                    @Override
                    public void onNext(List<Sample> samples) {
                        if (samples.isEmpty()) {
                            getMvpView().showSamplesEmpty();
                        } else {
                            getMvpView().showSamples(samples);
                        }
                    }
                });
    }

}
