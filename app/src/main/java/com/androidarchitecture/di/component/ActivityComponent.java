package com.androidarchitecture.di.component;

import com.androidarchitecture.di.module.ActivityModule;
import com.androidarchitecture.di.scope.PerActivity;
import com.androidarchitecture.ui.login.SignInActivity;
import com.androidarchitecture.ui.splash.SplashActivity;
import com.androidarchitecture.ui.main.MainActivity;
import com.androidarchitecture.ui.sample.SampleActivity;

import dagger.Component;

/**
 * Created by Zeki Guler on 29,March,2016
 * ©2015 Appscore. All Rights Reserved
 */

@PerActivity
@Component(dependencies = AppComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(SampleActivity sampleActivity);

    void inject(MainActivity mainActivity);

    void inject(SplashActivity splashActivity);

    void inject(SignInActivity signInActivity);
}
