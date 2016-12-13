package com.example.asus_cp.util;

import android.app.Application;
import android.content.Context;

/**
 * Created by asus-cp on 2016-12-07.
 */
public class MyApplication extends Application {

    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
