package com.itgosolutions.beastshopping.dialog;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.itgosolutions.beastshopping.entities.SharedLists;
import com.itgosolutions.beastshopping.infrastructure.Utils;
import com.itgosolutions.beastshopping.services.SharedListsServices;
import com.itgosolutions.beastshopping.services.ShoppingListServices;
import com.squareup.otto.Subscribe;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class DeleteListDialogFragment extends BaseDialog implements View.OnClickListener {

    private static final String SHOPPING_LIST_ID = "shoppingListId";
    private static final String IS_LONG_CLICKED = "isLongClicked";

    private String shoppingListId;
    private boolean isLongClicked;

    private ValueEventListener sharedListValueEventListener;
    private DatabaseReference sharedListReference;
    private SharedLists sharedLists;

    public static DeleteListDialogFragment newInstance(String shoppingListId, boolean isLongClicked) {

        Bundle bundle = new Bundle();
        bundle.putString(SHOPPING_LIST_ID, shoppingListId);
        bundle.putBoolean(IS_LONG_CLICKED, isLongClicked);

        DeleteListDialogFragment deleteListDialogFragment = new DeleteListDialogFragment();
        deleteListDialogFragment.setArguments(bundle);

        return deleteListDialogFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shoppingListId = getArguments().getString(SHOPPING_LIST_ID);
        isLongClicked = getArguments().getBoolean(IS_LONG_CLICKED);

        /*
        * get the users, to whom current shopping list is shared with
        * */
        sharedListReference = databaseReference.child("shared-lists").child(shoppingListId);
        bus.post(new SharedListsServices.GetSharedListUsersRequest(sharedListReference));

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle("Delete List")
                .setMessage("Are you sure?")
                .setPositiveButton("YES", null)
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                    }
                })
                .show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(this);

        return alertDialog;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (sharedListValueEventListener != null)
            sharedListReference.removeEventListener(sharedListValueEventListener);
    }

    @Override
    public void onClick(View view) {

//        if (isLongClicked) {
//            bus.post(new ShoppingListServices.DeleteShoppingListRequest(userEmail, shoppingListId));
//            dismiss();
//        }
        try {
            deleteShoppingListFromAllUsers();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        dismiss();

    }

    private void deleteShoppingListFromAllUsers() throws UnsupportedEncodingException {

        Map<String, Object> childUpdates = new HashMap<>();

        if (sharedLists != null && sharedLists.getSharedWith() != null) {
            for (String key : sharedLists.getSharedWith().keySet()
                    ) {
                System.out.println(key);
                childUpdates.put("users-shopping-lists" + "/" + Utils.encodeEmail(key) + "/" + shoppingListId , null);
            }
        }
        childUpdates.put("users-shopping-lists" + "/" + Utils.encodeEmail(userEmail) + "/" + shoppingListId, null);
        childUpdates.put("shopping-list-items" + "/" + shoppingListId ,null);
        childUpdates.put("shared-lists" + "/" + shoppingListId, null);

        databaseReference.updateChildren(childUpdates);
    }

    @Subscribe
    public void getSharedListUsers(SharedListsServices.GetSharedLsitUsersResponse response) {
        if (response.sharedLists != null) {
            sharedLists = response.sharedLists;

        } else {
            sharedLists = new SharedLists();
        }
        sharedListValueEventListener = response.valueEventListener;
    }
}
