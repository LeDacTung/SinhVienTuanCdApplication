package com.example.sinhvienapplication.screen.register;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.sinhvienapplication.R;
import com.example.sinhvienapplication.base.BaseActivity;
import com.example.sinhvienapplication.constant.Constant;
import com.example.sinhvienapplication.firebase.user.UserMethodFirebase;
import com.example.sinhvienapplication.model.User;
import com.example.sinhvienapplication.savedata.PrefManager;
import com.example.sinhvienapplication.utils.dialog.DialogUtils;
import com.example.sinhvienapplication.screen.home.HomeActivity;
import com.example.sinhvienapplication.validate.UserValidate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class RegisterActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    ImageView mBackIv;
    TextView mSignUpBtn;
    TextView mSignInBtn;
    EditText mMailEdt, mUsernameEdt, mStudentCodeEdt, mClassEdt;
    EditText mPasswordEdt;

    FirebaseAuth mAuth;
    UserMethodFirebase mUserMethod;
    LinearLayout layoutClass;
    final UserMethodFirebase userMethodFirebase = new UserMethodFirebase();

    private void initView() {
        mBackIv = findViewById(R.id.back_iv);
        mSignInBtn = findViewById(R.id.sign_in_btn);
        mMailEdt = findViewById(R.id.mail_edt);
        mPasswordEdt = findViewById(R.id.password_edt);
        mSignUpBtn = findViewById(R.id.sign_up_btn);
        mUsernameEdt = findViewById(R.id.username_edt);
        mStudentCodeEdt = findViewById(R.id.id_edt);
        mClassEdt = findViewById(R.id.class_edt);
        layoutClass = findViewById(R.id.layoutClass);

        mBackIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        if(PrefManager.getTypeUser(getViewContext()).equals(Constant.Firebase.TYPE_STUDENT_COLLECTION)){
            layoutClass.setVisibility(View.VISIBLE);
        }else {
            layoutClass.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPrepareLayout() {
        super.onPrepareLayout();
        initView();

        mUserMethod = new UserMethodFirebase();
        mAuth = FirebaseAuth.getInstance();
        initView();

        mMailEdt.setText("a@gmail.com");
        mPasswordEdt.setText("123456");

        mSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = mMailEdt.getText().toString().trim();
                String password = mPasswordEdt.getText().toString().trim();
                String id = mStudentCodeEdt.getText().toString().trim();
                String username = mUsernameEdt.getText().toString().trim();
                String grade = mClassEdt.getText().toString().trim();

                if (TextUtils.isEmpty(mail)){
                    UserValidate.isEmptyEdt(getViewContext(), mMailEdt, "mail empty");
                }else if (TextUtils.isEmpty(password)){
                    UserValidate.isEmptyEdt(getViewContext(), mPasswordEdt, "password empty");
                }else if (TextUtils.isEmpty(id)){
                    UserValidate.isEmptyEdt(getViewContext(), mStudentCodeEdt, "id empty");
                }else if (TextUtils.isEmpty(username)){
                    UserValidate.isEmptyEdt(getViewContext(), mUsernameEdt, "username empty");
                }else {
                    if(PrefManager.getTypeUser(getViewContext()).equals(Constant.Firebase.TYPE_STUDENT_COLLECTION)){
                        if (TextUtils.isEmpty(grade)){
                            UserValidate.isEmptyEdt(getViewContext(), mClassEdt, "grade empty");
                        }else {
                            checkIdUser(mail, password, id, username, grade);
                        }
                    }else {
                        checkIdUser(mail, password, id, username, "");
                    }

                }

            }
        });
    }

    boolean isExits = true;
    private void checkIdUser(String mail, String password, String id, String username, String grade){
        userMethodFirebase.userCollectionRef(PrefManager.getTypeUser(getViewContext()))
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot snapshots) {
                        List<User> users = snapshots.toObjects(User.class);
                        for(User user : users){
                            if(user.getId().equals(id)){
                                Toast.makeText(RegisterActivity.this, "Id already exists ", Toast.LENGTH_SHORT).show();
                                isExits = false;
                                break;
                            }
                        }

                        if(isExits){
                            signUp(mail, password, id, username, "");
                        }
                    }
                });
    }

    private void signUp(String mail, String password, String id, String username, String grade) {
        DialogUtils.showProgressDialog(getViewContext(), getViewContext().getString(R.string.sign_up));
        mAuth.createUserWithEmailAndPassword(mail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                DialogUtils.dismissProgressDialog();
                if(task.isSuccessful()){
                    String uid = mAuth.getUid();
                    onSuccessSignUp(uid, mail, id, username, grade);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                DialogUtils.dismissProgressDialog();
                Toast.makeText(getViewContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onSuccessSignUp(String uid, String mail, String id, String username, String grade) {

        User user = new User(
                uid,
                id,
                username,
                Constant.PHONE,
                mail,
                grade,
                PrefManager.getTypeUser(getViewContext()),
                Constant.IMAGE
        );
        mUserMethod.registerUser(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getViewContext(), "Register Success", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getViewContext(), HomeActivity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getViewContext(), "Register fail", Toast.LENGTH_LONG).show();
            }
        });


    }

}
