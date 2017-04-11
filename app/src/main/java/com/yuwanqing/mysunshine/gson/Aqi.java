package com.yuwanqing.mysunshine.gson;

/**
 * Created by yuwanqing on 2017-03-29.
 */
public class Aqi {
    public Aqicity city;
    public class Aqicity {
        public String aqi;
        public String co;
        public String no2;
        public String o3;
        public String pm10;
        public String pm25;
        public String qlty;
        public String so2;
    }
    //"aqi":{"city":{"aqi":"52","co":"1","no2":"34","o3":"35","pm10":"40","pm25":"36","qlty":"良","so2":"9"
}
