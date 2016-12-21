package com.itgosolutions.beastshopping.entities;

public class ShoppingListItem {

    private String id;
    private String itemName;
    private String ownerEmail;
    private String boughtBy;
    private boolean bought;

    public ShoppingListItem() {
    }

    public ShoppingListItem(String id, String itemName, String ownerEmail) {
        this.id = id;
        this.itemName = itemName;
        this.ownerEmail = ownerEmail;
        this.boughtBy = "";
        this.bought = false;
    }

    public String getId() {
        return id;
    }

    public String getItemName() {
        return itemName;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public String getBoughtBy() {
        return boughtBy;
    }

    public boolean isBought() {
        return bought;
    }
}
