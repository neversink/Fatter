package com.xiachen.fatter;

import android.app.Application;

import com.activeandroid.ActiveAndroid;

/**
 * Created by xiachen on 15/12/6.
 */
public class MyApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        ActiveAndroid.dispose();
    }
}

