package com.yf.model;

/**
 * User entity model, corresponds to the 'user' table.
 */
public class User {
    private int uId;
    private String uName;
    private String uPwd;

    // Default constructor
    public User() {}

    // Constructor for registration/login
    public User(String uName, String uPwd) {
        this.uName = uName;
        this.uPwd = uPwd;
    }

    // Getters and Setters
    public int getUId() {
        return uId;
    }

    public void setUId(int uId) {
        this.uId = uId;
    }

    public String getUName() {
        return uName;
    }

    public void setUName(String uName) {
        this.uName = uName;
    }

    public String getUPwd() {
        return uPwd;
    }

    public void setUPwd(String uPwd) {
        this.uPwd = uPwd;
    }

    @Override
    public String toString() {
        return "User{" +
                "uId=" + uId +
                ", uName='" + uName + '\'' +
                '}';
    }
}

