package com.example.zhouyeqingweather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    public static final String CREATE_PROVINCE = "create table Province ("
            + "id integer primary key autoincrement, "
            + "provinceName text, "
            + "provinceCode integer)";

    public static final String CREATE_CITY = "create table City ("
            + "id integer primary key autoincrement, "
            + "cityName text, "
            + "cityCode integer, "
            + "provinceId integer)";

    public static final String CREATE_COUNTY = "create table County ("
            + "id integer primary key autoincrement, "
            + "countyName text, "
            + "weatherId text, "
            + "cityId integer)";

    private Context mContext;

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.mContext = context;
    }

    public static void saveProvinceData(SQLiteDatabase db, String provinceName, String provinceCode) {
        db.execSQL("INSERT INTO Province (provinceName, provinceCode) Values (?, ?)", new String[] {provinceName, provinceCode});
    }

    public static void saveCityData(SQLiteDatabase db, String cityName, String cityCode, String provinceId) {
        db.execSQL("INSERT INTO City (cityName, cityCode, provinceId) Values (?, ?, ?)", new String[] {cityName, cityCode, provinceId});
    }

    public static void saveCountyData(SQLiteDatabase db, String countyName, String weatherId, String cityId) {
        db.execSQL("INSERT INTO County (countyName, weatherId, cityId) Values (?, ?, ?)", new String[] {countyName, weatherId, cityId});
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PROVINCE);
        db.execSQL(CREATE_CITY);
        db.execSQL(CREATE_COUNTY);
        Toast.makeText(mContext, "全国城市信息数据库创建成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists Province");
        db.execSQL("drop table if exists City");
        db.execSQL("drop table if exists County");
        onCreate(db);
    }
}
