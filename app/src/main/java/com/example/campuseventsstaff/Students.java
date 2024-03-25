package com.example.campuseventsstaff;

public class Students {
    private String rollno;
    private String username;
    private String dept;
    private String email;
    private String mobile;
    private String password;

    public Students() {
        // Default constructor required for Firebase
    }

    public Students(String rollno, String username, String dept, String email, String mobile, String password) {
        this.rollno = rollno;
        this.username = username;
        this.dept = dept;
        this.email = email;
        this.mobile = mobile;
        this.password = password;
    }

    public String getRollno() {
        return rollno;
    }

    public void setRollno(String rollno) {
        this.rollno = rollno;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
