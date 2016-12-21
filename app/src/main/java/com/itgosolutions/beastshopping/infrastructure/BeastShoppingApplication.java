package com.itgosolutions.beastshopping.infrastructure;

import android.app.Application;

import com.itgosolutions.beastshopping.live.Module;
import com.squareup.otto.Bus;

import net.danlew.android.joda.JodaTimeAndroid;

public class BeastShoppingApplication extends Application {


    private Bus bus;

    public BeastShoppingApplication() {
        bus = new Bus();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Module.register(this);
        JodaTimeAndroid.init(this);
    }

    public Bus getBus() {
        return bus;
    }
}
