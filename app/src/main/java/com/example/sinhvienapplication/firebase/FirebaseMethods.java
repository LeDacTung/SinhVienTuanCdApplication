package com.example.sinhvienapplication.firebase;

import com.example.sinhvienapplication.constant.Constant;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseMethods {
    static final FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();

    public final CollectionReference userRef = mFirestore.collection(Constant.Firebase.USER_COLLECTION);

    public final CollectionReference topicRef = mFirestore.collection(Constant.Firebase.TOPIC_COLLECTION);

}
