package com.itgosolutions.beastshopping.services;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.itgosolutions.beastshopping.entities.SharedLists;

/**
 * Created by dhaval on 12/20/2016.
 */

public class SharedListsServices {

    private SharedListsServices() {
    }

    public static class GetSharedListUsersRequest{

        public DatabaseReference reference;

        public GetSharedListUsersRequest(DatabaseReference reference) {
            this.reference = reference;
        }
    }

    public static class GetSharedLsitUsersResponse{

        public SharedLists sharedLists;
        public ValueEventListener valueEventListener;

        public GetSharedLsitUsersResponse() {
        }
    }


}
