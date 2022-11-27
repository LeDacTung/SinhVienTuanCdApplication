package com.example.sinhvienapplication.screen.detail;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.sinhvienapplication.R;
import com.example.sinhvienapplication.base.BaseActivity;
import com.example.sinhvienapplication.screen.detail.tab.DetailTabFragment;

public class DetailActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_detail;
    }

    @Override
    public void onPrepareLayout() {
        super.onPrepareLayout();
        addFragmentToActivity(new DetailTabFragment());
    }

    public void addFragmentToActivity(Fragment fragment){
        if (fragment == null) return;
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction tr = fm.beginTransaction();
        tr.replace(R.id.frameLayout, fragment);
        tr.commit();
    }
}
