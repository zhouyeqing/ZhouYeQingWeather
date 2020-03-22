package com.example.zhouyeqingweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherLifeStyle {
    public String status;
    public Basic basic;
    public Update update;
    @SerializedName("lifestyle")
    public List<LifeStyle> lifeStyleList;
}
