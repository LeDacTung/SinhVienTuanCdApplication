package com.example.sinhvienapplication.screen.register;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.sinhvienapplication.R;
import com.example.sinhvienapplication.base.BaseActivity;
import com.example.sinhvienapplication.utils.dialog.DialogUtils;
import com.example.sinhvienapplication.screen.home.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    ImageView mBackIv;
    TextView mSignUpBtn;
    TextView mSignInBtn;
    EditText mMailEdt;
    EditText mPasswordEdt;
    FirebaseAuth mAuth;

    @Override
    public void onPrepareLayout() {
        super.onPrepareLayout();
        initView();

        mAuth = FirebaseAuth.getInstance();
        initView();

        mMailEdt.setText("a@gmail.com");
        mPasswordEdt.setText("123456");

        mSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = mMailEdt.getText().toString().trim();
                String password = mPasswordEdt.getText().toString().trim();

                Toast.makeText(RegisterActivity.this, "AAAAA", Toast.LENGTH_SHORT).show();

                if (TextUtils.isEmpty(mail)){
                    Toast.makeText(getViewContext(), "mail empty", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(password)){
                    Toast.makeText(getViewContext(), "password empty", Toast.LENGTH_SHORT).show();
                }else {
                    signUp(mail, password);
                }

            }
        });
    }

    private void signUp(String mail, String password) {
        DialogUtils.showProgressDialog(getViewContext(), getViewContext().getString(R.string.sign_up));
        mAuth.createUserWithEmailAndPassword(mail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                DialogUtils.dismissProgressDialog();
                if(task.isSuccessful()){
                    startActivity(new Intent(getViewContext(), HomeActivity.class));
                    finish();
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
        mBackIv = findViewById(R.id.back_iv);
        mSignInBtn = findViewById(R.id.sign_in_btn);
        mMailEdt = findViewById(R.id.mail_edt);
        mPasswordEdt = findViewById(R.id.password_edt);
        mSignUpBtn = findViewById(R.id.sign_up_btn);

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
    }
}
