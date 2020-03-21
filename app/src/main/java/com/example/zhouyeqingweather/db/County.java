package com.example.zhouyeqingweather.db;

public class County {
    private int id;
    private String Name;
    private String weather;
    private int CityId;

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public void setCityId(int cityId) {
        CityId = cityId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return Name;
    }

    public String getWeather() {
        return weather;
    }

    public int getCityId() {
        return CityId;
    }
}
