package com.weather.app.model;

import java.util.List;

/**
 * Created by zengpu on 15/10/25.
 */
public class AllCities {

    /**
     * status : 200
     * message : OK
     * data : []
     */

    private int status;
    private String message;
    private List<CityInfo> data;

    public void setStatus(int status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(List<CityInfo> data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<CityInfo> getData() {
        return data;
    }

    public static class CityInfo {
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

        private boolean isSelected = false;

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

        public boolean isSelected() {
            return isSelected;
        }

        public void setIsSelected(boolean isSelected) {
            this.isSelected = isSelected;
        }

    }

}
