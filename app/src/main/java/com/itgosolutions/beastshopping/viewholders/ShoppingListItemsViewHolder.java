package com.itgosolutions.beastshopping.viewholders;


import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.itgosolutions.beastshopping.R;
import com.itgosolutions.beastshopping.entities.ShoppingListItem;
import com.itgosolutions.beastshopping.infrastructure.Utils;

public class ShoppingListItemsViewHolder extends RecyclerView.ViewHolder {

    private TextView tvItemName, tvBoughtBy;
    public ImageView ivDelete;
    public RelativeLayout rlListItem;

    public ShoppingListItemsViewHolder(View itemView) {
        super(itemView);
        tvItemName = (TextView) itemView.findViewById(R.id.tv_item_name);
        tvBoughtBy = (TextView) itemView.findViewById(R.id.tv_bought_by);
        ivDelete  = (ImageView) itemView.findViewById(R.id.iv_delete);
        rlListItem = (RelativeLayout) itemView.findViewById(R.id.rl_list_item);
    }

    public void populateListItem(ShoppingListItem shoppingListItem, String userEmail){
        tvItemName.setText(shoppingListItem.getItemName());

        if(shoppingListItem.isBought()){
            tvItemName.setPaintFlags(tvItemName.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
            tvBoughtBy.setVisibility(View.VISIBLE);
            if(Utils.encodeEmail(userEmail).equals(shoppingListItem.getBoughtBy())){
                tvBoughtBy.setText("Bought by: You");
            }else {
                tvBoughtBy.setText("Bought by: "+shoppingListItem.getBoughtBy());
            }
        }else {
            tvItemName.setPaintFlags(tvItemName.getPaintFlags()& (~Paint.STRIKE_THRU_TEXT_FLAG));
            tvBoughtBy.setVisibility(View.GONE);
        }


    }

}
