package com.example.sinhvienapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sinhvienapplication.R;
import com.example.sinhvienapplication.constant.Constant;
import com.example.sinhvienapplication.model.Topic;
import com.example.sinhvienapplication.utils.image.ImageUtils;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.ViewHolder> {

    Context mContext;
    ArrayList<Topic> mTopics;
    TopicClickListener topicClickListener;

    public void setTopicClickListener(TopicClickListener topicClickListener) {
        this.topicClickListener = topicClickListener;
    }

    public void addTopic(ArrayList<Topic> topics){
        mTopics.addAll(topics);
        notifyDataSetChanged();
    }

    public TopicAdapter(Context context, ArrayList<Topic> mTopics) {
        this.mContext = context;
        this.mTopics = mTopics;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_topic, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Topic topic = mTopics.get(position);
        holder.bindData(topic, mContext);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                topicClickListener.onClick(topic);
            }
        });

        holder.mLayoutFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                topicClickListener.onViewFile(topic);
            }
        });

        holder.mApproveTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                topicClickListener.onApproveFile(topic);
            }
        });

        holder.mEditTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                topicClickListener.onEdit(topic);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTopics.size();
    }

    public interface TopicClickListener {
        void onClick(Topic topic);

        void onEdit(Topic topic);

        void onApproveFile(Topic topic);

        void onViewFile(Topic topic);
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView mImageUser;
        TextView mSizeFileTv, mNameFileTv;
        TextView mNameTv, mTimeTv, mDescriptionTv, mGradeTv;
        LinearLayout mLayoutFile;
        TextView mApproveTv, mNoteApproveTv;
        TextView mEditTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mSizeFileTv = itemView.findViewById(R.id.size_tv);
            mNameFileTv = itemView.findViewById(R.id.name_file_tv);

            mNameTv = itemView.findViewById(R.id.title_tv);
            mTimeTv = itemView.findViewById(R.id.time_tv);
            mDescriptionTv = itemView.findViewById(R.id.description_tv);
            mGradeTv = itemView.findViewById(R.id.class_tv);
            mImageUser = itemView.findViewById(R.id.image);
            mLayoutFile = itemView.findViewById(R.id.layoutFile);

            mApproveTv = itemView.findViewById(R.id.approve_tv);
            mNoteApproveTv = itemView.findViewById(R.id.note_approve_tv);
            mEditTv = itemView.findViewById(R.id.edit_tv);
        }

        public void bindData(Topic topic, Context context){
            mSizeFileTv.setText(topic.getSizeFile());
            mNameFileTv.setText(topic.getNameFile());

            mNameTv.setText(topic.getNameStudent());
            mTimeTv.setText(topic.getTime());
            mDescriptionTv.setText(topic.getDescription());
            mGradeTv.setText(topic.getGradeStudent());
            ImageUtils.loadImage(context, mImageUser, topic.getImageStudent());

            if(topic.getStatus().equals(Constant.FILE.STATUS_NOT_APPROVE) &&
                    topic.getUidTeacher().equals(FirebaseAuth.getInstance().getUid())){
                mApproveTv.setVisibility(View.VISIBLE);
            }else {
                mApproveTv.setVisibility(View.GONE);
            }

            if(topic.getStatus().equals(Constant.FILE.STATUS_APPROVE)){
                mNoteApproveTv.setVisibility(View.VISIBLE);
            }else {
                mNoteApproveTv.setVisibility(View.GONE);
            }

            if(topic.getUidStudent().equals(FirebaseAuth.getInstance().getUid())
             && topic.getStatus().equals(Constant.FILE.STATUS_NOT_APPROVE)){
                mEditTv.setVisibility(View.VISIBLE);
            }else {
                mEditTv.setVisibility(View.GONE);
            }
        }
    }
}
