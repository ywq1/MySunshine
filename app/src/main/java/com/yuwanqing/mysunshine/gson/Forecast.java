package com.yuwanqing.mysunshine.gson;

/**
 * Created by yuwanqing on 2017-03-29.
 */
public class Forecast {
    public String date;
    public Temperature tmp;
    public Cond cond;
    public class Temperature {
        public String max;
        public String min;
    }
    public class Cond {
        public String txt_d;
    }

}
