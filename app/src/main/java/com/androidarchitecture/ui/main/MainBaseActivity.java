package com.androidarchitecture.ui.main;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.androidarchitecture.R;
import com.androidarchitecture.data.DataManager;
import com.androidarchitecture.data.vo.Sample;
import com.androidarchitecture.databinding.ActivityMainBinding;
import com.androidarchitecture.ui.base.BaseActivity;

import java.util.List;

import javax.inject.Inject;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by Zeki Guler on 20,January,2016
 * ©2015 Appscore. All Rights Reserved
 */
public class MainBaseActivity  extends BaseActivity {

    @Inject
    DataManager mDataManager;

    ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


    }


    @Override
    protected void onStart() {
        super.onStart();
        refresh(null);
        mDataManager.fetchSamplesAsync(0, 10);
    }

    private void refresh(@Nullable Sample referencePost) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void fetchData(View view) {
        mDataManager.getSamplesFromDbThenUpdateViaApi(0,30)

                .map(new Func1<List<Sample>, String>() {
            @Override
            public String call(List<Sample> samples) {
                String str = "";
                for (Sample s : samples) {
                    str += String.format("%s %s %s \n", s.getSampleId(), s.getDescription(), s.getTime());
                }

                return str;
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                        Timber.d("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e.getMessage());
                    }

                    @Override
                    public void onNext(String s) {
                        Timber.d("onNext");
                        mBinding.databaseInput.setText(s);
                    }
                });
    }

    public void clearData(View view) {
        mBinding.databaseInput.setText("");
    }
}