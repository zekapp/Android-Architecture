package com.androidarchitecture.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.androidarchitecture.di.component.ActivityComponent;

/**
 * Created by Zeki Guler on 31,March,2016
 * ©2015 Appscore. All Rights Reserved
 */
public class BaseFragment extends Fragment {

    private ActivityComponent mActivityComponent;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivityComponent();
    }

    public ActivityComponent getActivityComponent() {
        if (mActivityComponent == null)
            mActivityComponent =((BaseActivity) getActivity()).getActivityComponent();
        return mActivityComponent;
    }
}
