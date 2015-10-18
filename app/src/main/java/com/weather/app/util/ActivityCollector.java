package com.weather.app.util;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * 活动管理器
 * <p>Created by zengpu on 15/10/17.
 */
public class ActivityCollector  {

    public static List<Activity> activities = new ArrayList<>();

    public static void addActivity(Activity activity) {

        if (!activities.contains(activity)) {
            activities.add(activity);
        }
    }

    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    public static void finishAll() {

        for (Activity activity :activities) {
            if (!activity.isFinishing()) {

                //销毁活动
                activity.finish();
            }
        }
    }
}
