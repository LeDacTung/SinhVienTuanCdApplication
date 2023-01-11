package com.example.sinhvienapplication.validate;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sinhvienapplication.constant.Constant;
import com.example.sinhvienapplication.firebase.user.UserMethodFirebase;
import com.example.sinhvienapplication.model.User;
import com.example.sinhvienapplication.savedata.PrefManager;
import com.example.sinhvienapplication.utils.image.ImageUtils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

public class UserValidate {
    public static void isEmptyEdt(Context context, EditText editText, String message){
        editText.requestFocus();
        Toast.makeText(context, ""+message, Toast.LENGTH_SHORT).show();
    }
}
