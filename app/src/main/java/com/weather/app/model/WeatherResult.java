package com.weather.app.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 解析返回的Json格式的天气数据
 * <p>Created by zengpu on 15/10/17.
 */
public class WeatherResult {


    /**
     * HeWeather data service 3.0
     */

    @SerializedName("HeWeather data service 3.0")
    private List<WeatherInfo> HeWeatherResult;

    public void setHeWeatherResult(List<WeatherInfo> HeWeatherResult) {
        this.HeWeatherResult = HeWeatherResult;
    }

    public List<WeatherInfo> getHeWeatherResult() {
        return HeWeatherResult;
    }

    public static class WeatherInfo {
        /**
         * basic : { }
         * daily_forecast : [ ]
         * hourly_forecast : [ ]
         * now : { }
         * status : ok
         * suggestion : { }
         */

        private Basic basic;
        private Now now;
        private String status;
        private Suggestion suggestion;
        private List<DailyForecast> daily_forecast;
        private List<HourlyForecast> hourly_forecast;

        public void setBasic(Basic basic) {
            this.basic = basic;
        }

        public void setNow(Now now) {
            this.now = now;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public void setSuggestion(Suggestion suggestion) {
            this.suggestion = suggestion;
        }

        public void setDaily_forecast(List<DailyForecast> daily_forecast) {
            this.daily_forecast = daily_forecast;
        }

        public void setHourly_forecast(List<HourlyForecast> hourly_forecast) {
            this.hourly_forecast = hourly_forecast;
        }

        public Basic getBasic() {
            return basic;
        }

        public Now getNow() {
            return now;
        }

        public String getStatus() {
            return status;
        }

        public Suggestion getSuggestion() {
            return suggestion;
        }

        public List<DailyForecast> getDaily_forecast() {
            return daily_forecast;
        }

        public List<HourlyForecast> getHourly_forecast() {
            return hourly_forecast;
        }

        public static class Basic {
            /**
             * city :  城市
             * cnty :  国家
             * id :  ID
             * lat : 纬度
             * lon : 经度
             * update : {"loc":"2015-10-17 16:49","utc":"2015-10-17 08:49"}
             */

            private String city;
            private String cnty;
            private String id;
            private String lat;
            private String lon;
            private Update update;

            public void setCity(String city) {
                this.city = city;
            }

            public void setCnty(String cnty) {
                this.cnty = cnty;
            }

            public void setId(String id) {
                this.id = id;
            }

            public void setLat(String lat) {
                this.lat = lat;
            }

            public void setLon(String lon) {
                this.lon = lon;
            }

            public void setUpdate(Update update) {
                this.update = update;
            }

            public String getCity() {
                return city;
            }

            public String getCnty() {
                return cnty;
            }

            public String getId() {
                return id;
            }

            public String getLat() {
                return lat;
            }

            public String getLon() {
                return lon;
            }

            public Update getUpdate() {
                return update;
            }

            public static class Update {
                /**
                 * loc : 2015-10-17 16:49
                 * utc : 2015-10-17 08:49
                 */

                private String loc;
                private String utc;

                public void setLoc(String loc) {
                    this.loc = loc;
                }

                public void setUtc(String utc) {
                    this.utc = utc;
                }

                public String getLoc() {
                    return loc;
                }

                public String getUtc() {
                    return utc;
                }
            }
        }

        public static class Now {
            /**
             * cond : {"code":"101","txt":"多云"}
             * fl : 25
             * hum : 57
             * pcpn : 0
             * pres : 1018
             * tmp : 22
             * vis : 10
             * wind : {"deg":"10","dir":"东风","sc":"4-5","spd":"15"}
             */

            private Cond cond;
            private String fl;
            private String hum;
            private String pcpn;
            private String pres;
            private String tmp;
            private String vis;
            private Wind wind;

            public void setCond(Cond cond) {
                this.cond = cond;
            }

            public void setFl(String fl) {
                this.fl = fl;
            }

            public void setHum(String hum) {
                this.hum = hum;
            }

            public void setPcpn(String pcpn) {
                this.pcpn = pcpn;
            }

            public void setPres(String pres) {
                this.pres = pres;
            }

            public void setTmp(String tmp) {
                this.tmp = tmp;
            }

            public void setVis(String vis) {
                this.vis = vis;
            }

            public void setWind(Wind wind) {
                this.wind = wind;
            }

            public Cond getCond() {
                return cond;
            }

            public String getFl() {
                return fl;
            }

            public String getHum() {
                return hum;
            }

            public String getPcpn() {
                return pcpn;
            }

            public String getPres() {
                return pres;
            }

            public String getTmp() {
                return tmp;
            }

            public String getVis() {
                return vis;
            }

            public Wind getWind() {
                return wind;
            }

            public static class Cond {
                /**
                 * code : 101
                 * txt : 多云
                 */

                private String code;
                private String txt;

                public void setCode(String code) {
                    this.code = code;
                }

                public void setTxt(String txt) {
                    this.txt = txt;
                }

                public String getCode() {
                    return code;
                }

                public String getTxt() {
                    return txt;
                }
            }

            public static class Wind {
                /**
                 * deg : 10
                 * dir : 东风
                 * sc : 4-5
                 * spd : 15
                 */

                private String deg;
                private String dir;
                private String sc;
                private String spd;

                public void setDeg(String deg) {
                    this.deg = deg;
                }

                public void setDir(String dir) {
                    this.dir = dir;
                }

                public void setSc(String sc) {
                    this.sc = sc;
                }

                public void setSpd(String spd) {
                    this.spd = spd;
                }

                public String getDeg() {
                    return deg;
                }

                public String getDir() {
                    return dir;
                }

                public String getSc() {
                    return sc;
                }

                public String getSpd() {
                    return spd;
                }
            }
        }

        public static class Suggestion {
            /**
             * comf : {"brf":"舒适","txt":"白天不太热也不太冷，风力不大，相信您在这样的天气条件下，应会感到比较清爽和舒适。"}
             * cw : {"brf":"较适宜","txt":"较适宜洗车，未来一天无雨，风力较小，擦洗一新的汽车至少能保持一天。"}
             * drsg : {"brf":"舒适","txt":"建议着长袖T恤、衬衫加单裤等服装。年老体弱者宜着针织长袖衬衫、马甲和长裤。"}
             * flu : {"brf":"少发","txt":"各项气象条件适宜，无明显降温过程，发生感冒机率较低。"}
             * sport : {"brf":"较适宜","txt":"天气较好，但风力较大，推荐您进行室内运动，若在户外运动请注意避风保暖。"}
             * trav : {"brf":"适宜","txt":"天气较好，风稍大，但温度适宜，是个好天气哦。适宜旅游，您可以尽情地享受大自然的无限风光。"}
             * uv : {"brf":"强","txt":"紫外线辐射强，建议涂擦SPF20左右、PA++的防晒护肤品。避免在10点至14点暴露于日光下。"}
             */

            private Comf comf;
            private Cw cw;
            private Drsg drsg;
            private Flu flu;
            private Sport sport;
            private Trav trav;
            private Uv uv;

            public void setComf(Comf comf) {
                this.comf = comf;
            }

            public void setCw(Cw cw) {
                this.cw = cw;
            }

            public void setDrsg(Drsg drsg) {
                this.drsg = drsg;
            }

            public void setFlu(Flu flu) {
                this.flu = flu;
            }

            public void setSport(Sport sport) {
                this.sport = sport;
            }

            public void setTrav(Trav trav) {
                this.trav = trav;
            }

            public void setUv(Uv uv) {
                this.uv = uv;
            }

            public Comf getComf() {
                return comf;
            }

            public Cw getCw() {
                return cw;
            }

            public Drsg getDrsg() {
                return drsg;
            }

            public Flu getFlu() {
                return flu;
            }

            public Sport getSport() {
                return sport;
            }

            public Trav getTrav() {
                return trav;
            }

            public Uv getUv() {
                return uv;
            }

            public static class Comf {
                /**
                 * brf : 舒适
                 * txt : 白天不太热也不太冷，风力不大，相信您在这样的天气条件下，应会感到比较清爽和舒适。
                 */

                private String brf;
                private String txt;

                public void setBrf(String brf) {
                    this.brf = brf;
                }

                public void setTxt(String txt) {
                    this.txt = txt;
                }

                public String getBrf() {
                    return brf;
                }

                public String getTxt() {
                    return txt;
                }
            }

            public static class Cw {
                /**
                 * brf : 较适宜
                 * txt : 较适宜洗车，未来一天无雨，风力较小，擦洗一新的汽车至少能保持一天。
                 */

                private String brf;
                private String txt;

                public void setBrf(String brf) {
                    this.brf = brf;
                }

                public void setTxt(String txt) {
                    this.txt = txt;
                }

                public String getBrf() {
                    return brf;
                }

                public String getTxt() {
                    return txt;
                }
            }

            public static class Drsg {
                /**
                 * brf : 舒适
                 * txt : 建议着长袖T恤、衬衫加单裤等服装。年老体弱者宜着针织长袖衬衫、马甲和长裤。
                 */

                private String brf;
                private String txt;

                public void setBrf(String brf) {
                    this.brf = brf;
                }

                public void setTxt(String txt) {
                    this.txt = txt;
                }

                public String getBrf() {
                    return brf;
                }

                public String getTxt() {
                    return txt;
                }
            }

            public static class Flu {
                /**
                 * brf : 少发
                 * txt : 各项气象条件适宜，无明显降温过程，发生感冒机率较低。
                 */

                private String brf;
                private String txt;

                public void setBrf(String brf) {
                    this.brf = brf;
                }

                public void setTxt(String txt) {
                    this.txt = txt;
                }

                public String getBrf() {
                    return brf;
                }

                public String getTxt() {
                    return txt;
                }
            }

            public static class Sport {
                /**
                 * brf : 较适宜
                 * txt : 天气较好，但风力较大，推荐您进行室内运动，若在户外运动请注意避风保暖。
                 */

                private String brf;
                private String txt;

                public void setBrf(String brf) {
                    this.brf = brf;
                }

                public void setTxt(String txt) {
                    this.txt = txt;
                }

                public String getBrf() {
                    return brf;
                }

                public String getTxt() {
                    return txt;
                }
            }

            public static class Trav {
                /**
                 * brf : 适宜
                 * txt : 天气较好，风稍大，但温度适宜，是个好天气哦。适宜旅游，您可以尽情地享受大自然的无限风光。
                 */

                private String brf;
                private String txt;

                public void setBrf(String brf) {
                    this.brf = brf;
                }

                public void setTxt(String txt) {
                    this.txt = txt;
                }

                public String getBrf() {
                    return brf;
                }

                public String getTxt() {
                    return txt;
                }
            }

            public static class Uv {
                /**
                 * brf : 强
                 * txt : 紫外线辐射强，建议涂擦SPF20左右、PA++的防晒护肤品。避免在10点至14点暴露于日光下。
                 */

                private String brf;
                private String txt;

                public void setBrf(String brf) {
                    this.brf = brf;
                }

                public void setTxt(String txt) {
                    this.txt = txt;
                }

                public String getBrf() {
                    return brf;
                }

                public String getTxt() {
                    return txt;
                }
            }
        }

        public static class DailyForecast {
            /**
             * astro : {"sr":"05:57","ss":"17:22"}
             * cond : {"code_d":"100","code_n":"101","txt_d":"晴","txt_n":"多云"}
             * date : 2015-10-17
             * hum : 52
             * pcpn : 0.0
             * pop : 0
             * pres : 1018
             * tmp : {"max":"25","min":"15"}
             * vis : 10
             * wind : {"deg":"70","dir":"东北风","sc":"3-4","spd":"0"}
             */

            private AstroEntity astro;
            private Cond cond;
            private String date;
            private String hum;
            private String pcpn;
            private String pop;
            private String pres;
            private Tmp tmp;
            private String vis;
            private Wind wind;

            public void setAstro(AstroEntity astro) {
                this.astro = astro;
            }

            public void setCond(Cond cond) {
                this.cond = cond;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public void setHum(String hum) {
                this.hum = hum;
            }

            public void setPcpn(String pcpn) {
                this.pcpn = pcpn;
            }

            public void setPop(String pop) {
                this.pop = pop;
            }

            public void setPres(String pres) {
                this.pres = pres;
            }

            public void setTmp(Tmp tmp) {
                this.tmp = tmp;
            }

            public void setVis(String vis) {
                this.vis = vis;
            }

            public void setWind(Wind wind) {
                this.wind = wind;
            }

            public AstroEntity getAstro() {
                return astro;
            }

            public Cond getCond() {
                return cond;
            }

            public String getDate() {
                return date;
            }

            public String getHum() {
                return hum;
            }

            public String getPcpn() {
                return pcpn;
            }

            public String getPop() {
                return pop;
            }

            public String getPres() {
                return pres;
            }

            public Tmp getTmp() {
                return tmp;
            }

            public String getVis() {
                return vis;
            }

            public Wind getWind() {
                return wind;
            }

            public static class AstroEntity {
                /**
                 * sr : 05:57
                 * ss : 17:22
                 */

                private String sr;
                private String ss;

                public void setSr(String sr) {
                    this.sr = sr;
                }

                public void setSs(String ss) {
                    this.ss = ss;
                }

                public String getSr() {
                    return sr;
                }

                public String getSs() {
                    return ss;
                }
            }

            public static class Cond {
                /**
                 * code_d : 100
                 * code_n : 101
                 * txt_d : 晴
                 * txt_n : 多云
                 */

                private String code_d;
                private String code_n;
                private String txt_d;
                private String txt_n;

                public void setCode_d(String code_d) {
                    this.code_d = code_d;
                }

                public void setCode_n(String code_n) {
                    this.code_n = code_n;
                }

                public void setTxt_d(String txt_d) {
                    this.txt_d = txt_d;
                }

                public void setTxt_n(String txt_n) {
                    this.txt_n = txt_n;
                }

                public String getCode_d() {
                    return code_d;
                }

                public String getCode_n() {
                    return code_n;
                }

                public String getTxt_d() {
                    return txt_d;
                }

                public String getTxt_n() {
                    return txt_n;
                }
            }

            public static class Tmp {
                /**
                 * max : 25
                 * min : 15
                 */

                private String max;
                private String min;

                public void setMax(String max) {
                    this.max = max;
                }

                public void setMin(String min) {
                    this.min = min;
                }

                public String getMax() {
                    return max;
                }

                public String getMin() {
                    return min;
                }
            }

            public static class Wind {
                /**
                 * deg : 70
                 * dir : 东北风
                 * sc : 3-4
                 * spd : 0
                 */

                private String deg;
                private String dir;
                private String sc;
                private String spd;

                public void setDeg(String deg) {
                    this.deg = deg;
                }

                public void setDir(String dir) {
                    this.dir = dir;
                }

                public void setSc(String sc) {
                    this.sc = sc;
                }

                public void setSpd(String spd) {
                    this.spd = spd;
                }

                public String getDeg() {
                    return deg;
                }

                public String getDir() {
                    return dir;
                }

                public String getSc() {
                    return sc;
                }

                public String getSpd() {
                    return spd;
                }
            }
        }

        public static class HourlyForecast {
            /**
             * date : 2015-10-17 16:00
             * hum : 67
             * pop : 0
             * pres : 1018
             * tmp : 22
             * wind : {"deg":"78","dir":"东北风","sc":"微风","spd":"16"}
             */

            private String date;
            private String hum;
            private String pop;
            private String pres;
            private String tmp;
            private Wind wind;

            public void setDate(String date) {
                this.date = date;
            }

            public void setHum(String hum) {
                this.hum = hum;
            }

            public void setPop(String pop) {
                this.pop = pop;
            }

            public void setPres(String pres) {
                this.pres = pres;
            }

            public void setTmp(String tmp) {
                this.tmp = tmp;
            }

            public void setWind(Wind wind) {
                this.wind = wind;
            }

            public String getDate() {
                return date;
            }

            public String getHum() {
                return hum;
            }

            public String getPop() {
                return pop;
            }

            public String getPres() {
                return pres;
            }

            public String getTmp() {
                return tmp;
            }

            public Wind getWind() {
                return wind;
            }

            public static class Wind {
                /**
                 * deg : 78
                 * dir : 东北风
                 * sc : 微风
                 * spd : 16
                 */

                private String deg;
                private String dir;
                private String sc;
                private String spd;

                public void setDeg(String deg) {
                    this.deg = deg;
                }

                public void setDir(String dir) {
                    this.dir = dir;
                }

                public void setSc(String sc) {
                    this.sc = sc;
                }

                public void setSpd(String spd) {
                    this.spd = spd;
                }

                public String getDeg() {
                    return deg;
                }

                public String getDir() {
                    return dir;
                }

                public String getSc() {
                    return sc;
                }

                public String getSpd() {
                    return spd;
                }
            }
        }
    }
}
