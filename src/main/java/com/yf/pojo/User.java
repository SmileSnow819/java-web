package com.yf.pojo;

public class User {
    // 属性封装
    private int uId;
    private String uName;
    private String uPwd;

    // 构造方法 (无参)
    public User() {
    }

    // 构造方法 (全参，方便注册)
    public User(String uName, String uPwd) {
        this.uName = uName;
        this.uPwd = uPwd;
    }

    // 构造方法 (包含ID，方便查询)
    public User(int uId, String uName, String uPwd) {
        this.uId = uId;
        this.uName = uName;
        this.uPwd = uPwd;
    }

    // Getter 和 Setter
    public int getuId() {
        return uId;
    }

    public void setuId(int uId) {
        this.uId = uId;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getuPwd() {
        return uPwd;
    }

    public void setuPwd(String uPwd) {
        this.uPwd = uPwd;
    }

    // toString 方法 (方便打印信息)
    @Override
    public String toString() {
        return "User{" +
                "uId=" + uId +
                ", uName='" + uName + '\'' +
                ", uPwd='" + uPwd + '\'' +
                '}';
    }
}
