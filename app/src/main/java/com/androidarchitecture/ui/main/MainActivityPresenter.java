package com.androidarchitecture.ui.main;


import com.androidarchitecture.data.DataManager;
import com.androidarchitecture.ui.base.BasePresenter;

import javax.inject.Inject;

import rx.Subscription;

/**
 * Created by Zeki Guler on 10,February,2016
 * ©2015 Appscore. All Rights Reserved
 *
 * Main purpose: Fetch the data from Datamanager, Modify accroding to the view
 * and shows.
 */
public class MainActivityPresenter extends BasePresenter<MainActivityMvpView> {

    private final DataManager mDataManager;
    private Subscription mSubscription;

    @Inject
    public MainActivityPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(MainActivityMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
    }

}
