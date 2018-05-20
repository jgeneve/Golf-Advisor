package com.golf.dss.golf_project.Classes;

public class User {
    private String firstname;
    private String age;
    private String gender;
    private String height;
    private String weight;
    private String level;
    private String style;
    private String frequency;
    private String expTime;

    public User(String firstname, String age, String gender, String height, String weight, String level, String style, String frequency, String expTime) {
        this.firstname = firstname;
        this.age = age;
        this.gender = gender;
        this.height = height;
        this.weight = weight;
        this.level = level;
        this.style = style;
        this.frequency = frequency;
        this.expTime = expTime;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getExpTime() {
        return expTime;
    }

    public void setExpTime(String expTime) {
        this.expTime = expTime;
    }
}
