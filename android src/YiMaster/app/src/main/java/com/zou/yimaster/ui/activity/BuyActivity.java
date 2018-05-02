package com.zou.yimaster.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.zou.yimaster.R;
import com.zou.yimaster.net.RetrofitHelper;
import com.zou.yimaster.ui.base.BaseActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zougaoyuan on 04.27.027
 *
 * @author zougaoyuan
 */
public class BuyActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bug);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.BuyClose, R.id.Buy_1, R.id.Buy_2, R.id.Buy_3, R.id.Buy_4})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Buy_1:
                buy(getFee(5));
                break;
            case R.id.Buy_2:
                buy(getFee(10));
                break;
            case R.id.Buy_3:
                buy(getFee(15));
                break;
            case R.id.Buy_4:
                buy(getFee(20));
                break;
            case R.id.BuyClose:
                finish();
                break;
        }
    }

    @SuppressLint("CheckResult")
    private void buy(int fee) {
        RetrofitHelper.PlaceOrder(fee, "weChat")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(s -> {

                });
    }

    @Override
    protected boolean isBack() {
        return true;
    }

    /**
     * 将元转为分
     */
    private int getFee(float fee) {
        return (int) (fee * 10 * 10);
    }
}
