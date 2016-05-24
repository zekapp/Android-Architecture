package com.androidarchitecture.ui.splash;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.androidarchitecture.R;
import com.androidarchitecture.data.local.PreferencesHelper;
import com.androidarchitecture.gcm.QuickstartPreferences;
import com.androidarchitecture.gcm.RegistrationIntentService;
import com.androidarchitecture.ui.base.BaseActivity;
import com.androidarchitecture.ui.login.SignInActivity;
import com.androidarchitecture.ui.sample.SampleActivity;
import com.androidarchitecture.utils.Utils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by Zeki Guler on 29,January,2016
 * ©2015 Appscore. All Rights Reserved
 */
public class SplashActivity extends BaseActivity implements SplashMvpView {

    private static final long SPLASH_TIME = 2500;

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private Thread splashTread;
    private boolean interrupted = false;

    private BroadcastReceiver mRegistrationBroadcastReceiver;

    private boolean isReceiverRegistered;

    @Inject
    SplashPresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);
        setContentView(R.layout.splash_screen);
        mPresenter.attachView(this);
        //print KeyHash. May need for Facebook Login
        Utils.printHashKey(this);

        mPresenter.attachView(this);

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                startWaitThread();
            }
        };

        mPresenter.isThisDeviceRegisterForGCM();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        isReceiverRegistered = false;
        super.onPause();
    }

    private void registerReceiver(){
        if(!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
        }
    }

    private void startWaitThread() {
        splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    synchronized(this){
                        wait(SPLASH_TIME);
                    }
                } catch(InterruptedException e) {
                    Timber.e(e.getMessage());
                } finally {
                    if(!interrupted){
                        closeSplash();
                        interrupt();
                    }
                }
            }
        };

        splashTread.start();
    }

    private void closeSplash() {
        Intent i = SignInActivity.newIntent(this);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
        finish();
    }

    /********
     * MVP View Functions
     ********/

    @Override
    public void deviceRegisteredSuccessfully(String gcmToken) {
        Timber.i("GCM Token: %s", gcmToken);
        startWaitThread();
    }

    @Override
    public void deviceNeedsToBeRegister() {
        if (checkPlayServices()){
            registerReceiver();
            // Start IntentService to register this application with GCM.
            startService(RegistrationIntentService.getStartIntent(this));
        }
    }


    /********
     * Activity functions
     ********/


    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Timber.i("This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

}
