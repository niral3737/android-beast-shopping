package com.itgosolutions.beastshopping.viewholders;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.itgosolutions.beastshopping.R;
import com.itgosolutions.beastshopping.entities.ShoppingList;

import net.danlew.android.joda.DateUtils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class ShoppingListViewHolder extends RecyclerView.ViewHolder {

    private TextView tvListName, tvCreatedBy, tvListCreatedDate;
    public RelativeLayout rlList;

    public ShoppingListViewHolder(View itemView) {
        super(itemView);

        rlList = (RelativeLayout) itemView.findViewById(R.id.rl_list);
        tvListName = (TextView) itemView.findViewById(R.id.tv_list_name);
        tvCreatedBy = (TextView) itemView.findViewById(R.id.tv_created_by);
        tvListCreatedDate = (TextView) itemView.findViewById(R.id.tv_list_created_date);
    }

    public void populateShoppingList(ShoppingList shoppingList, Context context){

        tvListName.setText(shoppingList.getListName());
        tvCreatedBy.setText("Created by: " + shoppingList.getOwnerName());

        if(shoppingList.getDateCreated().get("date") != null){
            DateTime dateTime = new DateTime((long)shoppingList.getDateCreated().get("date"));
            tvListCreatedDate.setText(dateTime.toString("EEEE, d MMMM"));
        }


    }
}
