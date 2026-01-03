package com.yf.model;

public class Student {
    private int stuNo;
    private String stuName;
    private int stuAge;
    private String stuImg;  // 学生头像路径

    public Student() {}

    public int getStuNo() {
        return stuNo;
    }

    public void setStuNo(int stuNo) {
        this.stuNo = stuNo;
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public int getStuAge() {
        return stuAge;
    }

    public void setStuAge(int stuAge) {
        this.stuAge = stuAge;
    }

    public String getStuImg() {
        return stuImg;
    }

    public void setStuImg(String stuImg) {
        this.stuImg = stuImg;
    }
}