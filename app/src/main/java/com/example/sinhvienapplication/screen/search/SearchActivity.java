package com.example.sinhvienapplication.screen.search;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sinhvienapplication.R;
import com.example.sinhvienapplication.adapter.TopicAdapter;
import com.example.sinhvienapplication.base.BaseActivity;
import com.example.sinhvienapplication.firebase.topic.TopicMethodFirebase;
import com.example.sinhvienapplication.model.Topic;
import com.example.sinhvienapplication.savedata.PrefManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SearchActivity extends BaseActivity implements TopicAdapter.TopicClickListener {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
    }

    RecyclerView mRecyclerView;
    ArrayList<Topic> mTopics;
    TopicAdapter mTopicAdapter;
    TopicMethodFirebase topicMethodFirebase = new TopicMethodFirebase();
    FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
    EditText mSearchEdt;

    private void initView(){
        mRecyclerView = findViewById(R.id.recyclerview);
        mSearchEdt = findViewById(R.id.search_edt);
    }

    @Override
    public void onPrepareLayout() {
        super.onPrepareLayout();
        initView();
        setUpAdapter();
        setUpRecycler();


        mSearchEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // value_test_default =  "zbbshdbdbxb"
                String value = charSequence.toString();
                loadData(value);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void loadData(String value) {
        topicMethodFirebase.searchTopic(value,
                mUser.getUid(),
                PrefManager.getTypeUser(getViewContext()))
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        mTopics.addAll(value.toObjects(Topic.class));
                        mTopicAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void setUpAdapter() {
        mTopics = new ArrayList<>();
        mTopicAdapter = new TopicAdapter(getViewContext(), mTopics);
        mTopicAdapter.setTopicClickListener(this);
    }

    private void setUpRecycler() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getViewContext()));
        mRecyclerView.setAdapter(mTopicAdapter);
    }

    @Override
    public void onClick(Topic topic) {

    }

    @Override
    public void onEditFile(Topic topic) {

    }

    @Override
    public void onDeleteFile(Topic topic) {

    }

    @Override
    public void onApproveFile(Topic topic) {

    }

    @Override
    public void onViewFile(Topic topic) {

    }
}
