package com.example.zhouyeqingweather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.zhouyeqingweather.gson.Forecast;
import com.example.zhouyeqingweather.gson.WeatherForecast;
import com.example.zhouyeqingweather.gson.WeatherLifeStyle;
import com.example.zhouyeqingweather.gson.WeatherNow;
import com.example.zhouyeqingweather.util.HttpUtil;
import com.example.zhouyeqingweather.util.Utility;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {

    private ScrollView weatherLayout;

    private TextView titleCity;

    private TextView titleUpdateTime;

    private TextView degreeText;

    private TextView weatherInfoText;

    private LinearLayout forecastLayout;

    private TextView aqiText;

    private TextView pm25Text;

    private TextView suggestionText1;

    private TextView suggestionText2;

    private TextView suggestionText3;

    private TextView suggestionText4;

    private TextView suggestionText5;

    private TextView suggestionText6;

    private TextView suggestionText7;

    private TextView suggestionText8;

    SharedPreferences prefs;

    private Boolean weatherNowFinish = false;

    private Boolean weatherForecastFinish = false;

    private Boolean weatherLifeStyleFinish = false;

    private ImageView bingPicImg;

    public SwipeRefreshLayout swipeRefresh;

    private String mWeatherId;

    public DrawerLayout drawerLayout;

    private Button navButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_weather);
        //   初始化各控件
        weatherLayout = findViewById(R.id.weather_layout);
        titleCity = findViewById(R.id.title_city);
        titleUpdateTime = findViewById(R.id.title_update_time);
        degreeText = findViewById(R.id.degree_text);
        weatherInfoText = findViewById(R.id.weather_info_text);
        forecastLayout = findViewById(R.id.forecast_layout);
        aqiText = findViewById(R.id.aqi_text);
        pm25Text = findViewById(R.id.pm25_text);
        suggestionText1 = findViewById(R.id.suggestion_text1);
        suggestionText2 = findViewById(R.id.suggestion_text2);
        suggestionText3 = findViewById(R.id.suggestion_text3);
        suggestionText4 = findViewById(R.id.suggestion_text4);
        suggestionText5 = findViewById(R.id.suggestion_text5);
        suggestionText6 = findViewById(R.id.suggestion_text6);
        suggestionText7 = findViewById(R.id.suggestion_text7);
        suggestionText8 = findViewById(R.id.suggestion_text8);
        drawerLayout = findViewById(R.id.drawer_layout);
        navButton = findViewById(R.id.nav_button);
        bingPicImg = findViewById(R.id.bing_pic_img);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        swipeRefresh = findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        String bingPic = prefs.getString("bing_pic", null);
        if (bingPic != null) {
            Glide.with(this).load(bingPic).into(bingPicImg);
        } else {
            loadBingPic();
        }
        executiveProgram();
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeatherNow(mWeatherId);
                requestWeatherForecast(mWeatherId);
                requestWeatherLifeStyle(mWeatherId);
            }
        });
        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    void requestWeatherLifeStyle(String weatherId) {
        String weatherUrl = "https://free-api.heweather.net/s6/weather/lifestyle?location=" + weatherId + "&key=13ece114407142919af6ab87fbaafc07";
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "获取lifestyle天气信息失败2", Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseText = response.body().string();
                final WeatherLifeStyle weatherLifeStyle = Utility.handleWeatherLifeStyleResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weatherLifeStyle != null && "ok".equals(weatherLifeStyle.status)) {
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weatherLifeStyle", responseText);
                            editor.apply();
                            mWeatherId = weatherLifeStyle.basic.weatherId;
                            weatherLifeStyleFinish = true;
                            if (weatherForecastFinish && weatherNowFinish) {
                                executiveProgram();
                                swipeRefresh.setRefreshing(false);
                            }
                        } else {
                            Toast.makeText(WeatherActivity.this, "获取lifestyle天气信息失败1", Toast.LENGTH_SHORT).show();
                            swipeRefresh.setRefreshing(false);
                        }
                    }
                });
            }
        });
    }

    void requestWeatherForecast(String weatherId) {
        String weatherUrl = "https://free-api.heweather.net/s6/weather/forecast?location=" + weatherId + "&key=13ece114407142919af6ab87fbaafc07";
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "获取forecast天气信息失败2", Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseText = response.body().string();
                final WeatherForecast weatherForecast = Utility.handleWeatherForecastResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weatherForecast != null && "ok".equals(weatherForecast.status)) {
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weatherForecast", responseText);
                            editor.apply();
                            mWeatherId = weatherForecast.basic.weatherId;
                            weatherForecastFinish = true;
                            if (weatherNowFinish && weatherLifeStyleFinish) {
                                executiveProgram();
                                swipeRefresh.setRefreshing(false);
                            }
                        } else {
                            Toast.makeText(WeatherActivity.this, "获取forecast天气信息失败1", Toast.LENGTH_SHORT).show();
                            swipeRefresh.setRefreshing(false);
                        }
                    }
                });
            }
        });
    }

    void requestWeatherNow(String weatherId) {
        String weatherUrl = "https://free-api.heweather.net/s6/weather/now?location=" + weatherId + "&key=13ece114407142919af6ab87fbaafc07";
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "获取now天气信息失败2", Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseText = response.body().string();
                final WeatherNow weatherNow = Utility.handleWeatherNowResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weatherNow != null && "ok".equals(weatherNow.status)) {
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weatherNow", responseText);
                            editor.apply();
                            mWeatherId = weatherNow.basic.weatherId;
                            weatherNowFinish = true;
                            if (weatherForecastFinish && weatherLifeStyleFinish) {
                                executiveProgram();
                                swipeRefresh.setRefreshing(false);
                            }
                        } else {
                            Toast.makeText(WeatherActivity.this, "获取now天气信息失败1", Toast.LENGTH_SHORT).show();
                            swipeRefresh.setRefreshing(false);
                        }
                    }
                });
            }
        });
        loadBingPic();
    }

    private void showWeatherInfo(WeatherNow weatherNow, WeatherForecast weatherForecast, WeatherLifeStyle weatherLifeStyle) {
        String cityName = weatherNow.basic.cityName;
        String updateTime = weatherNow.update.updateTime.split(" ")[1];
        String degree = weatherNow.now.temperature + "℃";
        String weatherInfo = weatherNow.now.cond;
        titleCity.setText(cityName);
        titleUpdateTime.setText(updateTime);
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);
        forecastLayout.removeAllViews();
        for (Forecast forecast : weatherForecast.forecastList) {
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false);
            TextView dateText = view.findViewById(R.id.date_text);
            TextView infoText = view.findViewById(R.id.info_text);
            TextView maxText = view.findViewById(R.id.max_text);
            TextView minText = view.findViewById(R.id.min_text);
            dateText.setText(forecast.date);
            infoText.setText(forecast.cond);
            maxText.setText(forecast.maxTemperature);
            minText.setText(forecast.minTemperature);
            forecastLayout.addView(view);
        }
        aqiText.setText(weatherNow.now.windDir);
        pm25Text.setText(weatherNow.now.windSc + "级");
        String comf = "舒适度指数：" + weatherLifeStyle.lifeStyleList.get(0).brf + "," + weatherLifeStyle.lifeStyleList.get(0).txt;
        String drsg = "穿衣指数：" + weatherLifeStyle.lifeStyleList.get(1).brf + "," + weatherLifeStyle.lifeStyleList.get(1).txt;
        String flu = "感冒指数：" + weatherLifeStyle.lifeStyleList.get(2).brf + "," + weatherLifeStyle.lifeStyleList.get(2).txt;
        String sport = "运动指数：" + weatherLifeStyle.lifeStyleList.get(3).brf + "," + weatherLifeStyle.lifeStyleList.get(3).txt;
        String trav = "旅游指数：" + weatherLifeStyle.lifeStyleList.get(4).brf + "," + weatherLifeStyle.lifeStyleList.get(4).txt;
        String uv = "紫外线指数：" + weatherLifeStyle.lifeStyleList.get(5).brf + "," + weatherLifeStyle.lifeStyleList.get(5).txt;
        String cw = "洗车指数：" + weatherLifeStyle.lifeStyleList.get(6).brf + "," + weatherLifeStyle.lifeStyleList.get(6).txt;
        String air = "空气污染扩散条件指数：" + weatherLifeStyle.lifeStyleList.get(7).brf + "," + weatherLifeStyle.lifeStyleList.get(7).txt;
        suggestionText1.setText(comf);
        suggestionText2.setText(drsg);
        suggestionText3.setText(flu);
        suggestionText4.setText(sport);
        suggestionText5.setText(trav);
        suggestionText6.setText(uv);
        suggestionText7.setText(cw);
        suggestionText8.setText(air);
        weatherLayout.setVisibility(View.VISIBLE);
    }

    private void executiveProgram() {
        String weatherNowString = prefs.getString("weatherNow", null);
        String weatherForecastString = prefs.getString("weatherForecast", null);
        String weatherLifeStyleString = prefs.getString("weatherLifeStyle", null);
        if (weatherNowString != null && weatherForecastString != null && weatherLifeStyleString != null) {
            WeatherNow weatherNow = Utility.handleWeatherNowResponse(weatherNowString);
            WeatherForecast weatherForecast = Utility.handleWeatherForecastResponse(weatherForecastString);
            WeatherLifeStyle weatherLifeStyle = Utility.handleWeatherLifeStyleResponse(weatherLifeStyleString);
            mWeatherId = weatherNow.basic.weatherId;
            showWeatherInfo(weatherNow, weatherForecast, weatherLifeStyle);
        } else {
            mWeatherId = getIntent().getStringExtra("weather_id");
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeatherNow(mWeatherId);
            requestWeatherForecast(mWeatherId);
            requestWeatherLifeStyle(mWeatherId);
        }

    }

    private void loadBingPic() {
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString("bing_pic", bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingPic).into(bingPicImg);
                    }
                });
            }
        });
    }
}
