package com.example.sinhvienapplication.screen.topicdetail.tab;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sinhvienapplication.R;
import com.example.sinhvienapplication.adapter.InfoUserAdapter;
import com.example.sinhvienapplication.base.BaseFragment;
import com.example.sinhvienapplication.constant.Constant;
import com.example.sinhvienapplication.firebase.topic.TopicMethodFirebase;
import com.example.sinhvienapplication.firebase.user.UserMethodFirebase;
import com.example.sinhvienapplication.model.Topic;
import com.example.sinhvienapplication.model.User;
import com.example.sinhvienapplication.savedata.PrefManager;
import com.example.sinhvienapplication.screen.topicdetail.TopicDetailActivity;
import com.example.sinhvienapplication.utils.dialog.DialogUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

public class DetailTabFragment extends BaseFragment<TopicDetailActivity> {

    public synchronized static DetailTabFragment newInstance(Topic topic) {
        Bundle args = new Bundle();
        args.putSerializable(Constant.Intent.TOPIC_DETAIL, topic);
        DetailTabFragment fragment = new DetailTabFragment();
        fragment.setArguments(args);
        return fragment;
    }

    EditText mMarkEdt;
    Button mSaveMarkBtn;
    ConstraintLayout mMarkLayout;
    TextView mNameFileTv, mSizeTv;

    Topic mTopic;
    RecyclerView mRecyclerView;
    InfoUserAdapter infoUserAdapter;
    ArrayList<User> mUsers;
    final UserMethodFirebase userMethodFirebase = new UserMethodFirebase();
    final TopicMethodFirebase topicMethodFirebase = new TopicMethodFirebase();

    private void initView() {
        mNameFileTv = getRootView().findViewById(R.id.name_file_tv);
        mSizeTv = getRootView().findViewById(R.id.size_tv);
        mMarkEdt = getRootView().findViewById(R.id.mark_edt);
        mSaveMarkBtn = getRootView().findViewById(R.id.save_mark_btn);
        mMarkLayout = getRootView().findViewById(R.id.layoutMark);

        getRootView().findViewById(R.id.back_iv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getViewContext().onBackPressed();
            }
        });

        mSaveMarkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String mark = mMarkEdt.getText().toString();
                if(TextUtils.isEmpty(mark)) {
                    Toast.makeText(getViewContext(), "Mark empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    float f_mark = Float.parseFloat(mark);
                    if(f_mark < 0 || f_mark > 10) {
                        Toast.makeText(getViewContext(), "Mark error!!!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    DialogUtils.showProgressDialog(getViewContext(), "Update");
                    mTopic.setMark(mark);
                    topicMethodFirebase.markFileTeacher(mTopic).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            DialogUtils.dismissProgressDialog();
                            topicMethodFirebase.markFileStudent(mTopic);
                            Toast.makeText(getViewContext(), "Update mark success!!!", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            DialogUtils.dismissProgressDialog();
                            Toast.makeText(getViewContext(), "Error!!!", Toast.LENGTH_SHORT).show();
                        }
                    });

                } catch (Exception e) {
                    Log.d("ex", e.getMessage());
                }
            }
        });
    }

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

        initView();
        checkShowBtnMark();
        setUpAdapter();
        setUpRecycler();
        loadData();
        saveDate();
    }

    private void saveDate(){
        mSizeTv.setText(mTopic.getSizeFile());
        mNameFileTv.setText(mTopic.getNameFile());
        mMarkEdt.setText(mTopic.getMark());
    }

    private void checkShowBtnMark(){
        if(PrefManager.getTypeUser(getViewContext()).equals(Constant.Firebase.TYPE_STUDENT_COLLECTION)){
            mMarkLayout.setVisibility(View.GONE);
        }else {
            mMarkLayout.setVisibility(View.VISIBLE);
        }
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
