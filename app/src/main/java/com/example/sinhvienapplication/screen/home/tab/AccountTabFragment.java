package com.example.sinhvienapplication.screen.home.tab;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.sinhvienapplication.R;
import com.example.sinhvienapplication.base.BaseActivity;
import com.example.sinhvienapplication.base.BaseFragment;
import com.example.sinhvienapplication.screen.home.HomeActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountTabFragment extends BaseFragment<HomeActivity> {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tab_account;
    }

    EditText mEmailEdt;
    EditText mNameEdt ;
    EditText mAddressEdt ;
    TextView mBirthDayTv;
    CircleImageView mProfileIv;
    ImageView mEditImageBtn;
    private final int  cameraRequest = 1888;
    private final int  IMAGE_CHOOSE = 1000;
    private final int  PERMISSION_CODE = 1001;
    private final int REQUEST_CODE = 13;

    File mFile;
    @Override
    public void onPrepareLayout() {
        super.onPrepareLayout();

        initView();

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
                if(selectedImageUri!=null){
                    String imagePath= getRealPathFromURI(getViewContext(), selectedImageUri);
                    mProfileIv.setImageURI(data.getData());
                }
            }
        }

        if (requestCode == cameraRequest && resultCode == Activity.RESULT_OK) {
            Bitmap photo= (Bitmap) data.getExtras().get("data");
            mProfileIv.setImageBitmap(photo);
        }
    }

    public static String getRealPathFromURI(BaseActivity context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] projection = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, projection, null, null, null);

            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
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

    private void initView() {
        mEmailEdt = getRootView().findViewById(R.id.email_edt);
        mNameEdt = getRootView().findViewById(R.id.name_edt);
        mAddressEdt = getRootView().findViewById(R.id.address_edt);
        mBirthDayTv = getRootView().findViewById(R.id.birthday_tv);
        mProfileIv = getRootView().findViewById(R.id.profile_iv);
        mEditImageBtn = getRootView().findViewById(R.id.edit_image_btn);
    }

    private void chooseImageGallery() {
        Intent intent = new  Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE_CHOOSE);
    }
}
