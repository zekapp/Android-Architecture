package com.androidarchitecture.ui.main;

import android.os.Bundle;

import com.androidarchitecture.R;
import com.androidarchitecture.ui.base.BaseActivity;

import javax.inject.Inject;

/**
 * Created by Zeki Guler on 29,March,2016
 * ©2015 Appscore. All Rights Reserved
 *
 * This is main activity. Start programming from here.
 *
 * Change the default launch activity from manifest
 *
 */
public class MainActivity extends BaseActivity implements MainActivityMvpView{

    @Inject
    MainActivityPresenter mMainActivityPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);
        setContentView(R.layout.main_activity);
    }

    @Override
    public void showProgress() {
        // will be called from presenter.
    }

    @Override
    public void stopProgress() {
        // will be called from presenter.
    }
}
