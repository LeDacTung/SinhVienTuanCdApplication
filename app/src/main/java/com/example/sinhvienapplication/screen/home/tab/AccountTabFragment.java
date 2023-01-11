package com.example.sinhvienapplication.screen.home.tab;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.sinhvienapplication.R;
import com.example.sinhvienapplication.base.BaseActivity;
import com.example.sinhvienapplication.base.BaseFragment;
import com.example.sinhvienapplication.constant.Constant;
import com.example.sinhvienapplication.firebase.FirebaseMethods;
import com.example.sinhvienapplication.firebase.user.UserMethodFirebase;
import com.example.sinhvienapplication.model.Topic;
import com.example.sinhvienapplication.model.User;
import com.example.sinhvienapplication.savedata.PrefManager;
import com.example.sinhvienapplication.screen.home.HomeActivity;
import com.example.sinhvienapplication.screen.login.LoginActivity;
import com.example.sinhvienapplication.screen.splash.SplashActivity;
import com.example.sinhvienapplication.screen.uploadfile.UploadFileActivity;
import com.example.sinhvienapplication.utils.dialog.DialogUtils;
import com.example.sinhvienapplication.utils.dialog.DialogYesNo;
import com.example.sinhvienapplication.utils.image.ImageUtils;
import com.example.sinhvienapplication.validate.UserValidate;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountTabFragment extends BaseFragment<HomeActivity> {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tab_account;
    }

    EditText mEmailEdt;
    EditText mNameEdt ;
    EditText mPhoneEdt;
    TextView mClassTv;
    CircleImageView mProfileIv;
    ImageView mEditImageBtn, mLogoutIv;
    TextView mSaveTv;
    LinearLayout mLayoutClass, mLayoutData;
    View mLayoutLoad;
    Uri mUri;

    private final int  cameraRequest = 1888;
    private final int  IMAGE_CHOOSE = 1000;
    private final int  PERMISSION_CODE = 1001;
    private final int REQUEST_CODE = 13;

    File mFile;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseFirestore mFireStore;
    UserMethodFirebase userMethodFirebase;

    String name= "", email= "", phone= "", image= "", id= "", gradle= "";
    @Override
    public void onPrepareLayout() {
        super.onPrepareLayout();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        userMethodFirebase = new UserMethodFirebase();
        initView();

        mLayoutData.setVisibility(View.GONE);
        mLayoutLoad.setVisibility(View.VISIBLE);

        loadData();

        mProfileIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomDialogChooseImage();
            }
        });

        mEditImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomDialogChooseImage();
            }
        });

        mSaveTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(mNameEdt.getText().toString())){
                    UserValidate.isEmptyEdt(getViewContext(), mNameEdt, "name empty");
                }else if(TextUtils.isEmpty(mEmailEdt.getText().toString())){
                    UserValidate.isEmptyEdt(getViewContext(), mEmailEdt, "email empty");
                }else if(TextUtils.isEmpty(mPhoneEdt.getText().toString())){
                    UserValidate.isEmptyEdt(getViewContext(), mPhoneEdt, "phone empty");
                }else if(mPhoneEdt.getText().toString().length() > 13 || mPhoneEdt.getText().toString().length() < 13 ){
                    UserValidate.isEmptyEdt(getViewContext(), mPhoneEdt, "not phone");
                }else {
                    if(mUri != null){
                        uploadImageToStorage(mUri, mUser.getUid());
                    }else {
                        updateUser(image);
                    }
                    mSaveTv.setEnabled(false);
                    return;
                }
            }
        });

        mLogoutIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogYesNo dialogYesNo = new DialogYesNo(getViewContext(),
                        "Log out!",
                        "Do you want log out",
                        new DialogYesNo.OnClickListener() {
                            @Override
                            public void onYes() {
                                FirebaseAuth.getInstance().signOut();
                                gotoLogin();
                            }
                        });
                dialogYesNo.show();
            }
        });

    }

    private void gotoLogin() {
        Intent intent = new Intent(getViewContext(), SplashActivity.class);
        startActivity(intent);
        getViewContext().finish();
        PrefManager.clearData(getViewContext());
    }

    private void updateUser(String image) {
        name = mNameEdt.getText().toString().trim();
        phone = mPhoneEdt.getText().toString().trim();
        email = mEmailEdt.getText().toString().trim();

        DialogUtils.showProgressDialog(getBaseActivity(), "Updating...");

        User user = new User(
                mUser.getUid(),
                id,
                name,
                phone,
                email,
                gradle,
                PrefManager.getTypeUser(getViewContext()),
                image
        );

        userMethodFirebase.updateUser(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                DialogUtils.dismissProgressDialog();
                loadData();
                Toast.makeText(getViewContext(), "Update success", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                DialogUtils.dismissProgressDialog();
                Toast.makeText(getViewContext(), "Update fail", Toast.LENGTH_SHORT).show();
                mSaveTv.setEnabled(true);
            }
        });
    }

    public class TextChange implements TextWatcher{
        String value = "";
        public TextChange(String value) {
            this.value = value;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String s = charSequence.toString();
            if(s.equals(value)){
                setEnabledSaveBtn(true);
            }else {
                setEnabledSaveBtn(false);
            }
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String s = charSequence.toString();
            if(s.equals(value)){
                setEnabledSaveBtn(true);
            }else {
                setEnabledSaveBtn(false);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }

    private void setEnabledSaveBtn(boolean isChange){
        if(isChange){
            mSaveTv.setBackground(getViewContext().getDrawable(R.drawable.bg_btn_disable));
            mSaveTv.setEnabled(false);
        }else {
            mSaveTv.setBackground(getViewContext().getDrawable(R.drawable.bg_btn_green));
            mSaveTv.setEnabled(true);
        }
    }

    private void checkDefaultData(){
        if(mPhoneEdt.getText().toString().equals(phone)){
            setEnabledSaveBtn(true);
        }else if(mEmailEdt.getText().toString().equals(email)) {
            setEnabledSaveBtn(true);
        }else if(mNameEdt.getText().toString().equals(name)) {
            setEnabledSaveBtn(true);
        }
    }

    private void checkChangeData() {
        mNameEdt.addTextChangedListener(new TextChange(name));
        mEmailEdt.addTextChangedListener(new TextChange(email));
        mPhoneEdt.addTextChangedListener(new TextChange(phone));
    }

    private void loadData() {
        userMethodFirebase.userRef(mUser.getUid(), PrefManager.getTypeUser(getViewContext()))
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        mLayoutData.setVisibility(View.VISIBLE);
                        mLayoutLoad.setVisibility(View.GONE);

                        User user = documentSnapshot.toObject(User.class);
                        name = user.getName();
                        email = user.getGmail();
                        phone = user.getPhone();
                        image = user.getImage();
                        id = user.getId();
                        gradle = user.getGrade();
                        image = user.getImage();

                        mEmailEdt.setText(email);
                        mNameEdt.setText(name);
                        mPhoneEdt.setText(phone);
                        ImageUtils.loadImage(getViewContext(), mProfileIv, image);

                        if(user.getType().equals(Constant.Firebase.TYPE_STUDENT_COLLECTION)){
                            mLayoutClass.setVisibility(View.VISIBLE);
                            mClassTv.setText(user.getGrade());
                        }else {
                            mLayoutClass.setVisibility(View.GONE);
                        }

                        checkChangeData();
                        checkDefaultData();
                    }
                });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSION_CODE:
                if (grantResults != null && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    chooseImageGallery();
                }else{
                    Toast.makeText(getBaseActivity(),"Permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==IMAGE_CHOOSE){
            if(resultCode == Activity.RESULT_OK && data!=null) {
                Uri selectedImageUri = data.getData();
                mUri = selectedImageUri;
                if(selectedImageUri!=null){
                    mProfileIv.setImageURI(data.getData());
                    setEnabledSaveBtn(false);
                }
            }
        }

        if (requestCode == cameraRequest && resultCode == Activity.RESULT_OK) {
            Bitmap photo= (Bitmap) data.getExtras().get("data");
            mProfileIv.setImageBitmap(photo);
            Uri uri = getImageUri(getViewContext(), photo);
            mUri = uri;
            setEnabledSaveBtn(false);
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, ""+UUID.randomUUID(), null);
        return Uri.parse(path);
    }

    private void showBottomDialogChooseImage() {
        BottomSheetDialog bottomDialog = new BottomSheetDialog(getBaseActivity(), R.style.BottomSheetDialogTheme);
        View view = LayoutInflater.from(getBaseActivity()).inflate(R.layout.bottom_sheet_camare_gallary, null);
        bottomDialog.setContentView(view);

        TextView mCameraTv  = view.findViewById(R.id.camera_tv);
        TextView mGalleryTv = view.findViewById(R.id.gallery_tv);

        String[] mPermissions = {Manifest.permission.CAMERA};

        mCameraTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getBaseActivity(), Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED)
                    ActivityCompat.requestPermissions(getBaseActivity(), mPermissions, cameraRequest);
                else {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, cameraRequest);
                }
                bottomDialog.dismiss();
            }
        });

        mGalleryTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (ContextCompat.checkSelfPermission(getBaseActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_DENIED){
                        requestPermissions(mPermissions, PERMISSION_CODE);
                    } else{
                        chooseImageGallery();
                    }
                }else{
                    chooseImageGallery();

                }

                bottomDialog.dismiss();
            }
        });
        bottomDialog.show();
    }

    void uploadImageToStorage(Uri data, String uid){
        StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                .child(Constant.Firebase.IMAGE_CHILD)
                .child(Constant.Firebase.AVATAR_CHILD);
        final StorageReference filepath = storageReference.child(uid);

        filepath.putFile(data).continueWithTask(new Continuation() {
            @Override
            public Object then(@NonNull Task task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return filepath.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri uri = task.getResult();
                    String myurl = uri.toString();
                    updateUser(myurl);
                } else {
                    Toast.makeText(getViewContext(), "UploadedFailed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initView() {
        mEmailEdt = getRootView().findViewById(R.id.email_edt);
        mNameEdt = getRootView().findViewById(R.id.name_edt);
        mPhoneEdt = getRootView().findViewById(R.id.phone_edt);
        mClassTv = getRootView().findViewById(R.id.class_tv);
        mProfileIv = getRootView().findViewById(R.id.profile_iv);
        mEditImageBtn = getRootView().findViewById(R.id.edit_image_btn);
        mSaveTv = getRootView().findViewById(R.id.save_tv);
        mLayoutClass =getRootView().findViewById(R.id.layoutClass);
        mLayoutLoad = getRootView().findViewById(R.id.layoutLoad);
        mLayoutData = getRootView().findViewById(R.id.layoutData);
        mLogoutIv = getRootView().findViewById(R.id.logout_iv);
    }

    private void chooseImageGallery() {
        Intent intent = new  Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE_CHOOSE);
    }
}
