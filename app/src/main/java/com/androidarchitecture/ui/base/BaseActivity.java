package com.androidarchitecture.ui.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.androidarchitecture.di.component.ActivityComponent;


public class BaseActivity extends AppCompatActivity {

    private ActivityComponent mActivityComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


}
