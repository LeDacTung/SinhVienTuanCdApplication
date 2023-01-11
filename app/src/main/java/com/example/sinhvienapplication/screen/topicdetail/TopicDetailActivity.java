package com.example.sinhvienapplication.screen.topicdetail;

import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.sinhvienapplication.R;
import com.example.sinhvienapplication.base.BaseActivity;
import com.example.sinhvienapplication.constant.Constant;
import com.example.sinhvienapplication.model.Topic;
import com.example.sinhvienapplication.screen.topicdetail.tab.DetailTabFragment;
import com.example.sinhvienapplication.screen.topicdetail.tab.DocumentTabFragment;

public class TopicDetailActivity extends BaseActivity {

    int mPosition = 0 ;
    Topic mTopic;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_detail;
    }

    @Override
    public void onPrepareLayout() {
        super.onPrepareLayout();
        mPosition = getIntent().getIntExtra(Constant.Intent.POSITION_TOPIC_DETAIL, 0);
        mTopic = (Topic) getIntent().getSerializableExtra(Constant.Intent.TOPIC_DETAIL);

        if(mPosition == 0 ){
            addFragmentToActivity(DetailTabFragment.newInstance(mTopic));
        }else {
            if(mTopic.getTypeFile().equals(Constant.FILE.PDF)){
                addFragmentToActivity(DocumentTabFragment.newInstance(mTopic));
            }else {
                Uri webpage = Uri.parse(mTopic.getFileUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                startActivity(intent);
            }
        }

    }

    public void addFragmentToActivity(Fragment fragment){
        if (fragment == null) return;
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction tr = fm.beginTransaction();
        tr.replace(R.id.frameLayout, fragment);
        tr.commit();
    }
}
