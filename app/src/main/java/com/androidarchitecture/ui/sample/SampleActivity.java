package com.androidarchitecture.ui.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.androidarchitecture.R;
import com.androidarchitecture.data.vo.Sample;

import com.androidarchitecture.ui.base.BaseActivity;
import com.androidarchitecture.utils.DialogFactory;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Zeki Guler on 20,January,2016
 * ©2015 Appscore. All Rights Reserved
 */


public class SampleActivity extends BaseActivity implements SampleMvpView {

    @Inject SamplePresenter mSamplePresenter;
    @Inject SampleAdapter mSampleAdapter;

    @Bind(R.id.recycler_view) RecyclerView mRecyclerView;

    public static Intent newIntent(Context context ) {
        Intent intent = new Intent(context, SampleActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);
        setContentView(R.layout.activity_sample);
        ButterKnife.bind(this);

        mRecyclerView.setAdapter(mSampleAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mSamplePresenter.attachView(this);
        mSamplePresenter.loadSamples();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSamplePresenter.detachView();
    }

    /********
     * MVP View Functions
     ********/


    @Override
    public void showSamples(List<Sample> samples) {
        mSampleAdapter.setSamples(samples);
        mSampleAdapter.notifyDataSetChanged();
    }

    @Override
    public void showSamplesEmpty() {
        Toast.makeText(this, R.string.empty_samples, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showError() {
        DialogFactory.createGenericErrorDialog(this, getString(R.string.error_loading_sample))
                .show();
    }
}
