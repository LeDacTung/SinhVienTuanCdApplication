package com.example.sinhvienapplication.screen.uploadfile;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.sinhvienapplication.R;
import com.example.sinhvienapplication.base.BaseActivity;
import com.example.sinhvienapplication.utils.file.FileUtils;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.UUID;

public class UploadFileActivity extends BaseActivity {
    private static final String TAG = UploadFileActivity.class.getName();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_upload_file;
    }

    private static final int FILE_SELECT_CODE = 1;

    LinearLayout mLayoutUpload, mLayoutChooseFile;
    TextView mNameFileTv, mSizeTv, mUploadFileBtn;
    View mLayoutFile;
    ImageView mCloseTv, mBackIv;
    Uri mUri;

    private void initView() {
        mLayoutUpload = findViewById(R.id.layoutUpload);
        mNameFileTv = findViewById(R.id.name_file_tv);
        mLayoutChooseFile = findViewById(R.id.layoutChooseFile);
        mSizeTv = findViewById(R.id.size_tv);
        mLayoutFile = findViewById(R.id.layoutFile);
        mCloseTv = findViewById(R.id.close_iv);
        mUploadFileBtn = findViewById(R.id.upload_btn);
        mBackIv = findViewById(R.id.back_iv);
    }

    @Override
    public void onPrepareLayout() {
        super.onPrepareLayout();
        initView();

        mLayoutUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });

        mLayoutChooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });

        mCloseTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayoutUpload.setVisibility(View.VISIBLE);
                mLayoutFile.setVisibility(View.GONE);
                mUri = null;
            }
        });

        mUploadFileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mUri != null) {
                    uploadFileToStorage(mUri);
                }
            }
        });

        mBackIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    mUri = uri;
                    File file = new File(uri.getPath());
                    mLayoutUpload.setVisibility(View.GONE);
                    mLayoutFile.setVisibility(View.VISIBLE);
                    mNameFileTv.setText(FileUtils.getName(getViewContext(), mUri));
                    mSizeTv.setText(FileUtils.getFolderSizeLabel(getViewContext(), mUri));
                    uploadFileToStorage(mUri);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    void uploadFileToStorage(Uri data){
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("file");
        final StorageReference filepath = storageReference.child(UUID.randomUUID()+".docx");
        Toast.makeText(this, filepath.getName(), Toast.LENGTH_SHORT).show();

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
                    // After uploading is done it progress
                    // dialog box will be dismissed
                    Uri uri = task.getResult();
                    String myurl;
                    myurl = uri.toString();
                } else {
                    Toast.makeText(UploadFileActivity.this, "UploadedFailed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        String[] mimetypes = {"application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/msword"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }


    }


}
