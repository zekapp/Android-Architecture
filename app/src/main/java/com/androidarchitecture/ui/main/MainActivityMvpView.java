package com.androidarchitecture.ui.main;

import com.androidarchitecture.ui.base.MvpView;

/**
 * Created by Zeki Guler on 10,February,2016
 * ©2015 Appscore. All Rights Reserved
 */
public interface MainActivityMvpView extends MvpView {

    void showProgress();

    void stopProgress();
}
