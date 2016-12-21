package com.itgosolutions.beastshopping.infrastructure;


import android.content.Context;
import android.content.SharedPreferences;

public class Utils {

    public static final String BEAST_PREFERENCE = "BEAST_PREFERENCE";
    public static final String USER_EMAIL = "user_mail";
    public static final String USERNAME = "username";

    /*
    * shorting shopping list
    * */
    public static final String SHOPPING_LIST_SORTING_BY = "shoppingListShortingBy";
    public static final String SORT_BY_PUBLISH_TIME = "key";
    public static final String SORT_BY_LIST_NAME = "listName";
    public static final String SORT_BY_OWNER = "ownerEmail";


    public static String encodeEmail(String userEmail) {
        return userEmail.replaceAll("\\.", ",");
    }

    public static String decodeEmail(String userEmail) {
        return userEmail.replaceAll("\\,", ".");
    }

    public static void clearSharedPreferences(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Utils.BEAST_PREFERENCE, Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
    }
}
