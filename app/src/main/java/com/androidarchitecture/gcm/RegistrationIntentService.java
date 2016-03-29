package com.androidarchitecture.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.androidarchitecture.App;
import com.androidarchitecture.R;
import com.androidarchitecture.data.local.PreferencesHelper;
import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by Zeki Guler on 29,March,2016
 * ©2015 Appscore. All Rights Reserved
 */
public class RegistrationIntentService extends IntentService{

    private static final String TAG = "RegistrationIntentService";
    private static final String[] TOPICS = {"global"};

    @Inject
    PreferencesHelper mPrefrenceHelper;

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        App.get(this).getComponent().inject(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try{
            Timber.i("onHandleIntent");

            InstanceID instanceID = InstanceID.getInstance(this);
            // R.string.gcm_sender_id (the Sender ID) is in Gradle File.
            String token = instanceID.getToken(getString(R.string.gcm_sender_id),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

            Timber.d("GCM Sender ID         : %s", getString(R.string.gcm_sender_id));
            Timber.d("GCM Registration Token: %s", token);

            // TODO: Implement this method to send any registration to your app's servers.
            sendRegistrationToServer(token);

            mPrefrenceHelper.saveGCMToken(token);

            subscribeTopics(token);

            // You should store a boolean that indicates whether the generated token has been
            // sent to your server. If the boolean is false, send the token to your server,
            // otherwise your server should have already received the token.
            mPrefrenceHelper.setGCMTokenSavedToServer(true);

        }catch (Exception e){
            // set as empty if not
            mPrefrenceHelper.setGCMTokenSavedToServer(false);
            mPrefrenceHelper.saveGCMToken("");
        }
        Intent registrationComplete = new Intent(QuickstartPreferences.REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }


    /**
     * Persist registration to third-party servers.
     *
     * Modify this method to associate the user's GCM registration token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.
    }


    /**
     * Subscribe to any GCM topics of interest, as defined by the TOPICS constant.
     *
     * @param token GCM token
     * @throws IOException if unable to reach the GCM PubSub service
     */
    private void subscribeTopics(String token) throws IOException {
        GcmPubSub pubSub = GcmPubSub.getInstance(this);
        for (String topic : TOPICS) {
            pubSub.subscribe(token, "/topics/" + topic, null);
        }
    }
}
