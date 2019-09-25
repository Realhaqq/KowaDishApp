package com.haqq.namu;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

/**
 * Created by sagar on 28/6/17.
 */

public class MyApp extends Application {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}