package com.androidarchitecture.data.remote.oauth;

import com.androidarchitecture.data.local.PreferencesHelper;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

@Singleton
public final class OauthInterceptor implements Interceptor {

  private final PreferencesHelper mPreferenceHelper;

  @Inject
  public OauthInterceptor(PreferencesHelper preferencesHelper) {
    this.mPreferenceHelper = preferencesHelper;
  }

  @Override public Response intercept(Chain chain) throws IOException {
    Request.Builder builder = chain.request().newBuilder();

    /**
     * If you need to change header use this method instead of setting  inline
     *
     * Build your own logic here
     *
     * */
/*
    if (mPreferenceHelper.isTokenSet()) {
      Timber.d("OauthInterceptor called and header changed");
      builder.header("Authorization", "Bearer " + preferencesHelper.getAccessToken());
    }
*/
    return chain.proceed(builder.build());
  }
}
