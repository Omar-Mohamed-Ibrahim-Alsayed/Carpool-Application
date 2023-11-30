package com.example.carpool7813.utilities;

public class User {
    private String userType;
    private String email;
    private String name;


    public User() {
        // Default constructor required for Firebase
    }

    public User(String userType, String email, String otherUserData) {
        this.userType = userType;
        this.email = email;
        this.name = otherUserData;
    }

    // Getters and setters for the fields
    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String otherUserData) {
        this.name = otherUserData;
    }
}
