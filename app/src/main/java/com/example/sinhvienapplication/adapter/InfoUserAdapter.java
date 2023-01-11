package com.example.sinhvienapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sinhvienapplication.R;
import com.example.sinhvienapplication.model.User;
import com.example.sinhvienapplication.utils.image.ImageUtils;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class InfoUserAdapter extends RecyclerView.Adapter<InfoUserAdapter.ViewHolder> {

    Context mContext;
    ArrayList<User> mUsers;

    public InfoUserAdapter(Context mContext, ArrayList<User> mUsers) {
        this.mContext = mContext;
        this.mUsers = mUsers;
    }

    public void addUser(User user){
        mUsers.add(user);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_info_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = mUsers.get(position);
        holder.bindData(user, mContext);
    }

    @Override
    public int getItemCount() {
        return mUsers.size() != 0 ? mUsers.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView mImageUser;
        TextView mNameTv, mGradeTv, mGmailTv, mPhoneTv, mTypeTv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mNameTv = itemView.findViewById(R.id.title_tv);
            mGmailTv = itemView.findViewById(R.id.gmail_tv);
            mPhoneTv = itemView.findViewById(R.id.phone_tv);
            mGradeTv = itemView.findViewById(R.id.class_tv);
            mImageUser = itemView.findViewById(R.id.image);
            mTypeTv = itemView.findViewById(R.id.type_tv);
        }

        public void bindData(User user, Context context){
            mNameTv.setText(user.getName());
            mGmailTv.setText(user.getGmail());
            mPhoneTv.setText(user.getPhone());
            ImageUtils.loadImage(context, mImageUser, user.getImage());
            mTypeTv.setText(user.getType());

            if(user.getGrade() != ""){
                mGradeTv.setText(user.getGrade());
            }else {
                mGradeTv.setVisibility(View.GONE);
            }
        }
    }
}
