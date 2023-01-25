package com.example.sinhvienapplication.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Handler;

public class User implements Serializable {
    String uid, id, name, phone, image,
            gmail, grade, type;

    public User() {
    }

    public String getImage() {
        return image;
    }

    public String getUid() {
        return uid;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getGmail() {
        return gmail;
    }

    public String getGrade() {
        return grade;
    }

    public String getType() {
        return type;
    }

    public User(String uid, String id, String name, String phone, String gmail, String grade, String type, String image) {
        this.uid = uid;
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.gmail = gmail;
        this.grade = grade;
        this.type = type;
        this.image = image;
    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("uid",uid);
        map.put("id",id);
        map.put("name",name);
        map.put("phone",phone);
        map.put("image",image);
        map.put("gmail",gmail);
        map.put("grade",grade);
        map.put("type",type);
        return map;
    }

    public User fromMap(HashMap<String, Object> map){
        User user = new User(
                map.get("uid").toString(),
                map.get("id").toString(),
                map.get("name").toString(),
                map.get("phone").toString(),
                map.get("gmail").toString(),
                map.get("grade").toString(),
                map.get("type").toString(),
                map.get("image").toString()
        );
        return user;
    }
}
