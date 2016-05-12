package com.androidarchitecture.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;

import com.androidarchitecture.R;
import com.androidarchitecture.ui.base.BaseActivity;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by Zeki Guler on 09,May,2016
 * ©2015 Appscore. All Rights Reserved
 */
public class SignInActivity extends BaseActivity{

    public static <T> Intent newInstance(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, SignInActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);
        setContentView(R.layout.sign_in_activity);
        ButterKnife.bind(this);
    }
}
