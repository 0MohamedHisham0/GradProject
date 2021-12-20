package com.hti.Grad_Project.Model;

public class UserModel {
    private String email;
    private String userName;

    public UserModel() {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public UserModel(String email, String userName) {
        this.email = email;
        this.userName = userName;
    }
}
