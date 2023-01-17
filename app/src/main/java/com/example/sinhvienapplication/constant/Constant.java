package com.example.sinhvienapplication.constant;

public interface Constant {
    String PHONE = "+84";
    String IMAGE = "https://cdn-icons-png.flaticon.com/512/1946/1946429.png";

    interface Intent{
        String POSITION_TOPIC_DETAIL = "position_topic_detail";
        String TOPIC_DETAIL = "topic_detail";
        String EDIT_UPLOAD_FILE = "edit_upload_file";
        String TOPIC_UPLOAD_FILE = "topic_upload_file";
    }

    interface Firebase {
        String NAME_APP = "Student_Topic";
        String USER_COLLECTION = "User";
        String TOPIC_COLLECTION = "Topic";
        String TYPE_TEACHER_COLLECTION = "Teacher";
        String TYPE_STUDENT_COLLECTION = "Student";
        String IMAGE_CHILD = "image";
        String AVATAR_CHILD = "avatar";
        String FILE_CHILD = "file";
        String TYPE_COLLECTION = "TypeUser";
    }

    interface FILE {
        String STATUS_APPROVE = "approve";
        String STATUS_NOT_APPROVE = "not_approve";
        String PDF = "pdf";
        String DOCX = "docx";
        String DOC = "doc";
    }

    interface NUMBER_MARK {
        String MARK_DEFAULT = "";
    }
}
