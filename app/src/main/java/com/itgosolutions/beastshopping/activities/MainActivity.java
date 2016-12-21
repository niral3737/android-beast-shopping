package com.itgosolutions.beastshopping.activities;


import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.itgosolutions.beastshopping.R;
import com.itgosolutions.beastshopping.dialog.AddListDialogFragment;
import com.itgosolutions.beastshopping.dialog.DeleteListDialogFragment;
import com.itgosolutions.beastshopping.dialog.SortListDialogFragment;
import com.itgosolutions.beastshopping.entities.ShoppingList;
import com.itgosolutions.beastshopping.infrastructure.Utils;
import com.itgosolutions.beastshopping.services.SortingServices;
import com.itgosolutions.beastshopping.viewholders.ShoppingListViewHolder;
import com.squareup.otto.Subscribe;

import java.util.Map;

public class MainActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener, View.OnClickListener {

    private FloatingActionButton fab;
    private RecyclerView recyclerShoppingList;

    private FirebaseRecyclerAdapter firebaseRecyclerAdapter;
    private SharedPreferences sharedPreferences;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        * set toolbar, inflate menu for it and set its title
        * */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_main);
        toolbar.setOnMenuItemClickListener(this);

        if (userName.contains(" ")) {
            String toolbarTitle = userName.substring(0, userName.indexOf(" ")) + " 's list";
            toolbar.setTitle(toolbarTitle);
        } else {
            toolbar.setTitle(userName + " 's list");
        }

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        recyclerShoppingList = (RecyclerView) findViewById(R.id.recycler_shopping_list);
        recyclerShoppingList.setLayoutManager(new LinearLayoutManager(getApplicationContext()) {

            @Override
            public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
                try {
                    super.onLayoutChildren(recycler, state);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        recyclerShoppingList.setHasFixedSize(true);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait. Loading lists");
        progressDialog.setCancelable(false);


        populateShoppingList();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void populateShoppingList() {
        progressDialog.show();

        DatabaseReference shoppingListReference = databaseReference.child("users-shopping-lists").child(userEmail);

        sharedPreferences = getSharedPreferences(Utils.BEAST_PREFERENCE, Context.MODE_PRIVATE);
        Query query = shoppingListReference.orderByChild(sharedPreferences.getString(Utils.SHOPPING_LIST_SORTING_BY, Utils.SORT_BY_PUBLISH_TIME));

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ShoppingList, ShoppingListViewHolder>(ShoppingList.class,
                R.layout.row_shopping_list,
                ShoppingListViewHolder.class,
                query) {

            @Override
            protected void populateViewHolder(ShoppingListViewHolder viewHolder, final ShoppingList shoppingList, int position) {
                viewHolder.populateShoppingList(shoppingList, getApplicationContext());

                viewHolder.rlList.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(ShoppingListDetailActivity.newInstance(MainActivity.this, shoppingList.getId(),
                                shoppingList.getListName(), shoppingList.getOwnerEmail()));
                    }
                });

                viewHolder.rlList.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {

                        if (userEmail.equals(Utils.encodeEmail(shoppingList.getOwnerEmail()))) {

                            DialogFragment dialogFragment = DeleteListDialogFragment.newInstance(shoppingList.getId(), true);
                            dialogFragment.setCancelable(false);
                            dialogFragment.show(getFragmentManager(), DeleteListDialogFragment.class.getSimpleName());

                        } else {
                            Toast.makeText(application.getApplicationContext(), "You cannot delete this list", Toast.LENGTH_SHORT).show();
                        }

                        return false;
                    }
                });
            }
        };

        firebaseRecyclerAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);

                if(progressDialog.isShowing())
                    progressDialog.dismiss();
            }
        });

        recyclerShoppingList.setAdapter(firebaseRecyclerAdapter);
        recyclerShoppingList.invalidate();


    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                logoutUser();
                break;
            case R.id.action_sort:
                SortListDialogFragment sortListDialogFragment = SortListDialogFragment.newInstance();
                sortListDialogFragment.show(getFragmentManager(), SortListDialogFragment.class.getSimpleName());
                break;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                DialogFragment dialogFragment = AddListDialogFragment.newInstance();
                dialogFragment.setCancelable(false);
                dialogFragment.show(getFragmentManager(), AddListDialogFragment.class.getName());
                break;
        }
    }

    private void logoutUser() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Logging out...");
        progressDialog.setCancelable(false);
        Utils.clearSharedPreferences(this);
        auth.signOut();
        progressDialog.dismiss();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }

    @Subscribe
    public void sortShoppingListRequest(SortingServices.SortShoppingListRequest request) {
        populateShoppingList();
    }

}
