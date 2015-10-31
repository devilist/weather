package com.weather.app.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;

import com.weather.app.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 初始化天气信息
 * Created by zengpu on 15/11/21.
 */
public class InitWeatherInfo {

    public  SharedPreferences prefs;

    public InitWeatherInfo(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * 获得基本天气信息
     * @return String[]
     */
    public  List<String> getBaseWeatherInfo(){

        List<String> baseWeatherInfo = new ArrayList<>();
//        temp.setText(prefs.getString("tmp0", ""));
        LogUtil.i("TAG","tmp0 is: " +prefs.getString("tmp0", "0"));
        baseWeatherInfo.add(prefs.getString("tmp0", "0"));
        LogUtil.i("TAG", "baseWeatherInfo0 is: " + baseWeatherInfo.get(0));

//        tempSymbol.setText("°");
        baseWeatherInfo.add("°C");
//        String condCode = prefs.getString("cond_code","");
        baseWeatherInfo.add(prefs.getString("cond_code","0"));
//        condition.setText(prefs.getString("cond_text", ""));
        baseWeatherInfo.add(prefs.getString("cond_text", "0"));
//        cityView.setText(prefs.getString("city_selected", ""));
        baseWeatherInfo.add(prefs.getString("city_selected", "9"));
//        updateTime.setText("update: " + prefs.getString("update_time", ""));
        baseWeatherInfo.add("update: " + prefs.getString("update_time", "0"));
//        mDay.setText(prefs.getString("mDay",""));
        baseWeatherInfo.add(prefs.getString("mDay","0"));

        return  baseWeatherInfo;
    }

    /**
     * 其他天气信息
     * @return
     */
    public  List<List<String>>  getOtherWeatherInfo(){

        List<List<String>> otherWeatherInfo = new ArrayList<>();

        List<String> itemName = new ArrayList<>();
        List<String> itemContent = new ArrayList<>();

        itemName.add("湿度(%)");
        itemName.add("能见度(km)");
        itemName.add(prefs.getString("wind0_dir","--"));
        itemName.add("降雨量(mm)");
        itemName.add("气压(mb)");
        itemName.add("体感(°C)");
        itemContent.add(prefs.getString("hum",""));
        itemContent.add(prefs.getString("vis", ""));
        itemContent.add(prefs.getString("wind0_sc",""));
        itemContent.add(prefs.getString("pcpn",""));
        itemContent.add(prefs.getString("pres", ""));
        itemContent.add(prefs.getString("tmp0",""));
        otherWeatherInfo.add(itemName);
        otherWeatherInfo.add(itemContent);

        return otherWeatherInfo;
    }

    /**
     * 未来七天天气日期,MM/dd
     */
    public  String[] getMySrcDates() {

        String[] mySrcDates = new String[7];

        for (int i = 0; i < 7; i++) {
            String dateString = "date" + i;

            mySrcDates[i] = Utility.transformDate(prefs.getString(dateString, "2000-01-01 00:00"),
                    "MM/dd");
        }
        return mySrcDates;
    }

    /**
     * 未来七天天气预报曲线X坐标
     */
    public  List<double[]> getxValues_dailyForecast() {

        List<double[]> xValues_dailyForecast = new ArrayList<>();

        double[] mDatesH = new double[]{0, 1, 2, 3, 4, 5, 6};
        double[] mDateL = mDatesH;
        xValues_dailyForecast.add(mDatesH);
        xValues_dailyForecast.add(mDateL);
        return xValues_dailyForecast;
    }

    /**
     * 未来七天天气预报曲线y坐标
     */
    public  List<double[]> getyValues_dailyForecast() {

        List<double[]> yValues_dailyForecast = new ArrayList<>();

        double[] mTempH = new double[7];
        double[] mTempL = new double[7];
        for (int i = 0; i < 7; i++) {
            String tempMaxString = "temp_max" + i;
            String tempMinString = "temp_min" + i;
            mTempH[i] = Double.parseDouble(prefs.getString(tempMaxString, "15"));
            mTempL[i] = Double.parseDouble(prefs.getString(tempMinString, "15"));
            LogUtil.i("TAG", "mTempH " + i + "is: " + mTempH[i]);
            LogUtil.i("TAG", "mTempL " + i + "is: " + mTempL[i]);
        }
        yValues_dailyForecast.add(mTempH);
        yValues_dailyForecast.add(mTempL);
        return yValues_dailyForecast;
    }

    /**
     * 未来七天天气预报曲线标题
     */
    public  String[] getTitles() {

        String[] titles = new String[]{"H", "L"};

        return titles;
    }

    /**
     * 未来五天天气信息
     */
    public  List<ArrayList<String>> getDailyForecastWeatherInfo() {

        List<ArrayList<String>> dailyForecastWeatherInfo = new ArrayList<>();

        ArrayList<String> dateList = new ArrayList<>();
        ArrayList<String> mCodeList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            String dateString = "date" + i;
            String condCodeString_d = "cond_code_d" + i;
            String date = Utility.transformDate(prefs.getString(dateString, "2000-01-01 00:00"),
                    "MM/dd");
            String condCode_d = prefs.getString(condCodeString_d, "100");
            dateList.add(date);
            mCodeList.add(condCode_d);
            LogUtil.i("TAG", "code is: " + condCode_d);
            LogUtil.i("TAG", "date is: " + date);
        }
        dailyForecastWeatherInfo.add(dateList);
        dailyForecastWeatherInfo.add(mCodeList);

        return dailyForecastWeatherInfo;
    }

