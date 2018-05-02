package com.zou.yimaster.ui.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zou.yimaster.R;
import com.zou.yimaster.common.AppConfig;
import com.zou.yimaster.common.dao.OrderBean;
import com.zou.yimaster.net.RetrofitHelper;
import com.zou.yimaster.ui.base.BaseActivity;
import com.zou.yimaster.utils.YiException;

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

    private static final String TAG = "BuyActivity";
    private IWXAPI iwxapi;
    private ProgressDialog progressDialog;

    public static final String ACTION_PAY_RESULT = "pay_result";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bug);
        ButterKnife.bind(this);
        iwxapi = WXAPIFactory.createWXAPI(this, AppConfig.APPConfigs.get("wechat").getAppid());
        iwxapi.registerApp(AppConfig.APPConfigs.get("wechat").getAppid());
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("调起支付");
        IntentFilter filter = new IntentFilter(ACTION_PAY_RESULT);
        registerReceiver(receiver, filter);
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
        progressDialog.show();
        RetrofitHelper.PlaceOrder(fee, "weChat")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(s -> {
                    Log.e(TAG, "buy: " + s);
                    if (s.startsWith("FAIL")) {
                        throw new YiException(s);
                    }
                    callWXPay(s);
                }, throwable -> {
                    Toast.makeText(this, "支付失败:" + throwable, Toast.LENGTH_SHORT).show();
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

    private OrderBean bean;

    private void callWXPay(String json) throws Exception {
        Gson gson = new Gson();
        bean = gson.fromJson(json, OrderBean.class);
        PayReq payReq = new PayReq();
        payReq.appId = bean.getAppid();
        payReq.partnerId = bean.getMch_id();
        payReq.prepayId = bean.getPrepay_id();
        payReq.packageValue = "Sign=WXPay";
        payReq.nonceStr = bean.getNonce_str();
        payReq.timeStamp = bean.getTime_start();
        payReq.sign = bean.getSign();
        iwxapi.sendReq(payReq);
    }

    @SuppressLint("CheckResult")
    private void query() {
        RetrofitHelper.QueryOrder(bean.getOut_trade_no(), "wechat")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                }, throwable -> {
                });
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case ACTION_PAY_RESULT:
                    break;
            }
        }
    };

    @SuppressLint("CheckResult")
    private void WXOrderCallback() {
        String s = "我是测试我是测试我是测试我是测试我是测试";
        RetrofitHelper.WXOrderCallback(s)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s1 -> {
                    Log.d(TAG, "WXOrderCallback s1: " + s1);
                });
    }
}
