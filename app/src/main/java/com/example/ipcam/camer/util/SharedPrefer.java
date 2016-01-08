package com.example.ipcam.camer.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * 主要用于存储软件的配置信息
 * Created by Administrator on 2016/1/8.
 */
public class SharedPrefer {
    public static String ALARM_LS = "alarm_ls";  //报警声音
    public static String ALARM_T = "alarm_time";  //报警时长
    public static String ALARM_Z = "alarm_zd";  //报警震动

    /**
     * 保存软件的设置信息
     */
    public static void SaveAppSetingData(Context context, HashMap<String, Object> map) {
        SharedPreferences preferences = context.getSharedPreferences("setdata",
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        boolean flg = (boolean) map.get(ALARM_LS);
        editor.putBoolean(ALARM_LS, flg);
        editor.putBoolean(ALARM_Z, (Boolean) map.get(ALARM_Z));
        editor.putInt(ALARM_T, (Integer) map.get(ALARM_T));
        editor.commit();
    }

    /**
     * 获取软件的设置信息
     *
     * @param context
     * @return
     */
    public static HashMap<String, Object> GetAppSetingData(Context context) {
        HashMap<String, Object> map = new HashMap<>();
        SharedPreferences sharedPreferences = context.getSharedPreferences("setdata",
                Activity.MODE_PRIVATE);
        int time = sharedPreferences.getInt(ALARM_T, 15);
        boolean zd = sharedPreferences.getBoolean(ALARM_Z, false);
        boolean sy = sharedPreferences.getBoolean(ALARM_LS, false);

        map.put(ALARM_LS, sy);
        map.put(ALARM_Z, zd);
        map.put(ALARM_T, time);
        return map;
    }
}
