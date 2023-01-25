package com.example.sinhvienapplication.screen.home.tab;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sinhvienapplication.R;
import com.example.sinhvienapplication.adapter.TopicAdapter;
import com.example.sinhvienapplication.base.BaseFragment;
import com.example.sinhvienapplication.constant.Constant;
import com.example.sinhvienapplication.firebase.FirebaseMethods;
import com.example.sinhvienapplication.firebase.topic.TopicMethodFirebase;
import com.example.sinhvienapplication.firebase.user.UserMethodFirebase;
import com.example.sinhvienapplication.model.Topic;
import com.example.sinhvienapplication.savedata.PrefManager;
import com.example.sinhvienapplication.screen.home.HomeActivity;
import com.example.sinhvienapplication.screen.search.SearchActivity;
import com.example.sinhvienapplication.screen.topicdetail.TopicDetailActivity;
import com.example.sinhvienapplication.screen.uploadfile.UploadFileActivity;
import com.example.sinhvienapplication.utils.dialog.DialogUtils;
import com.example.sinhvienapplication.utils.dialog.DialogYesNo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class HomeTabFragment extends BaseFragment<HomeActivity> implements TopicAdapter.TopicClickListener {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tab_home;
    }

    ArrayList<Topic> mTopics;
    TopicAdapter mTopicAdapter;
    RecyclerView mRecyclerView;
    ImageView mSearchIv;

    final TopicMethodFirebase topicMethodFirebase = new TopicMethodFirebase();
    final UserMethodFirebase userMethodFirebase = new UserMethodFirebase();
    final FirebaseMethods firebaseMethods = new FirebaseMethods();

    @Override
    public void onPrepareLayout() {
        super.onPrepareLayout();
        initView();
        setUpAdapter();
        setUpRecycler();

        if(PrefManager.getTypeUser(getViewContext()).equals(Constant.Firebase.TYPE_ADMIN_COLLECTION))
            loadDataAdmin();
        else
            loadData();
    }

    private void loadDataAdmin(){
        firebaseMethods.topicRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot snapshots) {
                ArrayList<Topic> topics = (ArrayList<Topic>) snapshots.toObjects(Topic.class);
                mTopicAdapter.addTopic(topics);
            }
        });
    }

    private void initView() {
        mSearchIv = getRootView().findViewById(R.id.search_iv);
        mSearchIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getViewContext(), SearchActivity.class));
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if(PrefManager.getTypeUser(getViewContext()).equals(Constant.Firebase.TYPE_ADMIN_COLLECTION))
            loadDataAdmin();
        else
            loadData();
    }

    private void loadData() {
        topicMethodFirebase.loadData(PrefManager.getTypeUser(getViewContext()),
                FirebaseAuth.getInstance().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot snapshots) {
                        ArrayList<Topic> topics = (ArrayList<Topic>) snapshots.toObjects(Topic.class);
                        mTopicAdapter.addTopic(topics);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getViewContext(), "Fail", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setUpAdapter() {
        mTopics = new ArrayList<>();
        mTopicAdapter = new TopicAdapter(getViewContext(), mTopics);
        mTopicAdapter.setTopicClickListener(this);
    }

    private void setUpRecycler() {
        mRecyclerView = getRootView().findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getViewContext()));
        mRecyclerView.setAdapter(mTopicAdapter);
    }

    @Override
    public void onClick(Topic topic) {
        Intent intent = new Intent(getViewContext(), TopicDetailActivity.class);
        intent.putExtra(Constant.Intent.TOPIC_DETAIL, topic);
        intent.putExtra(Constant.Intent.POSITION_TOPIC_DETAIL, 0);
        startActivity(intent);
    }

    @Override
    public void onEditFile(Topic topic) {
        Intent intent = new Intent(getViewContext(), UploadFileActivity.class);
        intent.putExtra(Constant.Intent.TOPIC_UPLOAD_FILE, topic);
        startActivity(intent);
    }

    @Override
    public void onDeleteFile(Topic topic) {
        DialogYesNo dialogYesNo = new DialogYesNo(getViewContext(),
                "Delete File : "+topic.getNameFile(),
                "Do you want delete file !!!",
                new DialogYesNo.OnClickListener() {
                    @Override
                    public void onYes() {
                        FirebaseAuth.getInstance().signOut();
                        deleteFile(topic);
                    }
                });
        dialogYesNo.show();
    }

    private void deleteFile(Topic topic){
        DialogUtils.showProgressDialog(getBaseActivity());
        topicMethodFirebase.deleteFileStudent(topic);
        topicMethodFirebase.deleteFileTeacher(topic);
        topicMethodFirebase.deleteFileAdmin(topic).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                DialogUtils.dismissProgressDialog();
                Toast.makeText(getBaseActivity(), "Delete success !!!", Toast.LENGTH_SHORT).show();

                loadData();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                DialogUtils.dismissProgressDialog();
                Toast.makeText(getBaseActivity(), "Delete fail !!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onApproveFile(Topic topic) {
        topic.setStatus(Constant.FILE.STATUS_APPROVE);
        topicMethodFirebase.approveFileAdmin(topic)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        topicMethodFirebase.approveFileTeacher(topic);
                        topicMethodFirebase.approveFileStudent(topic);
                        mTopicAdapter.notifyDataSetChanged();
                        Toast.makeText(getViewContext(), "Approve file success", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onViewFile(Topic topic) {
        Intent intent = new Intent(getViewContext(), TopicDetailActivity.class);
        intent.putExtra(Constant.Intent.TOPIC_DETAIL, topic);
        intent.putExtra(Constant.Intent.POSITION_TOPIC_DETAIL, 1);
        startActivity(intent);
    }
}
