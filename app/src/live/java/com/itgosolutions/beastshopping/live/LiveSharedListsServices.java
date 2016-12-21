package com.itgosolutions.beastshopping.live;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.itgosolutions.beastshopping.entities.SharedLists;
import com.itgosolutions.beastshopping.infrastructure.BeastShoppingApplication;
import com.itgosolutions.beastshopping.services.SharedListsServices;
import com.squareup.otto.Subscribe;

/**
 * Created by dhaval on 12/20/2016.
 */

public class LiveSharedListsServices extends BaseLiveService {

    public LiveSharedListsServices(BeastShoppingApplication application) {
        super(application);
    }

    @Subscribe
    public void getSharedListUsersRequest(SharedListsServices.GetSharedListUsersRequest request) {

        final SharedListsServices.GetSharedLsitUsersResponse response = new SharedListsServices.GetSharedLsitUsersResponse();

        response.valueEventListener = request.reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                response.sharedLists = dataSnapshot.getValue(SharedLists.class);;
                bus.post(response);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
