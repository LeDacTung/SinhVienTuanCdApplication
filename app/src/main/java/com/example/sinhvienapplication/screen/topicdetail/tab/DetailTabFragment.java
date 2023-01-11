package com.example.sinhvienapplication.screen.topicdetail.tab;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sinhvienapplication.R;
import com.example.sinhvienapplication.adapter.InfoUserAdapter;
import com.example.sinhvienapplication.base.BaseFragment;
import com.example.sinhvienapplication.constant.Constant;
import com.example.sinhvienapplication.firebase.user.UserMethodFirebase;
import com.example.sinhvienapplication.model.Topic;
import com.example.sinhvienapplication.model.User;
import com.example.sinhvienapplication.screen.topicdetail.TopicDetailActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

public class DetailTabFragment extends BaseFragment<TopicDetailActivity> {

    public static DetailTabFragment newInstance(Topic topic) {
        Bundle args = new Bundle();
        args.putSerializable(Constant.Intent.TOPIC_DETAIL, topic);
        DetailTabFragment fragment = new DetailTabFragment();
        fragment.setArguments(args);
        return fragment;
    }

    Topic mTopic;
    RecyclerView mRecyclerView;
    InfoUserAdapter infoUserAdapter;
    ArrayList<User> mUsers;
    UserMethodFirebase userMethodFirebase = new UserMethodFirebase();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tab_detail;

    }

    @Override
    public void onPrepareLayout() {
        super.onPrepareLayout();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mTopic = (Topic) bundle.getSerializable(Constant.Intent.TOPIC_DETAIL);
        }

        getRootView().findViewById(R.id.back_iv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getViewContext().onBackPressed();
            }
        });

        setUpAdapter();
        setUpRecycler();
        loadData();

    }

    private void loadData() {
        userMethodFirebase.userRef(mTopic.getUidStudent(), Constant.Firebase.TYPE_STUDENT_COLLECTION)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        User user = documentSnapshot.toObject(User.class);
                        infoUserAdapter.addUser(user);
                    }
                });

        userMethodFirebase.userRef(mTopic.getUidTeacher(), Constant.Firebase.TYPE_TEACHER_COLLECTION)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        User user = documentSnapshot.toObject(User.class);
                        infoUserAdapter.addUser(user);
                    }
                });
    }

    private void setUpAdapter() {
        mUsers = new ArrayList<>();
        infoUserAdapter = new InfoUserAdapter(getViewContext(), mUsers);
    }

    private void setUpRecycler() {
        mRecyclerView = getRootView().findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getViewContext()));
        mRecyclerView.setAdapter(infoUserAdapter);
    }
}
