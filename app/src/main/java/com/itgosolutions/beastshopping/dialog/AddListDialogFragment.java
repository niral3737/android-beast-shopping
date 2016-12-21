package com.itgosolutions.beastshopping.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.itgosolutions.beastshopping.R;
import com.itgosolutions.beastshopping.services.ShoppingListServices;
import com.squareup.otto.Subscribe;

public class AddListDialogFragment extends BaseDialog implements View.OnClickListener {

    private EditText et_list_name;

    public static AddListDialogFragment newInstance() {
        return new AddListDialogFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.dialog_add_list, null);

        et_list_name = (EditText) view.findViewById(R.id.et_list_name);

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle("Create list")
                .setPositiveButton("Create", null)
                .setNegativeButton("Cancel", null)
                .show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(this);

        return alertDialog;
    }

    @Subscribe
    public void AddShoppingListResponse(ShoppingListServices.AddShoppingListResponse response) {

        if (!response.didSucceed()) {
            et_list_name.setError(response.getPropertyError("listName"));
        } else {
            dismiss();
        }

    }

    @Override
    public void onClick(View view) {
        bus.post(new ShoppingListServices.AddShoppingListRequest(et_list_name.getText().toString(), userName, userEmail));
    }
}
