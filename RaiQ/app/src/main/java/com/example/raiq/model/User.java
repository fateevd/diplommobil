package com.example.raiq.model;

public class User {

    private String Email;
    private String Famyli;
    private String Name;

    public User() {
    }

    public User(String email, String famyli, String name) {
        Email = email;
        Famyli = famyli;
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getFamyli() {
        return Famyli;
    }

    public void setFamyli(String famyli) {
        Famyli = famyli;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
