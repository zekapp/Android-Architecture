package com.androidarchitecture.di.component;

import com.androidarchitecture.di.module.ActivityModule;
import com.androidarchitecture.di.scope.PerActivity;
import com.androidarchitecture.ui.main.MainActivity;

import dagger.Component;

/**
 * Created by zeki on 17/01/2016.
 */

@PerActivity
@Component(dependencies = AppComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity mainActivity);

}
