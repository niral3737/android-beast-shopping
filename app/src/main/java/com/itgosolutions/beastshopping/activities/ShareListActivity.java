package com.itgosolutions.beastshopping.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.itgosolutions.beastshopping.R;
import com.itgosolutions.beastshopping.entities.SharedLists;
import com.itgosolutions.beastshopping.entities.ShoppingList;
import com.itgosolutions.beastshopping.entities.User;
import com.itgosolutions.beastshopping.infrastructure.Utils;
import com.itgosolutions.beastshopping.services.SharedListsServices;
import com.itgosolutions.beastshopping.services.ShoppingListServices;
import com.itgosolutions.beastshopping.viewholders.UsersViewHolder;
import com.squareup.otto.Subscribe;

public class ShareListActivity extends BaseActivity {

    private static final String SHOPPING_LIST_ID = "shoppingListId";

    private RecyclerView recyclerUsers;
    private FirebaseRecyclerAdapter usersAdapter;

    private ValueEventListener sharedListValueEventListener, shoppingListValueEventListener;

    private String shoppingListId;
    private SharedLists sharedLists;
    private ShoppingList shoppingList;
    private DatabaseReference sharedListReference, shoppingListReference;



    public static Intent newInstance(Context context, String shoppingListId) {
        Intent intent = new Intent(context, ShareListActivity.class);
        intent.putExtra(SHOPPING_LIST_ID, shoppingListId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_list);

        if (getIntent() != null) {
            shoppingListId = getIntent().getStringExtra(SHOPPING_LIST_ID);
        }

        recyclerUsers = (RecyclerView) findViewById(R.id.recycler_users);
        recyclerUsers.setLayoutManager(new LinearLayoutManager(this));
        recyclerUsers.setHasFixedSize(true);

        /*
        * get the users, to whom current shopping list is shared with
        * */
        sharedListReference = databaseReference.child("shared-lists").child(shoppingListId);
        bus.post(new SharedListsServices.GetSharedListUsersRequest(sharedListReference));
        /*
        * get shopping list
        * */
        shoppingListReference = databaseReference.child("users-shopping-lists")
                .child(Utils.encodeEmail(userEmail)).child(shoppingListId);
        bus.post(new ShoppingListServices.GetShoppingListRequest(shoppingListReference));


        populateUsers();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        usersAdapter.cleanup();
        if (sharedListValueEventListener != null)
            sharedListReference.removeEventListener(sharedListValueEventListener);
        if(shoppingListValueEventListener != null)
            shoppingListReference.removeEventListener(shoppingListValueEventListener);
    }

    private void populateUsers() {

        DatabaseReference reference = databaseReference.child("users");

        usersAdapter = new FirebaseRecyclerAdapter<User, UsersViewHolder>(
                User.class,
                R.layout.row_user,
                UsersViewHolder.class,
                reference
        ) {

            @Override
            protected void populateViewHolder(final UsersViewHolder viewHolder, final User user, int position) {

                if (user.getUserEmail().equals(Utils.decodeEmail(userEmail))) {
                    viewHolder.hideAllItems();

                } else {
                    viewHolder.showItems();
                    viewHolder.tvUserEmail.setText(user.getUserEmail());

                    if(isSharedWith(sharedLists, user.getUserEmail())){
                        viewHolder.ivCheck.setVisibility(View.VISIBLE);
                    }else {
                        viewHolder.ivCheck.setVisibility(View.INVISIBLE);
                    }

                }

                final DatabaseReference sharedListReference = databaseReference.child("shared-lists")
                        .child(shoppingListId).child("sharedWith").child(Utils.encodeEmail(user.getUserEmail()));
                final DatabaseReference usersShoppingListsReference = databaseReference.child("users-shopping-lists")
                        .child(Utils.encodeEmail(user.getUserEmail())).child(shoppingListId);

                viewHolder.rlUser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(!isSharedWith(sharedLists, user.getUserEmail())){
                            viewHolder.ivCheck.setVisibility(View.VISIBLE);
                            sharedListReference.setValue(true);
                            usersShoppingListsReference.setValue(shoppingList);

                        }else {
                            viewHolder.ivCheck.setVisibility(View.INVISIBLE);
                            sharedListReference.removeValue();
                            usersShoppingListsReference.removeValue();
                        }

                    }
                });


            }
        };

        recyclerUsers.setAdapter(usersAdapter);
    }

    private boolean isSharedWith(SharedLists sharedLists, String userEmail){
        if(sharedLists != null && sharedLists.getSharedWith() != null){
            return sharedLists.getSharedWith().containsKey(Utils.encodeEmail(userEmail));
        }
        return false;
    }

    @Subscribe
    public void getSharedListUsers(SharedListsServices.GetSharedLsitUsersResponse response) {
        if(response.sharedLists != null){
            sharedLists = response.sharedLists;

        }else {
            sharedLists = new SharedLists();
        }
        sharedListValueEventListener = response.valueEventListener;
    }

    @Subscribe
    public void getShoppingListResponse(ShoppingListServices.GetShoppingListResponse response){
        shoppingListValueEventListener = response.shoppingListValueEventListener;
        shoppingList = response.shoppingList;
    }


}
