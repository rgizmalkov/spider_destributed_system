package com.gmail.rgizmalkov.edu.projects.spider_aggregation.tests;

public class Cat {
    String speech;
    String name;
    int age;

    public Cat() {
    }

    public Cat(String speech, String name, int age) {
        this.speech = speech;
        this.name = name;
        this.age = age;
    }

    public void setSpeech(String speech) {
        this.speech = speech;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSpeech() {
        return speech;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
}
