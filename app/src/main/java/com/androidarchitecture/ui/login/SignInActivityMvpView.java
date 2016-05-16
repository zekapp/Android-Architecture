package com.androidarchitecture.ui.login;

import com.androidarchitecture.ui.base.MvpView;

/**
 * Created by Zeki Guler on 16,May,2016
 * ©2015 Appscore. All Rights Reserved
 */

public interface SignInActivityMvpView extends MvpView {
    void showProgress();

    void hideProgress();

    void showLoginError(String error);

    void loginSuccess();

    void onErrorPassword(String error);

    void onErrorEmail(String error);
}



