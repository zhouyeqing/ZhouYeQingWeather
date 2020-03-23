package com.example.zhouyeqingweather.util;

import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.example.zhouyeqingweather.db.MyDatabaseHelper;
import com.example.zhouyeqingweather.gson.WeatherForecast;
import com.example.zhouyeqingweather.gson.WeatherLifeStyle;
import com.example.zhouyeqingweather.gson.WeatherNow;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//   数据处理类，处理接收到的数据
public class Utility {
    public static boolean handleProvinceResponse(SQLiteDatabase db, String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allProvinces = new JSONArray(response);
                for (int i = 0; i < allProvinces.length(); i++) {
                    JSONObject provinceObject = allProvinces.getJSONObject(i);
                    MyDatabaseHelper.saveProvinceData(db, provinceObject.getString("name"), provinceObject.getString("id"));
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean handleCityResponse(SQLiteDatabase db, String response, int provinceId) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allCities = new JSONArray(response);
                for (int i = 0; i < allCities.length(); i++) {
                    JSONObject cityObject = allCities.getJSONObject(i);
                    MyDatabaseHelper.saveCityData(db, cityObject.getString("name"), cityObject.getString("id"), String.valueOf(provinceId));
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean handleCountyResponse(SQLiteDatabase db, String response, int cityId) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allCounties = new JSONArray(response);
                for (int i = 0; i < allCounties.length(); i++) {
                    JSONObject countyObject = allCounties.getJSONObject(i);
                    MyDatabaseHelper.saveCountyData(db, countyObject.getString("name"), countyObject.getString("weather_id"), String.valueOf(cityId));
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    //   将返回的JSON数据解析成WeatherNow实体类
    public static WeatherNow handleWeatherNowResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather6");
            String weatherContent = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent, WeatherNow.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //   将返回的JSON数据解析成WeatherForecast实体类
    public static WeatherForecast handleWeatherForecastResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather6");
            String weatherContent = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent, WeatherForecast.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //   将返回的JSON数据解析成WeatherLifeStyle实体类
    public static WeatherLifeStyle handleWeatherLifeStyleResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather6");
            String weatherContent = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent, WeatherLifeStyle.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
