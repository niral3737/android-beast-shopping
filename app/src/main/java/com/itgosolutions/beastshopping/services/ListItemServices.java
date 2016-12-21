package com.itgosolutions.beastshopping.services;

import com.itgosolutions.beastshopping.infrastructure.ServiceResponse;

/**
 * Created by dhaval on 12/17/2016.
 */

public class ListItemServices {

    private ListItemServices() {
    }

    public static class AddListItemRequest{
        public String shoppingListId;
        public String itemName;
        public String ownerEmail;

        public AddListItemRequest(String shoppingListId, String itemName, String ownerEmail) {
            this.shoppingListId = shoppingListId;
            this.itemName = itemName;
            this.ownerEmail = ownerEmail;
        }
    }

    public static class AddListItemResponse extends ServiceResponse{

    }

    public static class BuyListItemRequest{

        public String shoppingListId;
        public String shoppingListItemId;
        public String boughtBy;
        public boolean bought;

        public BuyListItemRequest(String shoppingListId, String shoppingListItemId, String boughtBy, boolean bought) {
            this.shoppingListId = shoppingListId;
            this.shoppingListItemId = shoppingListItemId;
            this.boughtBy = boughtBy;
            this.bought = bought;
        }
    }

    public static class DeleteListItemRequest{

        public String shoppingListId;
        public String shoppingListItemId;

        public DeleteListItemRequest(String shoppingListId, String shoppingListItemId) {
            this.shoppingListId = shoppingListId;
            this.shoppingListItemId = shoppingListItemId;
        }
    }
}
