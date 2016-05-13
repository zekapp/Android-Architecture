package com.androidarchitecture.ui.splash;

import com.androidarchitecture.data.DataManager;
import com.androidarchitecture.ui.base.BasePresenter;

import javax.inject.Inject;

/**
 * Created by Zeki Guler on 12,May,2016
 * ©2015 Appscore. All Rights Reserved
 */
public class SplashPresenter extends BasePresenter<SplashActivity>{

    private final DataManager mDataManager;

    @Inject
    public SplashPresenter(DataManager dataManager){
        mDataManager = dataManager;
    }

    @Override
    public void attachView(SplashActivity mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
    }


    public void isThisDeviceRegisterForGCM() {
        boolean alreadyRegistered = mDataManager.isGcmTokenSavedInOurServer();

        if (alreadyRegistered){
            String gcmToken = mDataManager.getSavedGcmToken();
            getMvpView().deviceRegisteredSuccessfully(gcmToken);
        }
        else{
            getMvpView().deviceNeedsToBeRegister();
        }

    }
}
