package com.example.sinhvienapplication.screen.login;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.sinhvienapplication.R;
import com.example.sinhvienapplication.base.BaseActivity;
import com.example.sinhvienapplication.firebase.user.UserMethodFirebase;
import com.example.sinhvienapplication.model.User;
import com.example.sinhvienapplication.savedata.PrefManager;
import com.example.sinhvienapplication.screen.splash.SplashActivity;
import com.example.sinhvienapplication.utils.dialog.DialogUtils;
import com.example.sinhvienapplication.screen.home.HomeActivity;
import com.example.sinhvienapplication.screen.register.RegisterActivity;
import com.example.sinhvienapplication.validate.UserValidate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

public class LoginActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    TextView mSignUpBtn;
    TextView mSignInBtn;
    EditText mMailEdt;
    EditText mPasswordEdt;
    FirebaseAuth mAuth;
    final UserMethodFirebase userMethodFirebase = new UserMethodFirebase();

    @Override
    public void onPrepareLayout() {
        super.onPrepareLayout();
        mAuth = FirebaseAuth.getInstance();
        initView();

        mMailEdt.setText("a@gmail.com");
        mPasswordEdt.setText("123456");

        checkedLogin();

        mSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = mMailEdt.getText().toString().trim();
                String password = mPasswordEdt.getText().toString().trim();

                if (TextUtils.isEmpty(mail)){
                    Toast.makeText(LoginActivity.this, "mail empty", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(password)){
                    Toast.makeText(LoginActivity.this, "password empty", Toast.LENGTH_SHORT).show();
                }else {
                    signIn(mail, password);
                }
            }
        });

    }

    private void checkedLogin() {
        FirebaseUser user =FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            gotoHome();
        }
    }

    private void gotoHome(){
        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
        finish();
    }


    private void signIn(String mail, String password) {
        DialogUtils.showProgressDialog(getViewContext(), getViewContext().getString(R.string.sign_in));
        mAuth.signInWithEmailAndPassword(mail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                DialogUtils.dismissProgressDialog();
                if(task.isSuccessful()){
                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    userMethodFirebase.userRef(uid, PrefManager.getTypeUser(getViewContext()))
                            .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    User user = documentSnapshot.toObject(User.class);
                                    if(user == null){
                                        Toast.makeText(getViewContext(), "Login Failed", Toast.LENGTH_LONG).show();
                                        FirebaseAuth.getInstance().signOut();
                                    }else {
                                        gotoHome();
                                    }
                                }
                            });
                }else {
                    Toast.makeText(getViewContext(), "Login Failed", Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                DialogUtils.dismissProgressDialog();
                Toast.makeText(getViewContext(), "Failed "+e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initView() {
        mSignUpBtn = findViewById(R.id.sign_up_btn);
        mMailEdt = findViewById(R.id.mail_edt);
        mPasswordEdt = findViewById(R.id.password_edt);
        mSignInBtn = findViewById(R.id.sign_in_btn);

        mSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        findViewById(R.id.back_iv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SplashActivity.class));
                finish();
            }
        });
    }

}
