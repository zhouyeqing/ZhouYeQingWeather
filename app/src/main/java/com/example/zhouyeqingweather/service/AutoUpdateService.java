package com.example.zhouyeqingweather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;

import com.example.zhouyeqingweather.gson.WeatherForecast;
import com.example.zhouyeqingweather.gson.WeatherLifeStyle;
import com.example.zhouyeqingweather.gson.WeatherNow;
import com.example.zhouyeqingweather.util.HttpUtil;
import com.example.zhouyeqingweather.util.Utility;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.prefs.PreferenceChangeEvent;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AutoUpdateService extends Service {
    public AutoUpdateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        updateWeather();
        updateBingPic();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour = 8 * 60 * 60 * 1000;
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        Intent i = new Intent(this, AutoUpdateService.class);
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        manager.cancel(pi);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }

    private void updateWeather() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherNowString = prefs.getString("weatherNow", null);
        if (weatherNowString != null) {
            WeatherNow weatherNow = Utility.handleWeatherNowResponse(weatherNowString);
            String weatherId = weatherNow.basic.weatherId;
            String weatherNowUrl = "https://free-api.heweather.net/s6/weather/now?location=" + weatherId + "&key=13ece114407142919af6ab87fbaafc07";
            String weatherForecastUrl = "https://free-api.heweather.net/s6/weather/forecast?location=" + weatherId + "&key=13ece114407142919af6ab87fbaafc07";
            String weatherLifeStyleUrl = "https://free-api.heweather.net/s6/weather/lifestyle?location=" + weatherId + "&key=13ece114407142919af6ab87fbaafc07";
            HttpUtil.sendOkHttpRequest(weatherNowUrl, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String responseText = response.body().string();
                    WeatherNow weatherNow = Utility.handleWeatherNowResponse(responseText);
                    if (weatherNow != null && "ok".equals(weatherNow.status)) {
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                        editor.putString("weatherNow", responseText);
                        editor.apply();
                    }
                }
            });
            HttpUtil.sendOkHttpRequest(weatherForecastUrl, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String responseText = response.body().string();
                    WeatherForecast weatherForecast = Utility.handleWeatherForecastResponse(responseText);
                    if (weatherForecast != null && "ok".equals(weatherForecast.status)) {
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                        editor.putString("weatherForecast", responseText);
                        editor.apply();
                    }
                }
            });
            HttpUtil.sendOkHttpRequest(weatherLifeStyleUrl, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String responseText = response.body().string();
                    WeatherLifeStyle weatherLifeStyle = Utility.handleWeatherLifeStyleResponse(responseText);
                    if (weatherLifeStyle != null && "ok".equals(weatherLifeStyle.status)) {
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                        editor.putString("weatherLifeStyle", responseText);
                        editor.apply();
                    }
                }
            });
        }
    }

    private void updateBingPic() {
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                editor.putString("bing_pic", bingPic);
                editor.apply();
            }
        });
    }
}
