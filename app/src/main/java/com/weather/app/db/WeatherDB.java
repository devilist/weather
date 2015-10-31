package com.weather.app.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.weather.app.R;
import com.weather.app.model.CityInformation;
import com.weather.app.util.LogUtil;
import com.weather.app.util.MyApplication;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
//        db = importDB();
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
     * 加载选中的城市
     * @return
     */
    public List<CityInformation> loadMyCities(){

        //加载所有selectedTag=1的城市,按升序排列

        List<CityInformation> mCities =DataSupport.where("selectedTag = ?","1")
                .order("selectedId asc")
                .find(CityInformation.class);
        return mCities;
    }

    /**
     * 标记选中的城市 ,将selectedTag属性置true;SelectedId属性置为maxSelectedId + 1
     * @param c CityInformation
     */
    public void markSelectedCity(CityInformation c){

        List<CityInformation> mCities = loadMyCities();

        int maxSelectedId;

        if (mCities.size() == 0) {
            maxSelectedId = -1;
        } else {
            maxSelectedId = mCities.get(mCities.size()-1).getSelectedId();
        }

        LogUtil.i("TAG","maxSelectedId is " + maxSelectedId);

        String district = c.getDistrict();
        ContentValues values = new ContentValues();
        values.put("selectedTag", "1");
        values.put("selectedId",maxSelectedId + 1);
        DataSupport.updateAll(CityInformation.class,
                values, "selectedTag = ? and district = ?", "0", district);
        DataSupport.updateAll(CityInformation.class,
                values,"selectedId = ? and district = ? ", "-1", district);
    }

    /**
     * 取消标记的城市,将selectedTag属性置false;SelectedId属性置为 -1
     * @param c
     */
    public void cancelSelectedCity(CityInformation c){

        String district = c.getDistrict();
        int selectedId = c.getSelectedId();
        ContentValues values = new ContentValues();
        values.put("selectedTag", "0");
        values.put("selectedId",-1);
        DataSupport.updateAll(CityInformation.class,
                values, "selectedTag = ? and district = ?", "1", district);
        DataSupport.updateAll(CityInformation.class,
                values,"selectedId = ? and district = ? ", String.valueOf(selectedId), district);
    }

    /**
     * 导入外部数据库
     * @return
     */
    public SQLiteDatabase importDB () {

        final int BUFFER_SIZE = 400000;
        final String DB_NAME = "weather.db"; //保存的数据库文件名
        final String PACKAGE_NAME = "com.weather.app";
        final String DB_PATH = "/data"
                + Environment.getDataDirectory().getAbsolutePath() + "/"
                + PACKAGE_NAME + "/databases";  //在手机里存放数据库的位置
        final String dbfile = DB_PATH + "/" + DB_NAME;
        try {
            if (!(new File(dbfile).exists())) {
                //判断数据库文件是否存在，若不存在则执行导入，否则直接打开数据库
                InputStream is = MyApplication.getContext().getResources().openRawResource(
                        R.raw.weather); //欲导入的数据库
                FileOutputStream fos = new FileOutputStream(dbfile);
                byte[] buffer = new byte[BUFFER_SIZE];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                fos.close();
                is.close();
            }
            SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(dbfile, null);
            return database;
        } catch (FileNotFoundException e) {
            Log.e("Database", "File not found");
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("Database", "IO exception");
            e.printStackTrace();
        }
        return null;
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
