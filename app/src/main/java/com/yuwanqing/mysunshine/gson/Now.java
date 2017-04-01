package com.yuwanqing.mysunshine.gson;

/**
 * Created by yuwanqing on 2017-03-29.
 */
public class Now {
    public String tmp;
    public String hum;
    public String fl;
    public Cond cond;
    public Wind wind;
    public class Cond{
        public String txt;
    }
    public class Wind{
        public String dir;
        public String sc;
    }
}
