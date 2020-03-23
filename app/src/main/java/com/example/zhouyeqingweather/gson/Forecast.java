package com.example.zhouyeqingweather.gson;

import com.google.gson.annotations.SerializedName;

public class Forecast {
    public String date;

    @SerializedName("tmp_max")
    public String maxTemperature;

    @SerializedName("tmp_min")
    public String minTemperature;

    @SerializedName("cond_txt_d")
    public String cond;
}
