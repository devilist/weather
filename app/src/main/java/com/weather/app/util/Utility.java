package com.weather.app.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.weather.app.R;
import com.weather.app.db.WeatherDB;
import com.weather.app.model.AllCities;
import com.weather.app.model.CityInformation;
import com.weather.app.model.WeatherResult;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by zengpu on 15/10/17.
 */
public class Utility {

    public final static String TAG = "UTILITY";

    /**
     * 用Gson解析服务器返回的城市列表数据,存储到本地
     * @param weatherDB 数据库
     * @param response 返回值
     * @return boolean
     */
    public synchronized static int handleCityInfoResponse(
            WeatherDB weatherDB, String response) {

        int progress = 0;

        if (!TextUtils.isEmpty(response)) {
            Gson gson = new Gson();
            AllCities allCities = gson.fromJson(response, AllCities.class);
            List<AllCities.CityInfo> cityList = allCities.getData();
            if (cityList != null && cityList.size() > 0) {
                for (AllCities.CityInfo c : cityList) {
                    CityInformation cityInformation = new CityInformation();
                    cityInformation.setAreaid(c.getAreaid());
                    cityInformation.setDistrict(c.getDistrict());
                    cityInformation.setCity(c.getCity());
                    cityInformation.setProv(c.getProv());
                    LogUtil.i(TAG, "cityinfo is " + cityInformation.toString());

                    weatherDB.saveCityInformation(cityInformation);
                    progress++;
                }
            }
        }
        return progress;
    }

    /**
     * 用Gson解析服务器返回的Json数据,将解析的数据储存到本地
     * @param context
     * @param response
     */
    public static void handleWeatherResponse(Context context,String response) {

        try {
            LogUtil.i(TAG, "response  is ------------> " + response);
            Gson gson = new Gson();
            WeatherResult weatherResult = gson.fromJson(response, WeatherResult.class);
            List<WeatherResult.WeatherInfo> heWeatherResult = weatherResult.getHeWeatherResult();
            WeatherResult.WeatherInfo weatherInfo = heWeatherResult.get(0);

            String city = weatherInfo.getBasic().getCity();

            LogUtil.i(TAG,"city is " + city);

            LogUtil.i(TAG, "weatherInfo  is ------------> " + weatherInfo.toString());

            saveWeatherInfo(context, weatherInfo);
        } catch (Exception e) {
            LogUtil.e(TAG, "--------------->" + e);
        }
    }

