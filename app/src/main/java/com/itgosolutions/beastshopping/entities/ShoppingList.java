package com.itgosolutions.beastshopping.entities;

import com.google.firebase.database.ServerValue;

import java.util.HashMap;

public class ShoppingList {

    private String id;
    private String listName;
    private String ownerEmail;
    private String ownerName;
    private HashMap<String, Object> dateCreated;
    private HashMap<String, Object> dateLastChanged;

    public ShoppingList() {
    }

    public ShoppingList(String id, String listName, String ownerEmail, String ownerName, HashMap<String, Object> dateCreated) {
        this.id = id;
        this.listName = listName;
        this.ownerEmail = ownerEmail;
        this.ownerName = ownerName;
        this.dateCreated = dateCreated;

        /*
        * update the date last changed value to server timestamp
        * */
        HashMap<String, Object> dateLastChanged = new HashMap<>();
        dateLastChanged.put("date", ServerValue.TIMESTAMP);
        this.dateLastChanged = dateLastChanged;
    }

    public String getId() {
        return id;
    }

    public String getListName() {
        return listName;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public HashMap<String, Object> getDateCreated() {
        if(dateLastChanged!=null){
            return dateCreated;
        }
        HashMap<String, Object> dateCreated = new HashMap<>();
        dateCreated.put("date", ServerValue.TIMESTAMP);
        return dateCreated;
    }

    public HashMap<String, Object> getDateLastChanged() {
        return dateLastChanged;
    }
}
