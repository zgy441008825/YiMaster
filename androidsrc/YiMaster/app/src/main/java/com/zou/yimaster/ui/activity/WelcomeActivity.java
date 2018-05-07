package com.zou.yimaster.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.zou.yimaster.R;
import com.zou.yimaster.common.AppConfig;
import com.zou.yimaster.common.PowerFactory;
import com.zou.yimaster.common.RxCountDown;
import com.zou.yimaster.net.RetrofitHelper;
import com.zou.yimaster.ui.base.BaseActivity;

import io.reactivex.schedulers.Schedulers;

public class WelcomeActivity extends BaseActivity {

    private Button showCountDown;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        showCountDown = findViewById(R.id.welcomeBTCountdown);
        PowerFactory.getInstance().init();
        RetrofitHelper.getAllChannelInfo()
                .subscribeOn(Schedulers.newThread())
                .subscribe(stringInfoBeanMap -> {
                    System.out.println(stringInfoBeanMap);
                    AppConfig.APPConfigs = stringInfoBeanMap;
                }, throwable -> AppConfig.APPConfigs = null);
        RxCountDown.countDown(3)
                .subscribe(integer -> {
                    if (integer == 0) {
                        startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                        finish();
                        return;
                    }
                    showCountDown.setText(String.valueOf(integer));
                });
    }

}
