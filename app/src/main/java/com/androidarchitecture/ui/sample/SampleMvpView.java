package com.androidarchitecture.ui.sample;

import com.androidarchitecture.data.vo.Sample;
import com.androidarchitecture.ui.base.MvpView;

import java.util.List;


public interface SampleMvpView extends MvpView {

    void showSamples(List<Sample> samples);

    void showSamplesEmpty();

    void showError();

}
