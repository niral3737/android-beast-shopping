package com.itgosolutions.beastshopping.entities;

import java.util.HashMap;


public class SharedLists {

    private HashMap<String, Boolean> sharedWith;

    public SharedLists() {
    }

    public SharedLists(HashMap<String, Boolean> sharedWith) {
        this.sharedWith = sharedWith;
    }

    public HashMap<String, Boolean> getSharedWith() {
        return sharedWith;
    }
}
