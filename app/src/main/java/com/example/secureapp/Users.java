package com.example.secureapp;

public class Users {
    private String name;
    private String uniEmail;
    private String uniID;
    private String faculty;

    public Users(){}

    public Users(String name, String uniID, String uniEmail, String faculty) {
        this.name = name;
        this.uniEmail = uniEmail;
        this.uniID = uniID;
        this.faculty = faculty;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUniEmail() {
        return uniEmail;
    }

    public void setUniEmail(String uniEmail) {
        this.uniEmail = uniEmail;
    }

    public String getUniID() {
        return uniID;
    }

    public void setUniID(String uniID) {
        this.uniID = uniID;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }
}
