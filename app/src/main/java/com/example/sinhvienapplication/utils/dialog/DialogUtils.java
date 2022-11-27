package com.example.sinhvienapplication.utils.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;

import com.example.sinhvienapplication.R;

public class DialogUtils {
    private static AlertDialog sAlert;
    private static ProgressDialog sProgress;

    public static synchronized void showProgressDialog(final Activity activity) {
        try {
            if (activity != null && !activity.isFinishing()) {
                if (sAlert != null && sAlert.isShowing()) {
                    sAlert.dismiss();
                }
                if (sProgress != null && sProgress.isShowing()) {
                    sProgress.dismiss();
                }
                sProgress = new ProgressDialog(activity);
                sProgress.setCancelable(false);
                sProgress.setIndeterminateDrawable(activity.getResources().getDrawable(R.drawable.progress_bar));
                sProgress.show();
            }

        } catch (Exception e) {

        }
    }

    public static synchronized void showProgressDialog(final Activity activity, String message) {
        try {
            if (activity != null && !activity.isFinishing()) {
                if (sAlert != null && sAlert.isShowing()) {
                    sAlert.dismiss();
                }
                if (sProgress != null && sProgress.isShowing()) {
                    sProgress.dismiss();
                }
                sProgress = new ProgressDialog(activity);
                sProgress.setCancelable(false);
                sProgress.setIndeterminateDrawable(activity.getResources().getDrawable(R.drawable.progress_bar));
                sProgress.setMessage(message);
                sProgress.show();
            }

        } catch (Exception e) {

        }
    }

    public static void dismissProgressDialog() {
        try {
            if (sProgress != null && sProgress.isShowing()) {
                sProgress.dismiss();
            }
        } catch (Exception e) {
        }
    }
}