    /**
     * 存储服务器返回的天气信息
     * @param context
     * @param info
     */
    public static void saveWeatherInfo(Context context, WeatherResult.WeatherInfo info){

        SharedPreferences.Editor editor = PreferenceManager
                                                .getDefaultSharedPreferences(context).edit();

//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.CHINA);
//        String strDate= info.getDaily_forecast().get(0).getDate();
//        Date date1=transformDate(strDate);
//        sdf.format(transformDate(info.getDaily_forecast().get(0).getDate()));


        String updateTime = info.getBasic().getUpdate().getLoc();
        String[] result = updateTime.split(" ");
        LogUtil.i(TAG, "S2 is " + result[1]);

        //basic 城市基本信息
        editor.putString("city_selected",info.getBasic().getCity());
        editor.putString("CityID",info.getBasic().getId());
        editor.putString("city_lat",info.getBasic().getLat());
        editor.putString("city_lon",info.getBasic().getLon());
        editor.putString("update_time",result[1]);
        editor.putString("mDay",myWeekDay());
        //now 实时天气
        editor.putString("tmp0", info.getNow().getTmp());
        LogUtil.i(TAG, "tmp0 is "+ info.getNow().getTmp());
        editor.putString("wind0_dir",info.getNow().getWind().getDir());//风向
        editor.putString("wind0_sc",info.getNow().getWind().getSc());//风力等级
        editor.putString("wind0_spd",info.getNow().getWind().getSpd());//风速kmph
        editor.putString("wind0_deg",info.getNow().getWind().getDeg());//风向角度
        editor.putString("cond_code",info.getNow().getCond().getCode());//天气代码
        editor.putString("cond_text",info.getNow().getCond().getTxt());//天气描述
        editor.putString("hum",info.getNow().getHum());//湿度%
        editor.putString("pcpn",info.getDaily_forecast().get(0).getPcpn());//降雨量mm
        editor.putString("vis", info.getDaily_forecast().get(0).getVis());//能见度km
        editor.putString("pres",info.getDaily_forecast().get(0).getPres());//气压
        editor.putString("fl0", info.getNow().getFl());//体感温度

        // daily_forecast 未来天气
        for (int i = 0; i < info.getDaily_forecast().size(); i++) {
            String date = "date" + i;
            String temp_min = "temp_min" + i;
            String temp_max = "temp_max" + i;
            String cond_code_d = "cond_code_d" + i;
            String cond_code_n = "cond_code_n" + i;
            String pop = "pop" + i;

            editor.putString(date,info.getDaily_forecast().get(i).getDate());
            editor.putString(temp_min,info.getDaily_forecast().get(i).getTmp().getMin());
            editor.putString(temp_max,info.getDaily_forecast().get(i).getTmp().getMax());
            editor.putString(cond_code_d,info.getDaily_forecast().get(i).getCond().getCode_d());//白天天气代码
            editor.putString(cond_code_n,info.getDaily_forecast().get(i).getCond().getCode_n());//夜间天气代码
            editor.putString(pop,info.getDaily_forecast().get(i).getPop());//降水概率
        }

        //hourly_forecast
        for (int i = 0; i < info.getHourly_forecast().size(); i++ ) {

            String hour = "hour" + i;
            String hourly_tmp = "hourly_tmp" + i;

            editor.putString(hour,info.getHourly_forecast().get(i).getDate());
            editor.putString(hourly_tmp,info.getHourly_forecast().get(i).getTmp());

        }
        editor.putInt("size",info.getHourly_forecast().size());

        //suggestion 生活指数
        //舒适指数
        editor.putString("comf_brf",info.getSuggestion().getComf().getBrf());
        editor.putString("comf_txt",info.getSuggestion().getComf().getTxt());
        //穿衣指数
        editor.putString("drsg_brf",info.getSuggestion().getDrsg().getBrf());
        editor.putString("drsg_txt",info.getSuggestion().getDrsg().getTxt());
        //紫外线指数uv
        editor.putString("uv_brf",info.getSuggestion().getUv().getBrf());
        editor.putString("uv_txt",info.getSuggestion().getUv().getTxt());
        //洗车指数cw
        editor.putString("cw_brf",info.getSuggestion().getCw().getBrf());
        editor.putString("cw_txt",info.getSuggestion().getCw().getTxt());
        //旅游指数trav
        editor.putString("trav_brf",info.getSuggestion().getTrav().getBrf());
        editor.putString("trav_txt",info.getSuggestion().getTrav().getTxt());
        //感冒指数flu
        editor.putString("flu_brf",info.getSuggestion().getFlu().getBrf());
        editor.putString("flu_txt",info.getSuggestion().getFlu().getTxt());
        //运动指数sport
        editor.putString("sport_brf",info.getSuggestion().getSport().getBrf());
        editor.putString("sport_txt",info.getSuggestion().getSport().getTxt());

        //status
        //   ok	接口正常
        //   invalid key	错误的用户 key
        //   unknown city	未知城市
        //   no more requests	超过访问次数
        //   anr	服务无响应或超时
        //   permission denied	没有访问权限
        editor.putString("status",info.getStatus());

        editor.commit();
    }

    /**
     * 显示周几
     * @return
     */
    public static String myWeekDay() {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String mDay = String.valueOf(calendar.get(Calendar.DAY_OF_WEEK));

        if ("1".equals(mDay)){
            mDay = "Sunday";
        } else if ("2".equals(mDay)) {
            mDay = "Monday";
        } else if ("3".equals(mDay)) {
            mDay = "Tuesday";
        } else if ("4".equals(mDay)) {
            mDay = "Wednesday";
        } else if ("5".equals(mDay)) {
            mDay = "Thursday";
        } else if ("6".equals(mDay)) {
            mDay = "Friday";
        } else if ("7".equals(mDay)) {
            mDay = "Saturday";
        }
        return mDay;
    }


