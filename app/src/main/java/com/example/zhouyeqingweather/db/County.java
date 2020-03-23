package com.example.zhouyeqingweather.db;

public class County {
    private int id;
    private String countyName;
    private String weatherId;
    private int CityId;

    public void setId(int id) {
        this.id = id;
    }

    public void setCountyName(String name) {
        this.countyName = name;
    }

    public void setWeatherId(String weather) {
        this.weatherId = weather;
    }

    public void setCityId(int cityId) {
        CityId = cityId;
    }

    public int getId() {
        return id;
    }

    public String getCountyName() {
        return countyName;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public int getCityId() {
        return CityId;
    }
}
