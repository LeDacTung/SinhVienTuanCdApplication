package com.example.sinhvienapplication.screen.uploadfile;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.sinhvienapplication.R;
import com.example.sinhvienapplication.adapter.SpinnerAdapter;
import com.example.sinhvienapplication.base.BaseActivity;
import com.example.sinhvienapplication.constant.Constant;
import com.example.sinhvienapplication.constant.Method;
import com.example.sinhvienapplication.firebase.topic.TopicMethodFirebase;
import com.example.sinhvienapplication.firebase.user.UserMethodFirebase;
import com.example.sinhvienapplication.model.Person;
import com.example.sinhvienapplication.model.Topic;
import com.example.sinhvienapplication.model.User;
import com.example.sinhvienapplication.savedata.PrefManager;
import com.example.sinhvienapplication.utils.dialog.DialogUtils;
import com.example.sinhvienapplication.utils.file.FileUtils;
import com.example.sinhvienapplication.utils.image.ImageUtils;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.UUID;

public class UploadFileActivity extends BaseActivity {
    private static final String TAG = UploadFileActivity.class.getName();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_upload_file;
    }

    private static final int FILE_SELECT_CODE = 1;

    LinearLayout mLayoutUpload, mLayoutChooseFile;
    TextView mNameFileTv, mSizeTv, mUploadFileBtn, mHeaderTv;
    EditText mTitleEdt, mDescriptionEdt;
    View mLayoutFile;
    ImageView mCloseTv, mBackIv;
    Uri mUri;
    Spinner mSpinner;
    ArrayList<Person> mPersons = new ArrayList<>();
    SpinnerAdapter mSpinnerAdapter;
    UserMethodFirebase userMethodFirebase = new UserMethodFirebase();
    TopicMethodFirebase topicMethodFirebase = new TopicMethodFirebase();
    String mUidTeacher = "", mTypeFile = "", mSizeFile= "", mNameFile = "";
    User mUser;
    Topic mTopic;
    static String TYPE_UPLOAD = "Upload";
    static String TYPE_EDIT = "Edit";
    String TYPE = TYPE_UPLOAD;

    private void initView() {
        mLayoutUpload = findViewById(R.id.layoutUpload);
        mNameFileTv = findViewById(R.id.name_file_tv);
        mLayoutChooseFile = findViewById(R.id.layoutChooseFile);
        mSizeTv = findViewById(R.id.size_tv);
        mLayoutFile = findViewById(R.id.layoutFile);
        mCloseTv = findViewById(R.id.close_iv);
        mUploadFileBtn = findViewById(R.id.upload_btn);
        mBackIv = findViewById(R.id.back_iv);
        mSpinner = findViewById(R.id.spinner);
        mTitleEdt = findViewById(R.id.title_edt);
        mDescriptionEdt = findViewById(R.id.description_edt);
        mHeaderTv = findViewById(R.id.header_tv);
    }

    @Override
    public void onPrepareLayout() {
        super.onPrepareLayout();

        mTopic = (Topic) getIntent().getSerializableExtra(Constant.Intent.TOPIC_UPLOAD_FILE);
        initView();
        loadDataTeacher();
        loadDataUser();
        setUpSpinner();

        if(mTopic != null){
            setData(mTopic);
        }else {
            TYPE = TYPE_UPLOAD;
        }

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
                    uploadFile();
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

    private void setData(Topic mTopic) {
        TYPE = TYPE_EDIT;
        mLayoutUpload.setVisibility(View.GONE);
        mLayoutFile.setVisibility(View.VISIBLE);

        mNameFileTv.setText(mTopic.getNameFile());
        mSizeTv.setText(mTopic.getSizeFile());
        mTitleEdt.setText(mTopic.getTitle());
        mDescriptionEdt.setText(mTopic.getDescription());
        mHeaderTv.setText("Edit file");
    }

    private void loadDataUser() {
        userMethodFirebase.userRef(FirebaseAuth.getInstance().getUid(), PrefManager.getTypeUser(getViewContext()))
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        mUser = documentSnapshot.toObject(User.class);
                    }
                });
    }

    private void uploadFile() {
        uploadFileToStorage(mUri);
    }

    private void loadDataTeacher() {
        userMethodFirebase.teacherRef().get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot snapshots) {
                ArrayList<User> users = (ArrayList<User>) snapshots.toObjects(User.class);
                for(User user: users){
                    Person person = new Person(user.getUid(), user.getName(), user.getImage());
                    mSpinnerAdapter.addPersons(person);
                }
            }
        });
    }

    private void setUpSpinner() {
        mSpinnerAdapter = new SpinnerAdapter(getViewContext(), mPersons);

        mSpinner.setAdapter(mSpinnerAdapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mUidTeacher = mPersons.get(i).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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
                    mTypeFile = getfileExtension(mUri, getViewContext());
                    mNameFile = FileUtils.getName(getViewContext(), mUri);
                    mSizeFile = FileUtils.getFolderSizeLabel(getViewContext(), mUri);
                    mNameFileTv.setText(mNameFile);
                    mSizeTv.setText(mSizeFile);

                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    void uploadFileToStorage(Uri data){

        DialogUtils.showProgressDialog(getViewContext(), "Upload file...");

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("file");
        final StorageReference filepath = storageReference.child(UUID.randomUUID()+"."+mTypeFile);
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
                    String fileUrl = uri.toString();
                    Topic topic = new Topic(
                            mNameFile,
                            mTitleEdt.getText().toString(),
                            mDescriptionEdt.getText().toString(),
                            fileUrl,
                            mTypeFile,
                            Method.getTimeCurrent(),
                            FirebaseAuth.getInstance().getUid(),
                            mUser.getName(),
                            mUidTeacher,
                            Constant.FILE.STATUS_NOT_APPROVE,
                            mSizeFile,
                            mUser.getGrade(),
                            mUser.getImage()
                    );
                    topicMethodFirebase.uploadFileStudent(topic);
                    topicMethodFirebase.uploadFileTeacher(topic);
                    topicMethodFirebase.uploadFile(topic).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            DialogUtils.dismissProgressDialog();
                            Toast.makeText(UploadFileActivity.this, "Success", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            DialogUtils.dismissProgressDialog();
                            onBackPressed();
                            Toast.makeText(UploadFileActivity.this, "Fail "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    DialogUtils.dismissProgressDialog();
                    Toast.makeText(UploadFileActivity.this, "UploadedFailed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String getfileExtension(Uri uri, Context context)
    {
        String extension;
        ContentResolver contentResolver = context.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        extension= mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
        return extension;
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
