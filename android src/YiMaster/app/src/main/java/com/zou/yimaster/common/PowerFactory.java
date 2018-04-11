package com.zou.yimaster.common;

import android.content.Context;
import android.util.Log;

import com.zou.yimaster.utils.SPTools;

import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.Locale;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by zougaoyuan on 2018/4/8
 *
 * @author zougaoyuan
 */
public class PowerFactory {

    private static final int CONSUME = 120;

    public static final int POWER_MAX = 15;

    private static final int CONSUMPTION_UNIT = 1;

    private static final String SP_KEY_POWER = "power_powerCnt";

    private static final String SP_KEY_TIME = "power_last_time";

    private static final String SP_KEY_COUNTDOWN = "power_countdown";

    private IPowerProductionListener listener;

    private Disposable productionDisposable;

    private SimpleDateFormat format = new SimpleDateFormat("mm:ss", Locale.getDefault());

    private static PowerFactory instance;
    private SPTools spTools;

    private PowerFactory(Context context) {
        spTools = SPTools.getInstance(context);
    }

    public static PowerFactory getInstance() {
        if (instance == null) {
            synchronized (PowerFactory.class) {
                if (instance == null) {
                    instance = new PowerFactory(x.app());
                }
            }
        }
        return instance;
    }

    public void init() {
        long lastTime = spTools.getLong(SP_KEY_TIME, System.currentTimeMillis()) / 1000;
        long curTime = System.currentTimeMillis() / 1000;
        int cnt = (int) ((curTime - lastTime) / CONSUME);
        int p = getPower();
        if (p < POWER_MAX) {
            if ((cnt + p) >= POWER_MAX) {
                changePower(POWER_MAX);
                countDown = 0;
            } else {
                countDown = (int) ((curTime - lastTime) % CONSUME);
                changePower(cnt + p);
                startProduction();
            }
        }
    }


    public int getPower() {
        return spTools.getInt(SP_KEY_POWER, POWER_MAX);
    }

    private int countDown = 0;

    private void startProduction() {
        int power = getPower();
        if (power >= POWER_MAX || productionDisposable != null) return;
        System.out.println("startProduction: " + countDown);
        productionDisposable = RxCountDown.countDown(CONSUME - countDown)
                .subscribe(integer -> {
                    System.out.println("startProduction onNext: " + integer);
                    countDown = integer;
                    if (listener != null) {
                        String formatTime = format.format(integer * 1000);
                        listener.onStateChange(formatTime);
                    }
                }, throwable -> {
                }, () -> {
                    changePower(power + 1);
                    productionDisposable = null;
                    startProduction();
                });
    }

    /**
     * 更新
     */
    private void changePower(int power) {
        spTools.saveInt(SP_KEY_POWER, power);
        spTools.saveLong(SP_KEY_TIME, System.currentTimeMillis());
        spTools.saveInt(SP_KEY_COUNTDOWN, 0);
        if (listener != null) {
            listener.onStockChange(power);
        }
    }

    public void trunToBackground() {
        spTools.saveLong(SP_KEY_TIME, System.currentTimeMillis());
        spTools.saveInt(SP_KEY_COUNTDOWN, countDown);
    }

    public boolean canPlay() {
        return getPower() >= CONSUMPTION_UNIT;
    }

    /**
     * 消费
     *
     * @return TRUE 可以消费，FALSE 不可以消费
     */
    public void consumption() {
        if (canPlay()) {
            changePower(getPower() - CONSUMPTION_UNIT);
        }
        startProduction();
    }

    public void setListener(@NonNull IPowerProductionListener listener) {
        this.listener = listener;
        this.listener.onStockChange(getPower());
    }

    public interface IPowerProductionListener {
        void onStateChange(String time);

        void onStockChange(int stock);
    }

}
