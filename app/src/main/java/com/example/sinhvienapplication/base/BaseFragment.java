package com.example.sinhvienapplication.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public abstract class BaseFragment<T extends BaseActivity> extends Fragment implements BaseView {

    View mRootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(getLayoutId(), container, false);
        onPrepareLayout();
        return  mRootView;
    }

    public View getRootView() {
        return mRootView;
    }

    protected T getBaseActivity()  {
        return (T) getActivity() ;
    }

    protected abstract int getLayoutId();

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void onPrepareLayout() {

    }

    @Override
    public BaseActivity getViewContext() {
        return (BaseActivity) getActivity();
    }
}
