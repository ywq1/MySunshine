package com.yuwanqing.mysunshine.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by yuwanqing on 2017-03-29.
 */
public class HttpUtil {
    public static void sendOkHttpRequest(String address, okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();//首先创建一个OkHttpClient的实例
        Request request = new Request.Builder().url(address).build();//想要发送一条HTTP的请求，就需要创建一个Request对象，用url（）方法来设置目标的网络地址
        client.newCall(request).enqueue(callback);//发送请求并获取服务器返回的数据
    }
}
