package com.weather.app.util;

/**
 * Created by zengpu on 15/10/17.
 */
public interface HttpCallbackListener {

    void onFinish(String response);

    void onError(Exception e);
}
