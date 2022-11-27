package com.example.sinhvienapplication.base;

public interface BaseView extends BasePresenter{
    void showProgress();

    void hideProgress();

    void onPrepareLayout();

    @Override
    BaseActivity getViewContext();
}
