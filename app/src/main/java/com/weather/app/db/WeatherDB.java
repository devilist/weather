package com.weather.app.db;

import android.database.sqlite.SQLiteDatabase;

import com.weather.app.model.City;
import com.weather.app.model.County;
import com.weather.app.model.Province;

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
     * province 实例存储到数据库
     */
    public void saveProvince(Province province){
        if (province != null) {
            province.save();
        }
    }

    /**
     * 从数据库读取全国省份
     */
    public List<Province> loadProvinces() {
        List<Province> provinceList = DataSupport.findAll(Province.class);
        return provinceList;
    }

    /**
     * city实例存储到数据库
     */
    public void saveCity(City city){
        if (city != null) {
            city.save();
        }
    }

    /**
     * 从数据库读取城市信息
     */
    public List<City> loadCities() {
        List<City> cityList = DataSupport.findAll(City.class);
        return cityList;
    }

    /**
     * county实例存储到数据库
     */
    public void saveCounty(County county){
        if (county != null) {
            county.save();
        }
    }

    /**
     * 从数据库读取县镇信息
     */
    public List<County> loadCounties() {
        List<County> countyList = DataSupport.findAll(County.class);
        return countyList;
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
