package com.androidarchitecture.data.local;


import android.database.sqlite.SQLiteDatabase;

import com.androidarchitecture.data.vo.Sample;
import com.raizlabs.android.dbflow.runtime.TransactionManager;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.Subscriber;
import timber.log.Timber;

/**
 * Created by Zeki Guler on 18,January,2016
 * ©2015 Appscore. All Rights Reserved
 */
@Singleton
public class DatabaseHelper {

    private SQLiteDatabase mDb;

    @Inject
    public DatabaseHelper(SQLiteDatabase database){
        mDb = database;
    }

    public void clearTables(){
        Delete.table(Sample.class);
    }

    /**
     * Set current values (checks PrimaryKey). If it is not in Db then add new row.
     * */
    public Observable<Sample> setSamples(final List<Sample> samples) {
        Timber.d("Observable<Sample> setSamples(final List<Sample> samples)");
        return Observable.create( new Observable.OnSubscribe<Sample>(){
            @Override
            public void call(final Subscriber<? super Sample> subscriber) {
                TransactionManager.transact(mDb, new Runnable() {
                    @Override
                    public void run() {
                        for (Sample sample: samples){
                            sample.save();

                            subscriber.onNext(sample);
                        }
                        subscriber.onCompleted();
                    }
                });
            }
        });
    }


    public List<Sample> sampleListQuery(){
        Timber.d("List<Sample> sampleListQuery()");
        return new Select().from(Sample.class).queryList();
    }

    public List<Sample> sampleListPageQuery(int page, int perPage){
        return new Select().from(Sample.class).where().limit(page*perPage).offset(perPage).queryList();
    }
}
