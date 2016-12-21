package com.itgosolutions.beastshopping.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.itgosolutions.beastshopping.infrastructure.BeastShoppingApplication;
import com.itgosolutions.beastshopping.infrastructure.Utils;
import com.squareup.otto.Bus;

public class BaseActivity extends AppCompatActivity {

    protected BeastShoppingApplication application;
    protected Bus bus;
    protected FirebaseAuth auth;
    protected String userEmail, userName;
    protected DatabaseReference databaseReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application = (BeastShoppingApplication) getApplication();
        bus = application.getBus();
        bus.register(this);
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        Log.e("ERROR", "onCreate: ERROR");

        SharedPreferences sharedPreferences = getSharedPreferences(Utils.BEAST_PREFERENCE, Context.MODE_PRIVATE);
        userEmail = sharedPreferences.getString(Utils.USER_EMAIL, "");
        userName = sharedPreferences.getString(Utils.USERNAME, "");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bus.unregister(this);
    }
}
