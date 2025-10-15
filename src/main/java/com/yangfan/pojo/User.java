// 路径: src/main/java/com/个人名字/pojo/User.java

package com.yangfan.pojo;

public class User {
    private Integer u_id;
    private String u_name;
    private String u_pwd;

    // 构造方法
    public User() {
    }

    public User(Integer u_id, String u_name, String u_pwd) {
        this.u_id = u_id;
        this.u_name = u_name;
        this.u_pwd = u_pwd;
    }

    // Getter 和 Setter 方法
    public Integer getU_id() {
        return u_id;
    }

    public void setU_id(Integer u_id) {
        this.u_id = u_id;
    }

    public String getU_name() {
        return u_name;
    }

    public void setU_name(String u_name) {
        this.u_name = u_name;
    }

    public String getU_pwd() {
        return u_pwd;
    }

    public void setU_pwd(String u_pwd) {
        this.u_pwd = u_pwd;
    }

    // toString 方法，方便输出
    @Override
    public String toString() {
        return "User{" +
                "u_id=" + u_id +
                ", u_name='" + u_name + '\'' +
                ", u_pwd='" + u_pwd + '\'' +
                '}';
    }
}