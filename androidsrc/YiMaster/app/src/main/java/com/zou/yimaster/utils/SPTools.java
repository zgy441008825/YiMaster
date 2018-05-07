package com.zou.yimaster.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by zougaoyuan on 2018/4/2
 *
 * @author zougaoyuan
 */
public class SPTools {

    private static SharedPreferences sp;
    private static final String SPName = "YiSp";
    private static SPTools spTools;

    private SPTools(Context context) {
        sp = context.getSharedPreferences(SPName, Context.MODE_PRIVATE);
    }

    public static SPTools getInstance(Context context) {
        if (spTools == null) {
            spTools = new SPTools(context);
        }
        return spTools;
    }

    public boolean getBoolean(String key) {
        return sp.getBoolean(key, false);
    }

    public void saveBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public int getInt(String key, int def) {
        return sp.getInt(key, def);
    }

    public void saveInt(String key, int value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public long getLong(String key, long def) {
        return sp.getLong(key, def);
    }

    public void saveLong(String key, long value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(key, value);
        editor.apply();
    }

}
