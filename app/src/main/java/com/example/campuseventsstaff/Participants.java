package com.example.campuseventsstaff;


// Participant.java
public class Participants {
    private String name;
    private String email;
    private String dept;
    private String eventName;
    private String eventDept;
    private String mobile;

    // Default constructor required for Firebase
    public Participants() {
    }

    public Participants(String name, String email, String dept, String eventName, String eventDept, String mobile) {
        this.name = name;
        this.email = email;
        this.dept = dept;
        this.eventName = eventName;
        this.eventDept = eventDept;
        this.mobile = mobile;
    }

    // Add getters for each field
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }


    public String getDept() {
        return dept;
    }

    public String getEventName() {
        return eventName;
    }

    public String getEventDept() {
        return eventDept;
    }

    public String getMobile() {
        return mobile;
    }

}

