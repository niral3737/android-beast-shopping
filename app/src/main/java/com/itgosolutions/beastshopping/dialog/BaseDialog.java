package com.itgosolutions.beastshopping.dialog;


import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.itgosolutions.beastshopping.infrastructure.BeastShoppingApplication;
import com.itgosolutions.beastshopping.infrastructure.Utils;
import com.squareup.otto.Bus;

public class BaseDialog extends DialogFragment {
    protected BeastShoppingApplication application;
    protected Bus bus;
    protected DatabaseReference databaseReference;
    protected String userEmail, userName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application = (BeastShoppingApplication) getActivity().getApplication();
        bus = application.getBus();
        bus.register(this);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        userEmail = getActivity().getSharedPreferences(Utils.BEAST_PREFERENCE, Context.MODE_PRIVATE).getString(Utils.USER_EMAIL, "");
        userName = getActivity().getSharedPreferences(Utils.BEAST_PREFERENCE, Context.MODE_PRIVATE).getString(Utils.USERNAME, "");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bus.unregister(this);
    }
}
