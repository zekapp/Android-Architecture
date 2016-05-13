package com.androidarchitecture.play;

import android.content.Context;

import com.androidarchitecture.di.qualifier.ApplicationContext;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import javax.inject.Singleton;

import timber.log.Timber;

/**
 * Created by Zeki Guler on 13,May,2016
 * ©2015 Appscore. All Rights Reserved
 */
@Singleton
public class GooglePlayServices {

    private Context mContext;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    public GooglePlayServices(@ApplicationContext Context context){

        mContext = context;
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(mContext);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {

                apiAvailability.getErrorDialog(mContext, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Timber.i("This device is not supported.");
            }
            return false;
        }
        return true;
    }
}
