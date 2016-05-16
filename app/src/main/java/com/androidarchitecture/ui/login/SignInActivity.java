package com.androidarchitecture.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.androidarchitecture.R;
import com.androidarchitecture.ui.base.BaseActivity;
import com.androidarchitecture.ui.sample.SampleActivity;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Zeki Guler on 09,May,2016
 * ©2015 Appscore. All Rights Reserved
 */

public class SignInActivity extends BaseActivity implements SignInActivityMvpView {

    @Inject
    SignInActivityPresenter mPresenter;
    @Bind(R.id.login_email)
    EditText mLoginEmail;
    @Bind(R.id.login_password)
    EditText mLoginPassword;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, SignInActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);
        setContentView(R.layout.sign_in_activity);
        ButterKnife.bind(this);
        mPresenter.attachView(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @OnClick(R.id.login)
    @SuppressWarnings("unused")
    public void onLoginButtonClicked(View view) {
        resetErrors();
        mPresenter.attemptToLogin(
                mLoginEmail.getText().toString(),
                mLoginPassword.getText().toString());
    }

    /********
     * MVP View Functions
     ********/

    @Override
    public void showProgress() {
        mProgressDialog.setMessage("Logging in...");
        mProgressDialog.show();
    }

    @Override
    public void hideProgress() {
        mProgressDialog.hide();
    }

    @Override
    public void showLoginError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void loginSuccess() {
        Intent a = SampleActivity.newIntent(this);
        a.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(a);
        finish();
    }

    @Override
    public void onErrorPassword(String error) {
        mLoginPassword.setError(error);
    }

    @Override
    public void onErrorEmail(String error) {
        mLoginEmail.setError(error);
    }


    /********
     * Activity related Functions
     ********/

    private void resetErrors() {
        mLoginEmail.setError(null);
        mLoginPassword.setError(null);
    }
}
