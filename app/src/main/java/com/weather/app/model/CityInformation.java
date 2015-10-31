package com.weather.app.model;

import org.litepal.crud.DataSupport;

/**
 * Created by zengpu on 15/10/25.
 */
public class CityInformation extends DataSupport{

    /**
     * areaid : 101010100
     * prov : 北京
     * city : 北京
     * district : 北京
     */

    private int areaid;
    private String prov;
    private String city;
    private String district;

    public void setAreaid(int areaid) {
        this.areaid = areaid;
    }

    public void setProv(String prov) {
        this.prov = prov;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public int getAreaid() {
        return areaid;
    }

    public String getProv() {
        return prov;
    }

    public String getCity() {
        return city;
    }

    public String getDistrict() {
        return district;
    }

    @Override
    public String toString() {
        if (prov.equals("北京")|prov.equals("上海")|prov.equals("天津")|prov.equals("重庆")) {
            if (city.equals(district)) {
                return district;
            }
            return district + " - " + prov;
        }
        if (city.equals(district)) {
            return district + " - " + prov;
        }
            return district + " - " + city + " - " + prov;
    }
}

