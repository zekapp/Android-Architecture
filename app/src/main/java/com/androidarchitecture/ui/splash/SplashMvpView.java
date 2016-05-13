package com.androidarchitecture.ui.splash;

import com.androidarchitecture.ui.base.MvpView;
import com.google.android.gms.common.GoogleApiAvailability;

/**
 * Created by Zeki Guler on 12,May,2016
 * ©2015 Appscore. All Rights Reserved
 */
public interface SplashMvpView extends MvpView{
    void deviceRegisteredSuccessfully(String gcmToken);

    void deviceNeedsToBeRegister();
}
