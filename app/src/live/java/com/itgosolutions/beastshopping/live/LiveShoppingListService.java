package com.itgosolutions.beastshopping.live;

import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.itgosolutions.beastshopping.entities.ShoppingList;
import com.itgosolutions.beastshopping.infrastructure.BeastShoppingApplication;
import com.itgosolutions.beastshopping.infrastructure.Utils;
import com.itgosolutions.beastshopping.services.ShoppingListServices;
import com.itgosolutions.beastshopping.services.SortingServices;
import com.squareup.otto.Subscribe;

import java.util.HashMap;
import java.util.Map;


public class LiveShoppingListService extends BaseLiveService {

    public LiveShoppingListService(BeastShoppingApplication application) {
        super(application);
    }

    @Subscribe
    public void AddShoppingListRequest(ShoppingListServices.AddShoppingListRequest request) {

        ShoppingListServices.AddShoppingListResponse response = new ShoppingListServices.AddShoppingListResponse();

        if (request.shoppingListName.isEmpty()) {
            response.setPropertyErrors("listName", "shopping list must have a name");
        }

        if (response.didSucceed()) {

            /*
            * create a reference and push unique id
            * */
            DatabaseReference reference = databaseReference.child("users-shopping-lists").child(request.ownerEmail).push();

            HashMap<String, Object> dateCreated = new HashMap<>();
            dateCreated.put("date", ServerValue.TIMESTAMP);

            ShoppingList shoppingList = new ShoppingList(reference.getKey(),
                    request.shoppingListName, Utils.decodeEmail(request.ownerEmail), request.ownerName, dateCreated);

            reference.setValue(shoppingList);

            Toast.makeText(application.getApplicationContext(), "list is created", Toast.LENGTH_SHORT).show();

        }

        bus.post(response);

    }

    @Subscribe
    public void deleteShoppingListRequest(ShoppingListServices.DeleteShoppingListRequest request){

        DatabaseReference reference = databaseReference.child("users-shopping-lists").child(request.ownerEmail).child(request.shoppingListId);
        reference.removeValue();

    }

    @Subscribe
    public void editShoppingListNameRequest(ShoppingListServices.EditShoppingListNameRequest request){
        ShoppingListServices.EditShoppingListNameResponse response = new ShoppingListServices.EditShoppingListNameResponse();

        if(request.shoppingListName.isEmpty()){
            response.setPropertyErrors("listName", "shopping list must have a name");
        }

        if(response.didSucceed()){

            Map<String, Object> childUpdates = new HashMap<>();

            if (request.sharedLists != null && request.sharedLists.getSharedWith() != null) {
                for (String key : request.sharedLists.getSharedWith().keySet()
                        ) {
                    childUpdates.put("users-shopping-lists" + "/" + Utils.encodeEmail(key) + "/" + request.shoppingListId + "/" + "listName", request.shoppingListName);
                }
            }
            childUpdates.put("users-shopping-lists" + "/" + Utils.encodeEmail(request.ownerEmail) + "/" + request.shoppingListId + "/" + "listName", request.shoppingListName);

            databaseReference.updateChildren(childUpdates);

        }
        bus.post(response);
    }


    @Subscribe
    public void getShoppingListRequest(ShoppingListServices.GetShoppingListRequest request){

        final ShoppingListServices.GetShoppingListResponse response = new ShoppingListServices.GetShoppingListResponse();

        response.shoppingListValueEventListener = request.reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot != null){
                    response.shoppingList = dataSnapshot.getValue(ShoppingList.class);
                    bus.post(response);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
