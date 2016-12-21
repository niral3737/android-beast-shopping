package com.itgosolutions.beastshopping.activities;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.itgosolutions.beastshopping.R;
import com.itgosolutions.beastshopping.dialog.AddListItemDialogFragment;
import com.itgosolutions.beastshopping.dialog.EditListDialogFragment;
import com.itgosolutions.beastshopping.entities.ShoppingList;
import com.itgosolutions.beastshopping.entities.ShoppingListItem;
import com.itgosolutions.beastshopping.infrastructure.Utils;
import com.itgosolutions.beastshopping.services.ListItemServices;
import com.itgosolutions.beastshopping.services.ShoppingListServices;
import com.itgosolutions.beastshopping.viewholders.ShoppingListItemsViewHolder;
import com.squareup.otto.Subscribe;

public class ShoppingListDetailActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener, View.OnClickListener {

    private static final String SHOPPING_LIST_ID = "shoppingListId";
    private static final String SHOPPING_LIST_NAME = "shoppingListName";
    private static final String SHOPPING_LIST_OWNER_EMAIL = "shoppingListOwnerEmail";

    private String shoppingListId, shoppingListName, shoppingListOwnerEmail;

    private DatabaseReference shoppingListReference;
    private ShoppingList shoppingList;
    private ValueEventListener shoppingListValueEventReference;
    private FirebaseRecyclerAdapter listItemAdapter;

    private Toolbar toolbar;
    private FloatingActionButton fab;
    private RecyclerView recyclerShoppingListItems;

    /*
    * create new instance and return an intent
    * */
    public static Intent newInstance(Context context, String shoppingListId, String shoppingListName, String ownerEmail){
        Intent intent = new Intent(context, ShoppingListDetailActivity.class);
        intent.putExtra(SHOPPING_LIST_ID, shoppingListId);
        intent.putExtra(SHOPPING_LIST_NAME, shoppingListName);
        intent.putExtra(SHOPPING_LIST_OWNER_EMAIL, ownerEmail);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list_detail);

        if(getIntent() != null){
            shoppingListId = getIntent().getStringExtra(SHOPPING_LIST_ID);
            shoppingListName = getIntent().getStringExtra(SHOPPING_LIST_NAME);
            shoppingListOwnerEmail = getIntent().getStringExtra(SHOPPING_LIST_OWNER_EMAIL);
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar_shopping_list_detail);
        if(Utils.encodeEmail(shoppingListOwnerEmail).equals(Utils.encodeEmail(userEmail))){
            toolbar.inflateMenu(R.menu.menu_shopping_list_detail);
        }
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setTitle(shoppingListName);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        recyclerShoppingListItems = (RecyclerView) findViewById(R.id.recycler_shopping_list_items);
        recyclerShoppingListItems.setLayoutManager(new LinearLayoutManager(this));
        recyclerShoppingListItems.setHasFixedSize(true);

        shoppingListReference = databaseReference.child("users-shopping-lists").child(Utils.encodeEmail(shoppingListOwnerEmail))
                .child(shoppingListId);
        bus.post(new ShoppingListServices.GetShoppingListRequest(shoppingListReference));

        populateListItems();
    }

    private void populateListItems() {

        DatabaseReference reference = databaseReference.child("shopping-list-items").child(shoppingListId);

        listItemAdapter = new FirebaseRecyclerAdapter<ShoppingListItem, ShoppingListItemsViewHolder>(
                ShoppingListItem.class,
                R.layout.row_shopping_list_item,
                ShoppingListItemsViewHolder.class,
                reference
        ) {

            @Override
            protected void populateViewHolder(ShoppingListItemsViewHolder viewHolder, final ShoppingListItem model, int position) {

                viewHolder.populateListItem(model, userEmail);

                viewHolder.rlListItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!model.isBought()){
                            //post bus
                            bus.post(new ListItemServices.BuyListItemRequest(shoppingListId, model.getId(), userEmail, true));
                        }else {
                            if(Utils.encodeEmail(model.getBoughtBy()).equals(Utils.encodeEmail(userEmail))){
                                //post bus
                                bus.post(new ListItemServices.BuyListItemRequest(shoppingListId, model.getId(), "", false));
                            }else {
                                Toast.makeText(application.getApplicationContext(), "You cannot perform this action", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

                viewHolder.ivDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // delete an item
                        if(Utils.encodeEmail(model.getOwnerEmail()).equals(Utils.encodeEmail(userEmail))){
                            bus.post(new ListItemServices.DeleteListItemRequest(shoppingListId, model.getId()));
                        }else {
                            Toast.makeText(application.getApplicationContext(), "You cannot delete this item", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        };

        recyclerShoppingListItems.setAdapter(listItemAdapter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        shoppingListReference.removeEventListener(shoppingListValueEventReference);
        listItemAdapter.cleanup();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fab:
                DialogFragment dialogFragment = AddListItemDialogFragment.newInstance(shoppingListId, userEmail);
                dialogFragment.setCancelable(false);
                dialogFragment.show(getFragmentManager(), AddListItemDialogFragment.class.getSimpleName());
                break;
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_edit_shopping_list_name:
                DialogFragment dialogFragment = EditListDialogFragment.newInstance(shoppingListId, shoppingListName, shoppingListOwnerEmail);
                dialogFragment.setCancelable(false);
                dialogFragment.show(getFragmentManager(), EditListDialogFragment.class.getSimpleName());
                break;
            case R.id.action_share_shopping_list:
                Intent intent = ShareListActivity.newInstance(ShoppingListDetailActivity.this, shoppingListId);
                startActivity(intent);
                break;
        }

        return true;
    }


    @Subscribe
    public void getShoppingListResponse(ShoppingListServices.GetShoppingListResponse response){
        shoppingList = response.shoppingList;
        shoppingListValueEventReference = response.shoppingListValueEventListener;
        toolbar.setTitle(shoppingList.getListName());
    }


}
