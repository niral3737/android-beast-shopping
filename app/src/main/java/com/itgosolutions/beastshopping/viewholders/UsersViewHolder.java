package com.itgosolutions.beastshopping.viewholders;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.itgosolutions.beastshopping.R;
import com.itgosolutions.beastshopping.entities.User;

public class UsersViewHolder extends RecyclerView.ViewHolder {

    public LinearLayout llMain;
    public RelativeLayout rlUser;
    public TextView tvUserEmail;
    public ImageView ivCheck;
    public CardView cvList;

    public UsersViewHolder(View itemView) {
        super(itemView);
        llMain = (LinearLayout) itemView.findViewById(R.id.ll_main);
        cvList = (CardView) itemView.findViewById(R.id.cv_list);
        rlUser = (RelativeLayout) itemView.findViewById(R.id.rl_user);
        tvUserEmail = (TextView) itemView.findViewById(R.id.tv_user_email);
        ivCheck = (ImageView) itemView.findViewById(R.id.iv_check);
    }
    public void hideAllItems(){
        llMain.setVisibility(View.GONE);
        cvList.setVisibility(View.GONE);
        rlUser.setVisibility(View.GONE);
        tvUserEmail.setVisibility(View.GONE);
        ivCheck.setVisibility(View.GONE);
    }

    public void showItems(){
        llMain.setVisibility(View.VISIBLE);
        cvList.setVisibility(View.VISIBLE);
        rlUser.setVisibility(View.VISIBLE);
        tvUserEmail.setVisibility(View.VISIBLE);
    }


}
