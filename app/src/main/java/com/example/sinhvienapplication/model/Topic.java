package com.example.sinhvienapplication.model;

import com.example.sinhvienapplication.model.User;

import java.io.Serializable;
import java.util.HashMap;

public class Topic implements Serializable {
    String nameFile, description, title, fileUrl,
            typeFile, time;
    String uidStudent, nameStudent, gradeStudent, imageStudent;
    String uidTeacher;
    String sizeFile;
    String status;
    String mark;
    User userStudent, userTeacher;

    public User getUserTeacher() {
        return userTeacher;
    }

    public User getUserStudent() {
        return userStudent;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getGradeStudent() {
        return gradeStudent;
    }

    public String getImageStudent() {
        return imageStudent;
    }

    public void setImageStudent(String imageStudent) {
        this.imageStudent = imageStudent;
    }

    public void setGradeStudent(String gradeStudent) {
        this.gradeStudent = gradeStudent;
    }

    public String getTitle() {
        return title;
    }

    public String getNameFile() {
        return nameFile;
    }

    public void setNameFile(String nameFile) {
        this.nameFile = nameFile;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getTypeFile() {
        return typeFile;
    }

    public void setTypeFile(String typeFile) {
        this.typeFile = typeFile;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUidStudent() {
        return uidStudent;
    }

    public void setUidStudent(String uidStudent) {
        this.uidStudent = uidStudent;
    }

    public String getNameStudent() {
        return nameStudent;
    }

    public void setNameStudent(String nameStudent) {
        this.nameStudent = nameStudent;
    }

    public String getUidTeacher() {
        return uidTeacher;
    }

    public void setUidTeacher(String uidTeacher) {
        this.uidTeacher = uidTeacher;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSizeFile() {
        return sizeFile;
    }

    public void setSizeFile(String sizeFile) {
        this.sizeFile = sizeFile;
    }

    public Topic() {
    }

    public Topic(String nameFile,String title, String description, String fileUrl, String typeFile, String time,
                 String uidStudent, String nameStudent, String uidTeacher,
                 String status, String sizeFile, String gradeStudent, String imageStudent, String mark,
                 User userStudent,
                 User userTeacher) {
        this.nameFile = nameFile;
        this.title = title;
        this.description = description;
        this.fileUrl = fileUrl;
        this.typeFile = typeFile;
        this.time = time;
        this.uidStudent = uidStudent;
        this.nameStudent = nameStudent;
        this.gradeStudent = gradeStudent;
        this.imageStudent = imageStudent;
        this.uidTeacher = uidTeacher;
        this.status = status;
        this.sizeFile = sizeFile;
        this.mark = mark;
        this.userStudent = userStudent;
        this.userTeacher = userTeacher;
    }

    public HashMap<String, Object> toMap(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("nameFile", nameFile);
        map.put("description", description);
        map.put("fileUrl", fileUrl);
        map.put("sizeFile", sizeFile);
        map.put("typeFile", typeFile);
        map.put("time", time);
        map.put("uidStudent", uidStudent);
        map.put("nameStudent", nameStudent);
        map.put("imageStudent", imageStudent);
        map.put("gradeStudent", gradeStudent);
        map.put("uidTeacher", uidTeacher);
        map.put("status", status);
        map.put("title", title);
        map.put("mark", mark);
        map.put("userStudent", userStudent.toMap());
        map.put("userTeacher", userTeacher.toMap());
        return map;
    }
}
