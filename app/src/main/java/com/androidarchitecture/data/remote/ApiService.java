package com.androidarchitecture.data.remote;

import com.androidarchitecture.data.remote.responses.SampleResponse;
import com.androidarchitecture.vo.MData;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by zeki on 17/01/2016.
 */
public interface ApiService {

    @GET("samples")
    Observable<MData<SampleResponse>> getSamples(@Query("page") int page, @Query("per_page") int perPage);


}
