package com.itgosolutions.beastshopping.live;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.itgosolutions.beastshopping.infrastructure.BeastShoppingApplication;
import com.squareup.otto.Bus;

public class BaseLiveService {
    protected Bus bus;
    protected BeastShoppingApplication application;
    protected FirebaseAuth auth;
    protected DatabaseReference databaseReference;

    public BaseLiveService(BeastShoppingApplication application) {
        this.application = application;
        bus = application.getBus();
        bus.register(this);
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }
}
