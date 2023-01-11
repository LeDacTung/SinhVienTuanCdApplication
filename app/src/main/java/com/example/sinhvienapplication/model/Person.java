package com.example.sinhvienapplication.model;

public class Person {
    String name, id, image;

    public Person(String id, String name, String image) {
        this.name = name;
        this.id = id;
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
}
