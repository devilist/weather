package com.weather.app.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.weather.app.util.ActivityCollector;
import com.weather.app.util.LogUtil;

/**
 * Created by zengpu on 15/11/15.
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityCollector.addActivity(this);

        LogUtil.i("TAG","BaseActivity : " + getClass().getSimpleName() );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ActivityCollector.removeActivity(this);
    }
}
