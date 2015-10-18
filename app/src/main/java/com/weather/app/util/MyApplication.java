package com.weather.app.util;

import android.content.Context;

import org.litepal.LitePalApplication;

/**
 * 构建Application 管理全局变量
 * Created by zengpu on 15/10/17.
 */
public class MyApplication extends LitePalApplication {

    private static Context context;

    @Override
    public void onCreate() {

        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
