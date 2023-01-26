package com.example.sinhvienapplication.utils.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.sinhvienapplication.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class DialogYesNo extends DialogFragment {
    Context mContext;
    View mView;

    public DialogYesNo(Context mContext) {
        this.mContext = mContext;
    }

    public DialogYesNo(@NonNull Context context, String title, String content, OnClickListener onClickListener) {
        this.mContext = context;
        this.title = title;
        this.content = content;
        this.onClickListener = onClickListener;
    }

    String title, content;
    TextView mTitleTv, mContentTv, mYesTv, mNoTv;
    OnClickListener onClickListener;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(mView);
        mTitleTv.setText(title);
        mContentTv.setText(content);

        mYesTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.onYes();
            }
        });

        mView.findViewById(R.id.close_iv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });

        mNoTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.dialog_yes_no, container);
        return mView;
    }

    private void initView(View mView) {
        mTitleTv = mView.findViewById(R.id.title_tv);
        mContentTv = mView.findViewById(R.id.content_tv);
        mYesTv = mView.findViewById(R.id.yes_tv);
        mNoTv = mView.findViewById(R.id.no_tv);

    }

    public interface OnClickListener{
        void onYes();
    }

}
