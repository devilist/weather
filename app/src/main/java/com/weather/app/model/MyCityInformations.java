package com.weather.app.model;

import com.weather.app.db.WeatherDB;

import java.util.ArrayList;
import java.util.List;

/**
 * 初始化变量cityIformations
 * Created by zengpu on 15/12/5.
 */
public class MyCityInformations {

    public  static  MyCityInformations myCityInformations;

    private  List<CityInformation> cityInformations = new ArrayList<>();

    private WeatherDB weatherDB;

    private MyCityInformations() {
        weatherDB = WeatherDB.getInstance();
        if (cityInformations.size()<=0) {
            cityInformations = weatherDB.loadCityInformation();
        }
    }

    public synchronized static MyCityInformations getInstance() {

        if (myCityInformations == null) {
            myCityInformations = new MyCityInformations();
        }
        return myCityInformations;
    }

    public  List<CityInformation> getCityInformations() {
        return cityInformations;
    }

    public  void setCityInformations(List<CityInformation> cityInformations) {
        this.cityInformations = cityInformations;
    }
}
