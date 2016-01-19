package com.androidarchitecture.data.vo;

import com.androidarchitecture.data.local.AppDatabase;
import com.androidarchitecture.utils.Validation;
import com.androidarchitecture.utils.ValidationFailedException;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by zeki on 17/01/2016.
 */

@Table(databaseName = AppDatabase.NAME)
public class Sample extends BaseModel implements Validation {
    @Column
    @PrimaryKey(autoincrement = false)
    @JsonProperty("id")
    long mSampleId;

    @Column
    @JsonProperty("description")
    String mDescription;

    @Column
    @JsonProperty("date_time")
    long mTime;


    public void validate() {
        if (mSampleId < 1) {
            throw new ValidationFailedException("invalid sample id");
        }
    }

}
