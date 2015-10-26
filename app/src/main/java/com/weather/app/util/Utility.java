package com.weather.app.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.weather.app.db.WeatherDB;
import com.weather.app.model.AllCities;
import com.weather.app.model.CityInformation;
import com.weather.app.model.WeatherResult;

import java.util.List;

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
    public synchronized static boolean handleCityInfoResponse(
            WeatherDB weatherDB, String response) {

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
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 用Gson解析服务器返回的Json数据,将解析的数据储存到本地
     */
    public static void handleWeatherResponse(Context context,String response) {

        try {
            LogUtil.i(TAG, "response  is ------------> " + response);
            Gson gson = new Gson();
            WeatherResult weatherResult = gson.fromJson(response, WeatherResult.class);
            List<WeatherResult.WeatherInfo> heWeatherResult = weatherResult.getHeWeatherResult();
            WeatherResult.WeatherInfo weatherInfo = heWeatherResult.get(0);

            LogUtil.i(TAG, "weatherInfo  is ------------> " + weatherInfo.toString());

            saveWeatherInfo(context,weatherInfo);
        } catch (Exception e) {
            LogUtil.e(TAG,"--------------->" + e);
        }
    }

    public static void saveWeatherInfo(Context context, WeatherResult.WeatherInfo info){

        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();

        //basic 城市基本信息
        editor.putString("city_selected",info.getBasic().getCity());
        editor.putString("CityID",info.getBasic().getId());
        editor.putString("city_lat",info.getBasic().getLat());
        editor.putString("city_lon",info.getBasic().getLon());
        editor.putString("update_time",info.getBasic().getUpdate().getLoc());

        //now 实时天气
        editor.putString("tmp0",info.getNow().getTmp());
        editor.putString("fl0",info.getNow().getFl());//体感温度
        editor.putString("wind0_dir",info.getNow().getWind().getDir());//风向
        editor.putString("wind0_sc",info.getNow().getWind().getSc());//风力等级
        editor.putString("wind0_spd",info.getNow().getWind().getSpd());//风速kmph
        editor.putString("wind0_deg",info.getNow().getWind().getDeg());//风向角度
        editor.putString("cond_code",info.getNow().getCond().getCode());//天气代码
        editor.putString("cond_text",info.getNow().getCond().getTxt());//天气描述
        editor.putString("hum",info.getNow().getHum());//湿度%
        editor.putString("pcpn",info.getNow().getPcpn());//降雨量mm
        editor.putString("vis",info.getNow().getVis());//能见度km
        editor.putString("pres",info.getNow().getPres());//气压

        //daily_forecast 未来天气
        editor.putString("date0",info.getDaily_forecast().get(0).getDate());
        editor.putString("temp_min0",info.getDaily_forecast().get(0).getTmp().getMin());
        editor.putString("temp_max0",info.getDaily_forecast().get(0).getTmp().getMax());
        editor.putString("cond_code_d0",info.getDaily_forecast().get(0).getCond().getCode_d());//白天天气代码
        editor.putString("cond_code_n0",info.getDaily_forecast().get(0).getCond().getCode_n());//夜间天气代码
        editor.putString("pop0",info.getDaily_forecast().get(0).getPop());//降水概率


        editor.putString("date1",info.getDaily_forecast().get(1).getDate());
        editor.putString("temp_min1",info.getDaily_forecast().get(1).getTmp().getMin());
        editor.putString("temp_max1",info.getDaily_forecast().get(1).getTmp().getMax());
        editor.putString("cond_code_d1",info.getDaily_forecast().get(1).getCond().getCode_d());//白天天气代码
        editor.putString("cond_code_n1",info.getDaily_forecast().get(1).getCond().getCode_n());//夜间天气代码
        editor.putString("pop1",info.getDaily_forecast().get(1).getPop());//降水概率

        editor.putString("date2",info.getDaily_forecast().get(2).getDate());
        editor.putString("temp_min2",info.getDaily_forecast().get(2).getTmp().getMin());
        editor.putString("temp_max2",info.getDaily_forecast().get(2).getTmp().getMax());
        editor.putString("cond_code_d2",info.getDaily_forecast().get(2).getCond().getCode_d());//白天天气代码
        editor.putString("cond_code_n2",info.getDaily_forecast().get(2).getCond().getCode_n());//夜间天气代码
        editor.putString("pop2",info.getDaily_forecast().get(2).getPop());//降水概率

        editor.putString("date3",info.getDaily_forecast().get(3).getDate());
        editor.putString("temp_min3",info.getDaily_forecast().get(3).getTmp().getMin());
        editor.putString("temp_max3",info.getDaily_forecast().get(3).getTmp().getMax());
        editor.putString("cond_code_d3",info.getDaily_forecast().get(3).getCond().getCode_d());//白天天气代码
        editor.putString("cond_code_n3",info.getDaily_forecast().get(3).getCond().getCode_n());//夜间天气代码
        editor.putString("pop3",info.getDaily_forecast().get(3).getPop());//降水概率

        editor.putString("date4",info.getDaily_forecast().get(4).getDate());
        editor.putString("temp_min4",info.getDaily_forecast().get(4).getTmp().getMin());
        editor.putString("temp_max4",info.getDaily_forecast().get(4).getTmp().getMax());
        editor.putString("cond_code_d4",info.getDaily_forecast().get(4).getCond().getCode_d());//白天天气代码
        editor.putString("cond_code_n4",info.getDaily_forecast().get(4).getCond().getCode_n());//夜间天气代码
        editor.putString("pop4",info.getDaily_forecast().get(4).getPop());//降水概率

        editor.putString("date5",info.getDaily_forecast().get(5).getDate());
        editor.putString("temp_min5",info.getDaily_forecast().get(5).getTmp().getMin());
        editor.putString("temp_max5",info.getDaily_forecast().get(5).getTmp().getMax());
        editor.putString("cond_code_d5",info.getDaily_forecast().get(5).getCond().getCode_d());//白天天气代码
        editor.putString("cond_code_n5",info.getDaily_forecast().get(5).getCond().getCode_n());//夜间天气代码
        editor.putString("pop5",info.getDaily_forecast().get(5).getPop());//降水概率

        editor.putString("date0",info.getDaily_forecast().get(6).getDate());
        editor.putString("temp_min6",info.getDaily_forecast().get(6).getTmp().getMin());
        editor.putString("temp_max6",info.getDaily_forecast().get(6).getTmp().getMax());
        editor.putString("cond_code_d6",info.getDaily_forecast().get(6).getCond().getCode_d());//白天天气代码
        editor.putString("cond_code_n6",info.getDaily_forecast().get(6).getCond().getCode_n());//夜间天气代码
        editor.putString("pop6",info.getDaily_forecast().get(6).getPop());//降水概率

        editor.putString("date7",info.getDaily_forecast().get(7).getDate());
        editor.putString("temp_min7",info.getDaily_forecast().get(7).getTmp().getMin());
        editor.putString("temp_max7",info.getDaily_forecast().get(7).getTmp().getMax());
        editor.putString("cond_code_d7",info.getDaily_forecast().get(7).getCond().getCode_d());//白天天气代码
        editor.putString("cond_code_n7",info.getDaily_forecast().get(7).getCond().getCode_n());//夜间天气代码
        editor.putString("pop7",info.getDaily_forecast().get(7).getPop());//降水概率

        //suggestion 生活指数
        //舒适指数
        editor.putString("comf_brf",info.getSuggestion().getComf().getBrf());
        editor.putString("comf_txt",info.getSuggestion().getComf().getTxt());
        //穿衣指数
        editor.putString("drsg_brf",info.getSuggestion().getDrsg().getBrf());
        editor.putString("drsg_txt",info.getSuggestion().getDrsg().getTxt());
        //紫外线指数uv
        editor.putString("drsg_brf",info.getSuggestion().getUv().getBrf());
        editor.putString("drsg_txt",info.getSuggestion().getUv().getTxt());
        //洗车指数cw
        editor.putString("drsg_brf",info.getSuggestion().getCw().getBrf());
        editor.putString("drsg_txt",info.getSuggestion().getCw().getTxt());
        //旅游指数trav
        editor.putString("drsg_brf",info.getSuggestion().getTrav().getBrf());
        editor.putString("drsg_txt",info.getSuggestion().getTrav().getTxt());
        //感冒指数flu
        editor.putString("drsg_brf",info.getSuggestion().getFlu().getBrf());
        editor.putString("drsg_txt",info.getSuggestion().getFlu().getTxt());
        //运动指数sport
        editor.putString("drsg_brf",info.getSuggestion().getSport().getBrf());
        editor.putString("drsg_txt",info.getSuggestion().getSport().getTxt());

        //status
        //   ok	接口正常
        //   invalid key	错误的用户 key
        //   unknown city	未知城市
        //   no more requests	超过访问次数
        //   anr	服务无响应或超时
        //   permission denied	没有访问权限
        editor.putString("status",info.getStatus());
    }
}


