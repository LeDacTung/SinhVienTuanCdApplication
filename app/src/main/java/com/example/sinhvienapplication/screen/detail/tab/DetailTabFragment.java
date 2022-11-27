package com.example.sinhvienapplication.screen.detail.tab;

import android.view.View;

import com.example.sinhvienapplication.R;
import com.example.sinhvienapplication.base.BaseFragment;
import com.example.sinhvienapplication.screen.detail.DetailActivity;

public class DetailTabFragment extends BaseFragment<DetailActivity> {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tab_detail;

    }

    @Override
    public void onPrepareLayout() {
        super.onPrepareLayout();
        getRootView().findViewById(R.id.pdf).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getBaseActivity().addFragmentToActivity(new DocumentTabFragment());
            }
        });
    }
}
