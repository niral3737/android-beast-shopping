package com.itgosolutions.beastshopping.entities;

import java.util.HashMap;


public class User {

    private String userEmail;
    private String userName;
    private HashMap<String, Object> dateJoined;
    private boolean hasLoggedInWithPassword;

    public User() {
    }

    public User(String userEmail, String userName, HashMap<String, Object> dateJoined, boolean hasLoggedInWithPassword) {
        this.userEmail = userEmail;
        this.userName = userName;
        this.dateJoined = dateJoined;
        this.hasLoggedInWithPassword = hasLoggedInWithPassword;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public HashMap<String, Object> getDateJoined() {
        return dateJoined;
    }

    public boolean isHasLoggedInWithPassword() {
        return hasLoggedInWithPassword;
    }
}
