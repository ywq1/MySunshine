package com.yuwanqing.mysunshine.gson;

import java.util.List;

/**
 * Created by yuwanqing on 2017-03-29.
 */
public class Weather {
    public Aqi aqi;
    public Basic basic;
    public List<Forecast> daily_forecast;
    public Now now;
    public String status;
    public Suggestion suggestion;
}
