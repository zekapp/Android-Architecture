package com.androidarchitecture.ui.login;

import android.content.Context;

import com.androidarchitecture.R;
import com.androidarchitecture.config.AppConfig;
import com.androidarchitecture.data.DataManager;
import com.androidarchitecture.data.remote.responses.SuccessResponse;
import com.androidarchitecture.di.qualifier.ActivityContext;
import com.androidarchitecture.ui.base.BasePresenter;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Zeki Guler on 16,May,2016
 * ©2015 Appscore. All Rights Reserved
 */

public class SignInActivityPresenter extends BasePresenter<SignInActivityMvpView> {
    private Context mContext;
    private DataManager mDataManager;
    private AppConfig mAppConfig;
    private Subscription mSubscription;

    @Inject
    public SignInActivityPresenter(@ActivityContext Context context,
                                   DataManager dataManger, AppConfig appConfig) {
        mContext = context;
        mDataManager = dataManger;
        mAppConfig = appConfig;
    }

    @Override
    public void attachView(SignInActivityMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscription != null) mSubscription.unsubscribe();
    }

    public void attemptToLogin(String email, String password) {
        if (validate(email,password)){
            login(email, password);
        }
    }

    private void login(String email, String password) {
        getMvpView().showProgress();
        mSubscription = mDataManager.login(email, password)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<SuccessResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().hideProgress();
                        getMvpView().showLoginError(e.getMessage());
                    }

                    @Override
                    public void onNext(SuccessResponse successResponse) {
                        getMvpView().hideProgress();
                        getMvpView().loginSuccess();
                    }
                });
    }

    private boolean validate(String email, String password) {
        boolean valid = true;

        if (email.isEmpty()){
            getMvpView().onErrorEmail(mContext.getString(R.string.error_field_required));
            valid = false;
        } else if (!email.contains("@")){
            getMvpView().onErrorEmail(mContext.getString(R.string.error_invalid_email));
            valid = false;
        }

        if (password.isEmpty()){
            getMvpView().onErrorPassword(mContext.getString(R.string.error_field_required));
            valid = false;
        } else  if (password.length() < mAppConfig.getPasswordLength()){
            getMvpView().onErrorPassword(mContext.getString(R.string.error_invalid_password));
            valid = false;
        }

        return valid;
    }
}



