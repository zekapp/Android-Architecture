package com.androidarchitecture.data.remote;

import com.androidarchitecture.data.remote.responses.SampleResponseData;
import com.androidarchitecture.data.remote.responses.TokenResponse;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Zeki Guler on 10,March,2016
 * ©2015 Appscore. All Rights Reserved
 *
 *
 * This clas is an example of the how to use ApiService
 *
 * Do not modify this class instead create a new one and add your api
 *
 * then create
 *
 */
public interface ApiService {

    // Example 1
    @Headers({
            "Authorization: Basic MjI3RkdOOmMxODc2NTNjY2U2MGY0NjU4MWVlYmUwZDVmMTE4NjVi"
    })
    @FormUrlEncoded
    @POST("oauth2/token")
    Observable<TokenResponse> accessTokenRequest(@Field("client_id") String clientId,
                                                 @Field("redirect_uri") String redirectUri,
                                                 @Field("code") String code,
                                                 @Field("grant_type") String grantType);

    // Example 2
    @GET("samples")
    Observable<SampleResponseData> getSamples(@Query("page") int page, @Query("per_page") int perPage);


    // Example 3
    @GET("1/user/{userId}/profile.json")
    Observable<String> getProfile(@Path("userId") String userId);


}
