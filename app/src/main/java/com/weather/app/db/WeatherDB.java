package com.weather.app.db;

import android.database.sqlite.SQLiteDatabase;

import com.weather.app.model.CityInformation;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.List;

/**
 * 封装数据库操作
 * <p>Created by zengpu on 15/10/17.
 */
public class WeatherDB {

    private SQLiteDatabase db;

    private static WeatherDB weatherDB;

    private WeatherDB() {
        //创建数据库表格
        db= Connector.getDatabase();
    }

    public synchronized static WeatherDB getInstance() {
        if (weatherDB == null) {
            weatherDB = new WeatherDB();
        }
        return weatherDB;
    }

    /**
     * 存储城市信息
     * @param cityInformation city's information
     */
    public void saveCityInformation(CityInformation cityInformation) {

        if (cityInformation != null) {
            cityInformation.save();
        }
    }

    /**
     * 从数据库读取全国城市信息
     * @return List<CityInformation>
     */
    public List<CityInformation> loadCityInformation(){

        List<CityInformation> cityInformationsList =DataSupport.findAll(CityInformation.class);
        return cityInformationsList;
    }

    /**
     关闭数据库
     */
    public void destroyDB(){
        if (db != null) {
            db.close();
        }
    }
}
