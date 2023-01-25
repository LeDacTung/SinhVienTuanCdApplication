package com.example.sinhvienapplication.screen.home;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.sinhvienapplication.R;
import com.example.sinhvienapplication.base.BaseActivity;
import com.example.sinhvienapplication.constant.Constant;
import com.example.sinhvienapplication.savedata.PrefManager;
import com.example.sinhvienapplication.screen.home.tab.AccountTabFragment;
import com.example.sinhvienapplication.screen.home.tab.HomeTabFragment;
import com.example.sinhvienapplication.screen.uploadfile.UploadFileActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class HomeActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
    }

    BottomNavigationView mBottomNavigationView;
    
    @Override
    public void onPrepareLayout() {
        super.onPrepareLayout();

        addFragmentToActivity(new HomeTabFragment());
        initView();
    }

    private void initView() {
        mBottomNavigationView = findViewById(R.id.bottomNavigationView);

        mBottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home_menu:
                        addFragmentToActivity(new HomeTabFragment());
                        break;
                    case R.id.upload_file_menu:
                        startActivity(new Intent(HomeActivity.this, UploadFileActivity.class));
                        break;
                    case R.id.person_menu:
                        addFragmentToActivity(new AccountTabFragment());
                        break;
                }
                return false;
            }
        });

    }

    private void addFragmentToActivity(Fragment fragment){
        if (fragment == null) return;
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction tr = fm.beginTransaction();
        tr.replace(R.id.frameLayout, fragment);
        tr.commit();
    }
}
