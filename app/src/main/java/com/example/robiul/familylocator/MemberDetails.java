package com.example.robiul.familylocator;

public class MemberDetails {
    public String name;
    public String id;
    public String emil;
    public String password;

    public MemberDetails()
    {

    }

    public MemberDetails(String name, String id, String emil, String password) {
        this.name = name;
        this.id = id;
        this.emil = emil;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmil() {
        return emil;
    }

    public void setEmil(String emil) {
        this.emil = emil;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}