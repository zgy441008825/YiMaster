package com.zou.yimaster.common;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zougy on 03.22.022
 * 使用RxJava的倒计时工具
 */
public class RxCountDown {

    public static Flowable<Integer> countDown(int time) {
        System.out.println("RxCountDown:" + time);
        final int countTime = time;
        return Flowable.interval(0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .map(aLong -> countTime - aLong.intValue())
                .take(countTime + 1);
    }

}
