package com.itgosolutions.beastshopping.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.itgosolutions.beastshopping.R;
import com.itgosolutions.beastshopping.services.ListItemServices;
import com.squareup.otto.Subscribe;

public class AddListItemDialogFragment extends BaseDialog implements View.OnClickListener {

    private static final String SHOPPING_LIST_ID = "shoppingListId";
    private static final String OWNER_EMAIL = "ownerName";

    private EditText et_list_item_name;

    private String shoppingListId, ownerEmail;

    public static AddListItemDialogFragment newInstance(String shoppingListId, String ownerEmail){
        AddListItemDialogFragment dialogFragment = new AddListItemDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(SHOPPING_LIST_ID, shoppingListId);
        bundle.putString(OWNER_EMAIL, ownerEmail);
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        shoppingListId = getArguments().getString(SHOPPING_LIST_ID);
        ownerEmail = getArguments().getString(OWNER_EMAIL);

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.dialog_add_list_item, null);

        et_list_item_name = (EditText) view.findViewById(R.id.et_list_item_name);

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle("Add an item")
                .setView(view)
                .setPositiveButton("ADD", null)
                .setNegativeButton("CANCEL", null)
                .show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(this);

        return alertDialog;
    }

    @Override
    public void onClick(View view) {
        bus.post(new ListItemServices.AddListItemRequest(shoppingListId, et_list_item_name.getText().toString(), ownerEmail));
    }

    @Subscribe
    public void addListItemResponse(ListItemServices.AddListItemResponse response){

        if(!response.didSucceed()){
            et_list_item_name.setError(response.getPropertyError("itemName"));
        }else {
            dismiss();
        }

    }
}
