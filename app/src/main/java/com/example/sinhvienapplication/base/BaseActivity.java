package com.example.sinhvienapplication.base;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity implements BaseView {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        onPrepareLayout();
    }

    protected abstract int getLayoutId();

    @Override
    public BaseActivity getViewContext()  {
        return this;
    }

    @Override
    public void hideProgress() {
        Log.d("hideProgress", "hideProgress");
    }

    @Override
    public void onPrepareLayout() {
        Log.d("onPrepareLayout", "onPrepareLayout");
    }

    @Override
    public void showProgress() {
        Log.d("showProgress", "showProgress");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
