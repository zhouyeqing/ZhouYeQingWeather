package com.example.zhouyeqingweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherForecast {
    public String status;
    public Basic basic;
    public Update update;
    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;
}
