package com.example.sinhvienapplication.firebase.topic;

import androidx.annotation.NonNull;

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

    /**   --------- ADMIN -----------    **/
    public Task<Void> uploadFileAdmin(@NonNull Topic topic) {
        return firebaseMethods.topicRef
                .document(topic.getUidStudent())
                .set(topic.toMap());
    }

    public Task<Void> updateFileAdmin(@NonNull Topic topic) {
        return firebaseMethods.topicRef
                .document(topic.getUidStudent())
                .update(topic.toMap());
    }

    public Task<Void> approveFileAdmin(@NonNull Topic topic) {
        return firebaseMethods.topicRef
                .document(topic.getUidStudent())
                .set(topic.toMap());
    }

    public Task<Void> deleteFileAdmin(@NonNull Topic topic) {
        return firebaseMethods.topicRef
                .document(topic.getUidStudent())
                .delete();
    }

    /** ------------------------------------------------- **/



    /**   --------- STUDENT -----------    **/

    public Task<Void> uploadFileStudent(@NonNull Topic topic) {
        return firebaseMethods.userRef
                .document(Constant.Firebase.TYPE_COLLECTION)
                .collection(Constant.Firebase.TYPE_STUDENT_COLLECTION)
                .document(topic.getUidStudent())
                .collection(Constant.Firebase.TOPIC_COLLECTION)
                .document(topic.getUidTeacher())
                .set(topic.toMap());
    }

    public Task<Void> updateFileStudent(@NonNull Topic topic) {
        return firebaseMethods.userRef
                .document(Constant.Firebase.TYPE_COLLECTION)
                .collection(Constant.Firebase.TYPE_STUDENT_COLLECTION)
                .document(topic.getUidStudent())
                .collection(Constant.Firebase.TOPIC_COLLECTION)
                .document(topic.getUidTeacher())
                .update(topic.toMap());
    }

    public Task<Void> approveFileStudent(@NonNull Topic topic) {
        return firebaseMethods.userRef
                .document(Constant.Firebase.TYPE_COLLECTION)
                .collection(Constant.Firebase.TYPE_STUDENT_COLLECTION)
                .document(topic.getUidStudent())
                .collection(Constant.Firebase.TOPIC_COLLECTION)
                .document(topic.getUidTeacher())
                .set(topic.toMap());
    }

    public Task<Void> markFileStudent(@NonNull Topic topic) {
        return firebaseMethods.userRef
                .document(Constant.Firebase.TYPE_COLLECTION)
                .collection(Constant.Firebase.TYPE_STUDENT_COLLECTION)
                .document(topic.getUidStudent())
                .collection(Constant.Firebase.TOPIC_COLLECTION)
                .document(topic.getUidTeacher())
                .update(topic.toMap());
    }

    public Task<Void> deleteFileStudent(@NonNull Topic topic) {
        return firebaseMethods.userRef
                .document(Constant.Firebase.TYPE_COLLECTION)
                .collection(Constant.Firebase.TYPE_STUDENT_COLLECTION)
                .document(topic.getUidStudent())
                .collection(Constant.Firebase.TOPIC_COLLECTION)
                .document(topic.getUidTeacher())
                .delete();
    }

    /** ------------------------------------------------- **/


    /**   --------- TEACHER -----------    **/

    public Task<Void> uploadFileTeacher(@NonNull Topic topic) {
        return firebaseMethods.userRef
                .document(Constant.Firebase.TYPE_COLLECTION)
                .collection(Constant.Firebase.TYPE_TEACHER_COLLECTION)
                .document(topic.getUidTeacher())
                .collection(Constant.Firebase.TOPIC_COLLECTION)
                .document(topic.getUidStudent())
                .set(topic.toMap());
    }

    public Task<Void> updateFileTeacher(@NonNull Topic topic) {
        return firebaseMethods.userRef
                .document(Constant.Firebase.TYPE_COLLECTION)
                .collection(Constant.Firebase.TYPE_TEACHER_COLLECTION)
                .document(topic.getUidTeacher())
                .collection(Constant.Firebase.TOPIC_COLLECTION)
                .document(topic.getUidStudent())
                .update(topic.toMap());
    }

    public Task<Void> approveFileTeacher(@NonNull Topic topic) {
        return firebaseMethods.userRef
                .document(Constant.Firebase.TYPE_COLLECTION)
                .collection(Constant.Firebase.TYPE_TEACHER_COLLECTION)
                .document(topic.getUidTeacher())
                .collection(Constant.Firebase.TOPIC_COLLECTION)
                .document(topic.getUidStudent())
                .set(topic.toMap());
    }

    public Task<Void> markFileTeacher(@NonNull Topic topic) {
        return firebaseMethods.userRef
                .document(Constant.Firebase.TYPE_COLLECTION)
                .collection(Constant.Firebase.TYPE_TEACHER_COLLECTION)
                .document(topic.getUidTeacher())
                .collection(Constant.Firebase.TOPIC_COLLECTION)
                .document(topic.getUidStudent())
                .update(topic.toMap());
    }

    public Task<Void> deleteFileTeacher(@NonNull Topic topic) {
        return firebaseMethods.userRef
                .document(Constant.Firebase.TYPE_COLLECTION)
                .collection(Constant.Firebase.TYPE_TEACHER_COLLECTION)
                .document(topic.getUidTeacher())
                .collection(Constant.Firebase.TOPIC_COLLECTION)
                .document(topic.getUidStudent())
                .delete();
    }

    /** ------------------------------------------------- **/


    /**   --------- OTHER -----------    **/

    public CollectionReference loadData(String type, String uid) {
        return firebaseMethods.userRef
                .document(Constant.Firebase.TYPE_COLLECTION)
                .collection(type)
                .document(uid)
                .collection(Constant.Firebase.TOPIC_COLLECTION);
    }

    public Query searchTopic(String value, String uid, String status) {
        return firebaseMethods.userRef
                .document(Constant.Firebase.TYPE_COLLECTION)
                .collection(status)
                .document(uid)
                .collection(Constant.Firebase.TOPIC_COLLECTION)
                .whereEqualTo("nameStudent", value);
    }

    /** ------------------------------------------------- **/
}
