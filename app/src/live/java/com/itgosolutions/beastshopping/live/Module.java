package com.itgosolutions.beastshopping.live;

import com.itgosolutions.beastshopping.infrastructure.BeastShoppingApplication;

public class Module {

    public static void register(BeastShoppingApplication application){
        new LiveAccountServices(application);
        new LiveShoppingListService(application);
        new LiveListItemServices(application);
        new LiveSharedListsServices(application);
    }
}
