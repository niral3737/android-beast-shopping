package com.itgosolutions.beastshopping.dialog;


import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.itgosolutions.beastshopping.R;
import com.itgosolutions.beastshopping.entities.SharedLists;
import com.itgosolutions.beastshopping.infrastructure.Utils;
import com.itgosolutions.beastshopping.services.SharedListsServices;
import com.itgosolutions.beastshopping.services.ShoppingListServices;
import com.squareup.otto.Subscribe;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class EditListDialogFragment extends BaseDialog implements View.OnClickListener {

    private static final String SHOPPING_LIST_ID = "shoppingListId";
    private static final String SHOPPING_LIST_NAME = "shoppingListName";
    private static final String SHOPPING_LIST_OWNER_EMAIL = "shoppingListOwnerEmail";

    private EditText et_list_name;

    private String shoppingListId;
    private String ownerEmail;

    private ValueEventListener sharedListValueEventListener;
    private DatabaseReference sharedListReference;
    private SharedLists sharedLists;

    public static EditListDialogFragment newInstance(String shoppingListId, String shoppingListName, String ownerEmail){
        Bundle arguments = new Bundle();
        arguments.putString(SHOPPING_LIST_ID, shoppingListId);
        arguments.putString(SHOPPING_LIST_NAME, shoppingListName);
        arguments.putString(SHOPPING_LIST_OWNER_EMAIL, ownerEmail);

        EditListDialogFragment fragment = new EditListDialogFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        shoppingListId = getArguments().getString(SHOPPING_LIST_ID);
        ownerEmail = getArguments().getString(SHOPPING_LIST_OWNER_EMAIL);

        /*
        * get the users, to whom current shopping list is shared with
        * */
        sharedListReference = databaseReference.child("shared-lists").child(shoppingListId);
        bus.post(new SharedListsServices.GetSharedListUsersRequest(sharedListReference));
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.dialog_edit_shopping_list_name, null);

        et_list_name = (EditText) view.findViewById(R.id.et_list_name);
        et_list_name.setText(getArguments().getString(SHOPPING_LIST_NAME));

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle("Edit list name")
                .setView(view)
                .setPositiveButton("UPDATE", null)
                .setNegativeButton("CANCEL", null)
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
        bus.post(new ShoppingListServices.EditShoppingListNameRequest(et_list_name.getText().toString(), shoppingListId, ownerEmail, sharedLists));
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

    @Subscribe
    public void editShoppingListNameResponse(ShoppingListServices.EditShoppingListNameResponse response){
        if(!response.didSucceed()){
            et_list_name.setError(response.getPropertyError("listName"));
        }else {
            dismiss();
        }
    }
}
