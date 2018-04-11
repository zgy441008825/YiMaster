package com.zou.yimaster.utils;

import android.widget.Toast;

import org.xutils.x;

/**
 * Created by zougaoyuan on 2018/4/10
 *
 * @author zougaoyuan
 */
public class ToastHelper {

    private static Toast toast;

    public static void show(String msg, int duration) {
        if (toast != null) {
            toast.cancel();
            toast = null;
        }
        toast = Toast.makeText(x.app(), msg, duration);
        toast.show();
    }

    public static void show(int msgID, int duration) {
        show(x.app().getResources().getString(msgID), duration);
    }

    public static void show(int msgID) {
        show(x.app().getResources().getString(msgID), Toast.LENGTH_SHORT);
    }

    public static void show(String msg) {
        show(msg, Toast.LENGTH_SHORT);
    }

}
