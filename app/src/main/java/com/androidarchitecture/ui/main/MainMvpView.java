package com.androidarchitecture.ui.main;

import com.androidarchitecture.data.vo.Sample;
import com.androidarchitecture.ui.base.MvpView;

import java.util.List;


public interface MainMvpView extends MvpView {

    void showSamples(List<Sample> samples);

    void showSamplesEmpty();

    void showError();

}
