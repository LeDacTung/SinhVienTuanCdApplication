package com.example.sinhvienapplication.utils.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.sinhvienapplication.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class DialogYesNo extends MaterialAlertDialogBuilder implements DialogInterface.OnDismissListener {
    Context mContext;
    public DialogYesNo(@NonNull Context context) {
        super(context);
        mContext =context;
    }

    public DialogYesNo(@NonNull Context context, String title, String content, OnClickListener onClickListener) {
        super(context);
        this.mContext = context;
        this.title = title;
        this.content = content;
        this.onClickListener = onClickListener;
    }

    String title, content;
    TextView mTitleTv, mContentTv, mYesTv, mNoTv;
    OnClickListener onClickListener;

    @NonNull
    @Override
    public MaterialAlertDialogBuilder setBackground(@Nullable Drawable background) {
        return super.setBackground(background);
    }

    @NonNull
    @Override
    public MaterialAlertDialogBuilder setView(@Nullable View view) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.dialog_yes_no, null);
        initView(mView);
        setBackground(mContext.getDrawable(R.drawable.bg_dialog_white));
        mTitleTv.setText(title);
        mContentTv.setText(content);

        mYesTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.onYes();
            }
        });

        return super.setView(mView);
    }

    @NonNull
    @Override
    public MaterialAlertDialogBuilder setOnDismissListener(@Nullable DialogInterface.OnDismissListener onDismissListener) {
        return super.setOnDismissListener(onDismissListener);
    }

    private void initView(View mView) {
        mTitleTv = mView.findViewById(R.id.title_tv);
        mContentTv = mView.findViewById(R.id.content_tv);
        mYesTv = mView.findViewById(R.id.yes_tv);
        mNoTv = mView.findViewById(R.id.no_tv);

        mView.findViewById(R.id.close_iv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDismiss(new DialogInterface() {
                    @Override
                    public void cancel() {

                    }

                    @Override
                    public void dismiss() {

                    }
                });
            }
        });
    }

    public interface OnClickListener{
        void onYes();
    }

    @Override
    public AlertDialog show() {
        setView(null);
        return super.show();
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {

    }
}
