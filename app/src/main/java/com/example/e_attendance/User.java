package com.example.e_attendance;

public class User {
    private String Department;
    private String Email;
    private String Name;
    private String Role;

    public User() {

    }
    public User(String department, String email, String name, String role) {
        Department = department;
        Email = email;
        Name = name;
        Role = role;
    }

    public String getDepartment() {
        return Department;
    }

    public String getEmail() {
        return Email;
    }

    public String getName() {
        return Name;
    }

    public String getRole() {
        return Role;
    }
}
