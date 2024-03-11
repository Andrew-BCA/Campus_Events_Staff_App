package com.example.campuseventsstaff;


public class Participants {


    private String name;
    private String dept;
    private String eventName;
    private String mobile;
    private String eventDept;
    private  String roll;


    public Participants() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }
    public Participants(String name, String dept, String eventName,String mobile,String eventDept,String roll,String Mobile) {
        this.name = name;
        this.dept = dept;
        this.eventName = eventName;
        this.eventDept = eventDept;
        this.mobile = mobile;
        this.roll = roll;
    }

    public String getName() { return name; }

    public String getDept() {
        return dept;
    }

    public String getEventName() {
        return eventName;
    }

    public String getEventDept() {
        return eventDept;
    }

    public String getMobile(){ return mobile; }

    public  String getRoll(){ return roll; }
}


