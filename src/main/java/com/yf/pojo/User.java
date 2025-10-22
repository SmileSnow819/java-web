package com.yf.pojo;

/**
 * 用户模型类 (POJO)
 */
public class User {
    private int uId;
    private String uName;
    private String uPwd;

    public User() {}

    public User(int uId, String uName, String uPwd) {
        this.uId = uId;
        this.uName = uName;
        this.uPwd = uPwd;
    }

    // Getter 和 Setter (封装)
    public int getuId() { return uId; }
    public void setuId(int uId) { this.uId = uId; }
    public String getuName() { return uName; }
    public void setuName(String uName) { this.uName = uName; }
    public String getuPwd() { return uPwd; }
    public void setuPwd(String uPwd) { this.uPwd = uPwd; }

    @Override
    public String toString() {
        return "用户编号:" + uId + "\t用户名:" + uName + "\t密码:" + uPwd;
    }
}