package com.weather.app.service;

import android.app.IntentService;
import android.content.Intent;

import com.weather.app.model.MyCityInformations;

/**
 * 初始化城市数据
 * Created by zengpu on 15/12/5.
 */
public class InitCityService extends IntentService {

    private MyCityInformations myCityInformations;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public InitCityService( ) {
        super("InitCityService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        myCityInformations = MyCityInformations.getInstance();
    }
}
