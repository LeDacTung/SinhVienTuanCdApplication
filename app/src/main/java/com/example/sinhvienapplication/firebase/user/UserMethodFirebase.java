package com.example.sinhvienapplication.firebase.user;

import com.example.sinhvienapplication.constant.Constant;
import com.example.sinhvienapplication.firebase.FirebaseMethods;
import com.example.sinhvienapplication.model.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;

public class UserMethodFirebase {

    final FirebaseMethods firebaseMethods = new FirebaseMethods();

    public CollectionReference userCollectionRef(String type){
        return firebaseMethods.userRef
                .document(Constant.Firebase.TYPE_COLLECTION)
                .collection(type);
    }

    public DocumentReference userRef(String uid, String type){
        return firebaseMethods.userRef
                .document(Constant.Firebase.TYPE_COLLECTION)
                .collection(type)
                .document(uid);
    }

    public Task<Void> registerUser(User user) {
        return firebaseMethods.userRef
                .document(Constant.Firebase.TYPE_COLLECTION)
                .collection(user.getType())
                .document(user.getUid())
                .set(user.toMap());
    }

    public Task<Void> updateUser(User user) {
        return firebaseMethods.userRef
                .document(Constant.Firebase.TYPE_COLLECTION)
                .collection(user.getType())
                .document(user.getUid())
                .update(user.toMap());
    }

    public CollectionReference teacherRef(){
        return firebaseMethods.userRef
                .document(Constant.Firebase.TYPE_COLLECTION)
                .collection(Constant.Firebase.TYPE_TEACHER_COLLECTION);
    }

    public CollectionReference studentRef(){
        return firebaseMethods.userRef
                .document(Constant.Firebase.TYPE_COLLECTION)
                .collection(Constant.Firebase.TYPE_TEACHER_COLLECTION);
    }
}
