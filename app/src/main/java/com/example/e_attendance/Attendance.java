package com.example.e_attendance;

public class Attendance {
    private String Date;
    private String Name;
    private String Time;

    public Attendance() {
    }
    public Attendance(String name) {
        this.Name = name;
    }

    public String getDate() {
        return Date;
    }

    public String getName() {
        return Name;
    }

    public String getTime() {
        return Time;
    }

    public void setDate(String date) {
        Date = date;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setTime(String time) {
        Time = time;
    }
}
