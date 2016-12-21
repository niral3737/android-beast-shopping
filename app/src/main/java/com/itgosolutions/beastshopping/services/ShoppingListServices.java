package com.itgosolutions.beastshopping.services;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.itgosolutions.beastshopping.entities.SharedLists;
import com.itgosolutions.beastshopping.entities.ShoppingList;
import com.itgosolutions.beastshopping.infrastructure.ServiceResponse;

public class ShoppingListServices {

    private ShoppingListServices(){

    }

    public static class AddShoppingListRequest{

        public String shoppingListName;
        public String ownerName;
        public String ownerEmail;

        public AddShoppingListRequest(String shoppingListName, String ownerName, String ownerEmail) {
            this.shoppingListName = shoppingListName;
            this.ownerName = ownerName;
            this.ownerEmail = ownerEmail;
        }
    }

    public static class AddShoppingListResponse extends ServiceResponse{

    }

    public static class DeleteShoppingListRequest{

        public String ownerEmail;
        public String shoppingListId;

        public DeleteShoppingListRequest(String ownerEmail, String shoppingListId) {
            this.ownerEmail = ownerEmail;
            this.shoppingListId = shoppingListId;
        }
    }


    public static class EditShoppingListNameRequest{

        public String shoppingListName;
        public String shoppingListId;
        public String ownerEmail;
        public SharedLists sharedLists;

        public EditShoppingListNameRequest(String shoppingListName, String shoppingListId, String ownerEmail, SharedLists sharedLists) {
            this.shoppingListName = shoppingListName;
            this.shoppingListId = shoppingListId;
            this.ownerEmail = ownerEmail;
            this.sharedLists = sharedLists;
        }
    }

    public static class EditShoppingListNameResponse extends ServiceResponse{

    }


    public static class GetShoppingListRequest{

        public DatabaseReference reference;

        public GetShoppingListRequest(DatabaseReference reference) {
            this.reference = reference;
        }
    }

    public static class GetShoppingListResponse{

        public ShoppingList shoppingList;
        public ValueEventListener shoppingListValueEventListener;

        public GetShoppingListResponse() {

        }
    }

}
