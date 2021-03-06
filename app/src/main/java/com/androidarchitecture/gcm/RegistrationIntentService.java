package com.androidarchitecture.gcm;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v4.content.LocalBroadcastManager;

import com.androidarchitecture.App;
import com.androidarchitecture.R;
import com.androidarchitecture.data.DataManager;
import com.androidarchitecture.data.local.PreferencesHelper;
import com.androidarchitecture.data.remote.posts.UpdateGcmTokenPost;
import com.androidarchitecture.data.remote.responses.SuccessResponse;
import com.androidarchitecture.utils.AndroidComponentUtil;
import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

import javax.inject.Inject;

import retrofit2.Response;
import timber.log.Timber;

/**
 * Created by Zeki Guler on 29,March,2016
 * ©2015 Appscore. All Rights Reserved
 */
public class RegistrationIntentService extends IntentService{

    private static final String TAG = "RegistrationIntentService";
    private static final String[] TOPICS = {"global"};

    @Inject
    DataManager mDataManager;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, RegistrationIntentService.class);
    }

    public static boolean isRunning(Context context) {
        return AndroidComponentUtil.isServiceRunning(context, RegistrationIntentService.class);
    }

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

            String devId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

            Timber.d("GCM Sender ID         : %s", getString(R.string.gcm_sender_id));
            Timber.d("GCM Registration Token: %s", token);
            Timber.d("GCM Registration DevId: %s", devId);

            // TODO: Implement this method to send any registration to your app's servers.
            sendRegistrationToServer(token,devId);

            mDataManager.saveGCMToken(token);

            subscribeTopics(token);

            // You should store a boolean that indicates whether the generated token has been
            // sent to your server. If the boolean is false, send the token to your server,
            // otherwise your server should have already received the token.
            mDataManager.setGCMTokenSavedToServer(true);
            Timber.d("GCM Registration done successfully");
        }catch (Exception e){
            // set as empty if not
            Timber.d(e.getMessage());
            mDataManager.setGCMTokenSavedToServer(false);
            mDataManager.saveGCMToken("");
        }

        // don't worry about if it is not saved in our database. You can try next launch the app
        // leave it
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
    private void sendRegistrationToServer(String token, String uniqueId) throws IOException {
        // Add custom implementation, as needed.
        Response<SuccessResponse> response = mDataManager.updateTokenSynchronously(new UpdateGcmTokenPost.Builder(uniqueId,token).build());

        if(!response.isSuccessful()){
            Timber.d("Error: HttpError:%s, Message%s", response.code(), response.errorBody().string());
            throw new IOException("Http error: " + response.code());
        }
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
