package com.zou.yimaster.common;

import android.annotation.SuppressLint;

import com.zou.yimaster.common.dao.UserGameRecord;
import com.zou.yimaster.common.dao.YiDBManager;
import com.zou.yimaster.net.RetrofitHelper;

import io.reactivex.schedulers.Schedulers;

/**
 * Created by zougaoyuan on 2018/4/12
 *
 * @author zougaoyuan
 */
public class GameHelper {

    @SuppressLint("CheckResult")
    public static void saveResult(UserGameRecord record) {
        RetrofitHelper.saveRecord(record)
                .subscribeOn(Schedulers.io())
                .subscribe(s -> {
                    record.setSendToServer(true);
                    YiDBManager.getInstance().saveRecord(record);
                }, throwable -> {
                    record.setSendToServer(false);
                    YiDBManager.getInstance().saveRecord(record);
                    throwable.printStackTrace();
                });
    }

}
