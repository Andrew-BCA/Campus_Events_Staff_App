package com.example.campuseventsstaff;

public class User {

    private String username;
    private String email;
    private String password;
    private String dept;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email, String password,String dept) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.dept = dept;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
    public String getDept() {
        return dept;
    }
}