    /**
     * 未来24小时天气预报时间
     * @return
     */
    public  String[] getMySrcHours() {

//        int size = prefs.getInt("size",7);
        String[] mySrcHours = new String[7];

        for (int i = 0; i < 7; i++) {
            String hour = "hour" + i;
            mySrcHours[i] = Utility.transformDate(prefs.getString(hour, "2000-01-01 00:00"),
                    "HH:mm");
        }
        return mySrcHours;
    }

    /**
     * 未来24小时天气预报x坐标
     */
    public List<double[]> getxValues_hourlyForecast() {

        List<double[]> xValues_dailyForecast = new ArrayList<>();
        double[] mX = new double[]{0, 1, 2, 3, 4, 5, 6, 7, 8};
        xValues_dailyForecast.add(mX);

        return xValues_dailyForecast;
    }

    /**
     * 未来24小时天气预报Y坐标
     */
    public List<double[]> getyValues_hourlyForecast() {

        List<double[]> yValues_hourlyForecast = new ArrayList<>();
        int size = prefs.getInt("size",1);

        double[] mHours = new double[7+2];
        mHours[0] = Double.parseDouble(prefs.getString("hourly_tmp0", "0"));
        for (int i = 0; i < size; i++) {
            String hourly_tmp = "hourly_tmp" + i;
            mHours[i+1] = Double.parseDouble(prefs.getString(hourly_tmp,
                    String.valueOf((int)mHours[i]+(int)(Math.random()*2)-(int)(Math.random()*2))));
            LogUtil.i("TAG", "mTempH " + i + "is: " + mHours[i]);
        }
        if (size < 7) {
            for (int i = size +1 ; i < 9; i++) {

                double max = Double.parseDouble(prefs.getString("temp_max1", "15"));
                double min = Double.parseDouble(prefs.getString("temp_min1", "15"));
                mHours[i] = (int)((max + min)/2) + (int)(Math.random()*2)-(int)(Math.random()*2);
            }
        } else {
            mHours[7+2-1] = mHours[7];
//            mHours[7+2-1] = Double.parseDouble(prefs.getString("hourly_tmp6",
//                    String.valueOf((int)mHours[7+2-2]+(int)(Math.random()*2)-(int)(Math.random()*2))));
        }

        yValues_hourlyForecast.add(mHours);
        return yValues_hourlyForecast;
    }

    public  String[] getTitles1() {

        String[] titles = new String[]{"H"};

        return titles;
    }

    /**
     * 未来24小时天气预报信息
     * @return
     */
    public  List<String> getHourlyForecastHours() {

        List<String> hours = new ArrayList<>();
        int size = prefs.getInt("size",0);

        LogUtil.i("TAG","size is : " + size);
        int number = 22;

        for (int i = 0; i < size; i++) {
            String hour = "hour" + i;
            number = 22 + i*3 - 24;
            String format = "2000-01-01 "+ number +":00";
            hours.add(Utility.transformDate(prefs.getString(hour, format),
                    "HH:mm"));
        }
        if (size < 7) {
            for (int i = size; i < 7; i++) {
                number = 1 + (i-size)*3;
//                String hour = "hour" + i;
//                String format = "2000-01-01 "+ number +":00";
//                hours.add(Utility.transformDate(prefs.getString(hour, format),
//                        "HH:mm"));
                String format ;
                if (number<10) {
                    format = "0"+ number +":00";
                } else {
                    format = number +":00";
                }
                hours.add(format);
                LogUtil.i("TAG", "munber is : " + number);
                LogUtil.i("TAG", "hour is : " + format);
            }
        }
        return hours;
    }

    /**
     * 生活指数
     * @return
     */
    public List<List<String>> getSuggestion() {

        List<String> suggestionItem = new ArrayList<>();
        List<String> brf = new ArrayList<>();
        List<String> txt = new ArrayList<>();
        List<List<String>> suggestion = new ArrayList<>();

        suggestionItem.add("穿衣指数");
        suggestionItem.add("感冒指数");
        suggestionItem.add("紫外线指数");
        suggestionItem.add("运动指数");
        suggestionItem.add("旅游指数");
        suggestionItem.add("洗车指数");

        brf.add(prefs.getString("drsg_brf", "--"));
        brf.add(prefs.getString("flu_brf", "--"));
        brf.add(prefs.getString("uv_brf", "--"));
        brf.add(prefs.getString("sport_brf", "--"));
        brf.add(prefs.getString("trav_brf", "--"));
        brf.add(prefs.getString("cw_brf", "--"));

        txt.add(prefs.getString("drsg_txt", "啊呀,服务器崩溃了..."));
        txt.add(prefs.getString("flu_txt", "啊呀,服务器崩溃了..."));
        txt.add(prefs.getString("uv_txt", "啊呀,服务器崩溃了..."));
        txt.add(prefs.getString("sport_txt", "啊呀,服务器崩溃了..."));
        txt.add(prefs.getString("trav_txt", "啊呀,服务器崩溃了..."));
        txt.add(prefs.getString("cw_txt", "啊呀,服务器崩溃了..."));



        suggestion.add(suggestionItem);
        suggestion.add(brf);
        suggestion.add(txt);

        return suggestion;
    }

    /**
     * 生活指数图标
     * @return
     */
    public List<Drawable> getSuggestionitemImag() {
        List<Drawable> images = new ArrayList<>();
        images.add(MyApplication.getContext().getResources().getDrawable(R.drawable.drsg));
        images.add(MyApplication.getContext().getResources().getDrawable(R.drawable.flu));
        images.add(MyApplication.getContext().getResources().getDrawable(R.drawable.uv));
        images.add(MyApplication.getContext().getResources().getDrawable(R.drawable.sport));
        images.add(MyApplication.getContext().getResources().getDrawable(R.drawable.trav));
        images.add(MyApplication.getContext().getResources().getDrawable(R.drawable.cw));

        return  images;

    }

}
