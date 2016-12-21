package com.itgosolutions.beastshopping.live;

import com.google.firebase.database.DatabaseReference;
import com.itgosolutions.beastshopping.entities.ShoppingListItem;
import com.itgosolutions.beastshopping.infrastructure.BeastShoppingApplication;
import com.itgosolutions.beastshopping.services.ListItemServices;
import com.squareup.otto.Subscribe;

/**
 * Created by dhaval on 12/17/2016.
 */

public class LiveListItemServices extends BaseLiveService {

    public LiveListItemServices(BeastShoppingApplication application) {
        super(application);
    }

    @Subscribe
    public void addListItemRequest(ListItemServices.AddListItemRequest request){

        ListItemServices.AddListItemResponse response = new ListItemServices.AddListItemResponse();

        if(request.itemName.isEmpty()){
            response.setPropertyErrors("itemName", "item must have a name");
        }

        if(response.didSucceed()){
            DatabaseReference reference = databaseReference.child("shopping-list-items").child(request.shoppingListId).push();
            ShoppingListItem item = new ShoppingListItem(reference.getKey(), request.itemName, request.ownerEmail);
            reference.setValue(item);
        }

        bus.post(response);
    }

    @Subscribe
    public void buyListItemRequest(ListItemServices.BuyListItemRequest request){

        DatabaseReference reference = databaseReference.child("shopping-list-items").child(request.shoppingListId)
                .child(request.shoppingListItemId);

        reference.child("bought").setValue(request.bought);
        reference.child("boughtBy").setValue(request.boughtBy);

    }

    @Subscribe
    public void deleteListItemRequest(ListItemServices.DeleteListItemRequest request){
        DatabaseReference reference = databaseReference.child("shopping-list-items").child(request.shoppingListId)
                .child(request.shoppingListItemId);

        reference.removeValue();
    }
}
