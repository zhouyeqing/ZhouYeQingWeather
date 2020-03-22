package com.example.zhouyeqingweather;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.example.zhouyeqingweather.db.MyDatabaseHelper;

public class MainActivity extends AppCompatActivity {
//    private MyDatabaseHelper dbHelper;
//    private static String URL_PROVINCE = "http://guolin.tech/api/china";
//    private static String URL_CITY = "http://guolin.tech/api/china/provinceId";
//    private static String URL_COUNTY = "http://guolin.tech/api/china/provinceId/cityId";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        dbHelper = new MyDatabaseHelper(this, "City.db", null, 1);   //  创建数据库，名字：City.db，版本：1
//        SQLiteDatabase database = dbHelper.getWritableDatabase();   //  打开数据库
//        database.close();
    }
}
