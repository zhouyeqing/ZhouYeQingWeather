package com.example.zhouyeqingweather.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

//   网络连接类，只有一个方法，接收2个参数：请求地址和回调函数
public class HttpUtil {
    public static void sendOkHttpRequest(String address, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }
}
