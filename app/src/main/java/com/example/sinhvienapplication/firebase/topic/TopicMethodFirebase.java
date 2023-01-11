package com.example.sinhvienapplication.firebase.topic;

import com.example.sinhvienapplication.constant.Constant;
import com.example.sinhvienapplication.firebase.FirebaseMethods;
import com.example.sinhvienapplication.model.Topic;
import com.example.sinhvienapplication.model.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;

public class TopicMethodFirebase {
    final FirebaseMethods firebaseMethods = new FirebaseMethods();

    public Task<Void> uploadFile(Topic topic) {
        return firebaseMethods.topicRef
                .document(topic.getUidTeacher())
                .collection(topic.getStatus())
                .document(topic.getUidStudent())
                .set(topic.toMap());
    }

    public CollectionReference loadData(String type, String uid) {
        return firebaseMethods.userRef
                .document(Constant.Firebase.TYPE_COLLECTION)
                .collection(type)
                .document(uid)
                .collection(Constant.Firebase.TOPIC_COLLECTION);
    }

    public Task<Void> uploadFileStudent(Topic topic) {
        return firebaseMethods.userRef
                .document(Constant.Firebase.TYPE_COLLECTION)
                .collection(Constant.Firebase.TYPE_STUDENT_COLLECTION)
                .document(topic.getUidStudent())
                .collection(Constant.Firebase.TOPIC_COLLECTION)
                .document(topic.getUidTeacher())
                .set(topic.toMap());
    }

    public Task<Void> uploadFileTeacher(Topic topic) {
        return firebaseMethods.userRef
                .document(Constant.Firebase.TYPE_COLLECTION)
                .collection(Constant.Firebase.TYPE_TEACHER_COLLECTION)
                .document(topic.getUidTeacher())
                .collection(Constant.Firebase.TOPIC_COLLECTION)
                .document(topic.getUidStudent())
                .set(topic.toMap());
    }

    public Task<Void> approveFile(Topic topic) {
        return firebaseMethods.topicRef
                .document(topic.getUidTeacher())
                .collection(topic.getStatus())
                .document(topic.getUidStudent())
                .set(topic.toMap());
    }

    public Task<Void> approveFileTeacher(Topic topic) {
        return firebaseMethods.userRef
                .document(Constant.Firebase.TYPE_COLLECTION)
                .collection(Constant.Firebase.TYPE_TEACHER_COLLECTION)
                .document(topic.getUidTeacher())
                .collection(Constant.Firebase.TOPIC_COLLECTION)
                .document(topic.getUidStudent())
                .set(topic.toMap());
    }

    public Task<Void> approveFileStudent(Topic topic) {
        return firebaseMethods.userRef
                .document(Constant.Firebase.TYPE_COLLECTION)
                .collection(Constant.Firebase.TYPE_STUDENT_COLLECTION)
                .document(topic.getUidStudent())
                .collection(Constant.Firebase.TOPIC_COLLECTION)
                .document(topic.getUidTeacher())
                .set(topic.toMap());
    }

    public Task<Void> updateFile(Topic topic) {
        return firebaseMethods.topicRef
                .document(topic.getUidTeacher())
                .collection(topic.getStatus())
                .document(topic.getUidStudent())
                .update(topic.toMap());
    }

    public Task<Void> deleteFile(Topic topic) {
        return firebaseMethods.topicRef
                .document(topic.getUidTeacher())
                .collection(topic.getStatus())
                .document(topic.getUidStudent())
                .delete();
    }

    public Query searchTopic(String value, String uid, String status) {
        return firebaseMethods.userRef
                .document(Constant.Firebase.TYPE_COLLECTION)
                .collection(status)
                .document(uid)
                .collection(Constant.Firebase.TOPIC_COLLECTION)
                .whereEqualTo("nameStudent", value);
    }
}
