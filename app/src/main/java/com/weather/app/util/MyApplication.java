package com.weather.app.util;

import android.content.Context;
import android.content.Intent;

import com.weather.app.service.InitCityService;

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

        OverrideFonts.replaceFont(context, "SERIF", "fonts/NotoSansCJKsc-Thin.otf");

        //开启服务,初始化城市数据 cityinformations,(bug,3秒卡滞)
        Intent intentService = new Intent(this, InitCityService.class);
        startService(intentService);
    }

    public static Context getContext() {
        return context;
    }

}
