package com.itgosolutions.beastshopping.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.itgosolutions.beastshopping.infrastructure.Utils;
import com.itgosolutions.beastshopping.services.SortingServices;

public class SortListDialogFragment extends BaseDialog {

    SharedPreferences sharedPreferences;

    public static SortListDialogFragment newInstance(){
        return new SortListDialogFragment();
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        sharedPreferences = getActivity().getSharedPreferences(Utils.BEAST_PREFERENCE, Context.MODE_PRIVATE);

        final String[] values = {"Publish time", "List name", "Owner"};

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle("Sort by")
                .setSingleChoiceItems(values, getSelectedValue(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(i == 0){
                            sharedPreferences.edit().putString(Utils.SHOPPING_LIST_SORTING_BY, Utils.SORT_BY_PUBLISH_TIME).apply();
                        }else if(i == 1){
                            sharedPreferences.edit().putString(Utils.SHOPPING_LIST_SORTING_BY, Utils.SORT_BY_LIST_NAME).apply();
                        }else {
                            sharedPreferences.edit().putString(Utils.SHOPPING_LIST_SORTING_BY, Utils.SORT_BY_OWNER).apply();
                        }
                        bus.post(new SortingServices.SortShoppingListRequest());
                        dismiss();
                    }
                }).show();

        return alertDialog;
    }

    private int getSelectedValue() {
        if(sharedPreferences.getString(Utils.SHOPPING_LIST_SORTING_BY, Utils.SORT_BY_PUBLISH_TIME).equals(Utils.SORT_BY_PUBLISH_TIME)){
            return 0;
        }else if(sharedPreferences.getString(Utils.SHOPPING_LIST_SORTING_BY, Utils.SORT_BY_PUBLISH_TIME).equals(Utils.SORT_BY_LIST_NAME)){
            return 1;
        }else {
            return 2;
        }
    }
}