    /**
     * 转换日期格式
     * @param strDate
     * @return
     */
    public  static String transformDate(String strDate, String format){

//        String s = "2015-10-13";
//        String s1 ="2015-10-13 22:00";
//        LogUtil.i("TAG","s 长度:"+s.length());
//        LogUtil.i("TAG", "s1 长度:" + s1.length());
        Date date = new Date();
//        SimpleDateFormat sdf0 = new SimpleDateFormat("MM/dd", Locale.CHINA);
        SimpleDateFormat sdf0 = new SimpleDateFormat(format, Locale.CHINA);
        SimpleDateFormat sdf1= new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2= new SimpleDateFormat("yyyy-MM-dd HH:mm");
        //必须捕获异常
        try
        {
            if (strDate.length()<11){
                date =sdf1.parse(strDate);
            } else {
                date =sdf2.parse(strDate);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return sdf0.format(date);
    }

    /**
     * 初始化ImageView,设置天气图标
     * @param code
     * @param imageView
     */
    public static void setCondImage(String code,ImageView imageView){

        switch (code) {
            case "100":
                imageView.setImageResource(R.drawable.weather_100);
                break;
            case "101":
                imageView.setImageResource(R.drawable.weather_101);
                break;
            case "102": case "104":
                imageView.setImageResource(R.drawable.weather_102_104);
                break;
            case "103":
                imageView.setImageResource(R.drawable.weahter_103);
                break;
            case "200": case "201": case "202": case "203": case "204":
                imageView.setImageResource(R.drawable.weather_201_to_204);
                break;
            case "205": case "206": case "207":
                imageView.setImageResource(R.drawable.weather_205_to_207);
                break;
            case "208": case "209": case "210":
            case "211": case "212": case "213":
                imageView.setImageResource(R.drawable.weather_208_to_213);
                break;
            case "300":
                imageView.setImageResource(R.drawable.weather_300);
                break;
            case "301":
                imageView.setImageResource(R.drawable.weather_301);
                break;
            case "302":
                imageView.setImageResource(R.drawable.weather_302);
                break;
            case "303":
                imageView.setImageResource(R.drawable.weather_303);
                break;
            case "304":
                imageView.setImageResource(R.drawable.weather_304);
                break;
            case "305":
                imageView.setImageResource(R.drawable.weather_305);
                break;
            case "306":
                imageView.setImageResource(R.drawable.weather_306);
                break;
            case "307":
                imageView.setImageResource(R.drawable.weather_307);
                break;
            case "308": case "312":
                imageView.setImageResource(R.drawable.weather_308_312);
                break;
            case "309":
                imageView.setImageResource(R.drawable.weather_309);
                break;
            case "310": case "311":
                imageView.setImageResource(R.drawable.weather_310_311);
                break;
            case "313":
                imageView.setImageResource(R.drawable.weather_313);
                break;
            case "400":
                imageView.setImageResource(R.drawable.weather_400);
                break;
            case "401":
                imageView.setImageResource(R.drawable.weather_401);
                break;
            case "402":
                imageView.setImageResource(R.drawable.weather_402);
                break;
            case "403":
                imageView.setImageResource(R.drawable.weather_403);
                break;
            case "404": case "405":
                imageView.setImageResource(R.drawable.weather_404_405);
                break;
            case "406":
                imageView.setImageResource(R.drawable.weather_406);
                break;
            case "407":
                imageView.setImageResource(R.drawable.weather_407);
                break;
            case "500": case "501":
                imageView.setImageResource(R.drawable.weather_500_501);
                break;
            case "502":
                imageView.setImageResource(R.drawable.weather_502);
                break;
            case "503": case "504": case "505": case "506":
                imageView.setImageResource(R.drawable.weather_503_to_506);
                break;
            case "507":
                imageView.setImageResource(R.drawable.weather_507);
                break;
            case "508":
                imageView.setImageResource(R.drawable.weather_508);
                break;
            case "900":
                imageView.setImageResource(R.drawable.weather_900);
                break;
            case "901":
                imageView.setImageResource(R.drawable.weather_901);
                break;
        }
//        imageView.setAlpha(0.55f);
    }

//    /**
//     * 初始化天气背景
//     * @param code
//     * @param layout
//     */
//    public static void setBgColor(String code,View layout) {
//
//        switch (code) {
//            case "100":
//                layout.setBackgroundResource(R.drawable.weather_bg_sun);
//                break;
//            case "104":
//                layout.setBackgroundResource(R.drawable.weather_bg_no_cloudy);
//                break;
//            case "101": case "102": case "103":  case "200": case "201":
//            case "202": case "203": case "204":
//                layout.setBackgroundResource(R.drawable.weather_bg_cloudy);
//                break;
//            case "205": case "206": case "207": case "208":  case "209":
//            case "210": case "211": case "212":  case "213":
//                layout.setBackgroundResource(R.drawable.weather_bg_wind);
//                break;
//            case "300": case "301": case "302": case "303": case "304":
//            case "305": case "306": case "307": case "308": case "309":
//            case "310": case "311": case "312": case "313":
//                layout.setBackgroundResource(R.drawable.weather_bg_rain);
//                break;
//            case "400": case "401": case "402": case "403": case "404":
//            case "405": case "406": case "407": case "500": case "501":
//            case "502": case "503": case "504": case "506": case "507":
//            case "508":
//                layout.setBackgroundResource(R.drawable.weather_bg_frog);
//                break;
//            case "900": case "901": case "999": default:
//                layout.setBackgroundResource(R.drawable.weather_bg_sun);
//                break;
//        }
//    }

    /**
     * 查询天气代码
     * @param text
     * @return
     */
    public static String getWeatherCode(String text) {

        String code ;
        switch (text) {
            case "晴":
                code = "100";
                break;
            case "多云":
                code = "101";
                break;
            case "少云":
                code = "102";
                break;
            case "晴间多云":
                code = "103";
                break;
            case "阴":
                code = "104";
                break;
            case "有风":
                code = "200";
                break;
            case "平静":
                code = "201";
                break;
            case "微风":
                code = "202";
                break;
            case "和风":
                code = "203";
                break;
            case "清风":
                code = "204";
                break;
            case "强风/劲风":
                code = "205";
                break;
            case "疾风":
                code = "206";
                break;
            case "大风":
                code = "207";
                break;
            case "烈风":
                code = "208";
                break;
            case "风暴":
                code = "209";
                break;
            case "狂风暴":
                code = "210";
                break;
            case "飓风":
                code = "211";
                break;
            case "龙卷风":
                code = "212";
                break;
            case "热带风暴":
                code = "213";
                break;
            case "阵雨":
                code = "300";
                break;
            case "强阵雨":
                code = "301";
                break;
            case "雷阵雨":
                code = "302";
                break;
            case "强雷阵雨":
                code = "303";
                break;
            case "雷阵雨伴有冰雹":
                code = "304";
                break;
            case "小雨":
                code = "306";
                break;
            case "大雨":
                code = "307";
                break;
            case "极端降雨":
                code = "308";
                break;
            case "毛毛雨/细雨":
                code = "309";
                break;
            case "暴雨":
                code = "310";
                break;
            case "大暴雨":
                code = "311";
                break;
            case "特大暴雨":
                code = "312";
                break;
            case "冻雨":
                code = "313";
                break;
            case "小雪":
                code = "400";
                break;
            case "中雪":
                code = "401";
                break;
            case "大雪":
                code = "402";
                break;
            case "暴雪":
                code = "403";
                break;
            case "雨夹雪":
                code = "404";
                break;
            case "雨雪天气":
                code = "405";
                break;
            case "阵雨夹雪":
                code = "406";
                break;
            case "阵雪":
                code = "407";
                break;
            case "薄雾":
                code = "500";
                break;
            case "雾":
                code = "501";
                break;
            case "霾":
                code = "502";
                break;
            case "扬沙":
                code = "503";
                break;
            case "浮尘":
                code = "504";
                break;
            case "火山灰":
                code = "506";
                break;
            case "沙尘暴":
                code = "507";
                break;
            case "热":
                code = "900";
                break;
            case "冷":
                code = "901";
                break;
            case "未知":
                code = "999";
                break;
            default:
                code = "999";
        }
        return code;
    }

    /**
     * dp转px
     *
     * @param context
     * @param dpVal
     * @return
     */
    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }


    /**
     * 判断是否存在 NavigationBar
     * @param context
     * @return
     */
    public static boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
            Log.w(TAG, e);
        }

        return hasNavigationBar;
    }

    /**
     * 获取NavigationBar的高度
     * @param context
     * @return
     */
    public static int getNavigationBarHeight(Context context) {
        int navigationBarHeight = 0;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("navigation_bar_height", "dimen", "android");
        if (id > 0 && checkDeviceHasNavigationBar(context)) {
            navigationBarHeight = rs.getDimensionPixelSize(id);
        }
        return navigationBarHeight;
    }

}


