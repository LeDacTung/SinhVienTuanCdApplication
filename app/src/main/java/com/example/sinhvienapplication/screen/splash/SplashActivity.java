package com.example.sinhvienapplication.screen.splash;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.example.sinhvienapplication.R;
import com.example.sinhvienapplication.base.BaseActivity;
import com.example.sinhvienapplication.constant.Constant;
import com.example.sinhvienapplication.savedata.PrefManager;
import com.example.sinhvienapplication.screen.login.LoginActivity;

public class SplashActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    TextView mStudentBtn, mTeacherBtn, mAdminBtn;

    @Override
    public void onPrepareLayout() {
        super.onPrepareLayout();
        mStudentBtn = findViewById(R.id.student_btn);
        mAdminBtn = findViewById(R.id.admin_btn);
        mTeacherBtn = findViewById(R.id.teacher_btn);

        if(PrefManager.getOpenFirstApp(getViewContext()) || PrefManager.getTypeUser(getViewContext()).equals("")){
            mStudentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PrefManager.saveOpenFirstApp(getViewContext(), false);
                    PrefManager.saveTypeUser(getViewContext(), Constant.Firebase.TYPE_STUDENT_COLLECTION);
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                }
            });


            mTeacherBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PrefManager.saveOpenFirstApp(getViewContext(), false);
                    PrefManager.saveTypeUser(getViewContext(), Constant.Firebase.TYPE_TEACHER_COLLECTION);
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                }
            });

            mAdminBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PrefManager.saveOpenFirstApp(getViewContext(), false);
                    PrefManager.saveTypeUser(getViewContext(), Constant.Firebase.TYPE_ADMIN_COLLECTION);
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                }
            });
        }else {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
        }
    }


}
