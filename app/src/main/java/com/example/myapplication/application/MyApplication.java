package com.example.myapplication.application;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

/**
 * Created by hongli on 2018/5/18.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(MyApplication.this);
    }
}
