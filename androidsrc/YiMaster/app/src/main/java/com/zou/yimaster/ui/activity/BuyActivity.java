package com.zou.yimaster.ui.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zou.yimaster.R;
import com.zou.yimaster.common.AppConfig;
import com.zou.yimaster.common.PowerFactory;
import com.zou.yimaster.net.RetrofitHelper;
import com.zou.yimaster.ui.base.BaseActivity;
import com.zou.yimaster.utils.AnimationHelper;
import com.zou.yimaster.utils.ToastHelper;
import com.zou.yimaster.utils.YiException;

import org.json.JSONObject;
import org.reactivestreams.Publisher;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zougaoyuan on 04.27.027
 *
 * @author zougaoyuan
 */
public class BuyActivity extends BaseActivity {

    private static final String TAG = "BuyActivity";
    @BindView(R.id.Buy_1)
    Button Buy1;
    @BindView(R.id.Buy_2)
    Button Buy2;
    @BindView(R.id.Buy_3)
    Button Buy3;
    @BindView(R.id.Buy_4)
    Button Buy4;
    private IWXAPI iwxapi;
    private ProgressDialog progressDialog;

    public static final String ACTION_PAY_RESULT = "pay_result";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);
        ButterKnife.bind(this);
        if (AppConfig.APPConfigs != null) {
            iwxapi = WXAPIFactory.createWXAPI(this, AppConfig.APPConfigs.get("wechat").getAppid());
            iwxapi.registerApp(AppConfig.APPConfigs.get("wechat").getAppid());
            if (!iwxapi.isWXAppSupportAPI()) {
                ToastHelper.show("检测到设备不支持微信支付");
                finish();
            }
        }
        int offset = 100;
        AnimationHelper.translationY(Buy1, true, 500, offset);
        AnimationHelper.translationY(Buy2, true, 500, offset + 100);
        AnimationHelper.translationY(Buy3, true, 500, offset + 200);
        AnimationHelper.translationY(Buy4, true, 500, offset + 300);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("准备支付");
        IntentFilter filter = new IntentFilter(ACTION_PAY_RESULT);
        registerReceiver(receiver, filter);
    }

    private int buyCount = -1;

    private boolean buySuccess = false;

    @OnClick({R.id.BuyClose, R.id.Buy_1, R.id.Buy_2, R.id.Buy_3, R.id.Buy_4})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Buy_1:
                buyCount = 10;
                buy(getFee(5));
                break;
            case R.id.Buy_2:
                buyCount = 15;
                buy(getFee(10));
                break;
            case R.id.Buy_3:
                buyCount = 25;
                buy(getFee(15));
                break;
            case R.id.Buy_4:
                buyCount = 50;
                buy(getFee(20));
                break;
            case R.id.BuyClose:
                exit();
                break;
        }
    }

    @SuppressLint("CheckResult")
    private void buy(int fee) {
        progressDialog.show();
        Flowable<String> buyFlowable;
        if (iwxapi == null) {
            buyFlowable = RetrofitHelper.getWXChannelInfo()
                    .flatMap((Function<AppConfig.InfoBean, Publisher<String>>) infoBean -> {
                        iwxapi = WXAPIFactory.createWXAPI(BuyActivity.this, infoBean.getAppid());
                        return retrofitBuy(fee);
                    })
                    .observeOn(AndroidSchedulers.mainThread());
        } else {
            buyFlowable = retrofitBuy(fee);
        }
        buyFlowable.subscribe(s -> {
            Log.e(TAG, "buy: " + s);
            if (s.startsWith("FAIL")) {
                throw new YiException(s);
            }
            progressDialog.dismiss();
            callWXPay(s);
        }, throwable -> {
            progressDialog.dismiss();
            Toast.makeText(this, "支付失败:" + throwable, Toast.LENGTH_SHORT).show();
        });
    }

    private Flowable<String> retrofitBuy(int fee) {
        return RetrofitHelper.PlaceOrder(fee, "weChat")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread());
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

    private String out_trade_no;

    private void callWXPay(String json) throws Exception {
        ToastHelper.show("调起支付，请完成支付。");
        JSONObject object = new JSONObject(json);
        PayReq payReq = new PayReq();
        String prepayId = object.getString("prepayid");
        if (TextUtils.isEmpty(prepayId)) {
            throw new NullPointerException("prepay id is null");
        }
        out_trade_no = object.getString("out_trade_no");
        payReq.appId = object.getString("appid");
        payReq.partnerId = object.getString("partnerid");
        payReq.prepayId = prepayId;
        payReq.packageValue = object.getString("package");
        payReq.nonceStr = object.getString("noncestr");
        payReq.timeStamp = object.getString("timestamp");
        payReq.sign = object.getString("sign");
        boolean sendReq = iwxapi.sendReq(payReq);
        Log.d(TAG, "callWXPay: " + sendReq);
    }

    @SuppressLint("CheckResult")
    private void query() {
        progressDialog.setMessage("获取订单信息");
        progressDialog.show();
        RetrofitHelper.QueryOrder(out_trade_no, "wechat")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(s -> {
                    Log.e(TAG, "subscribe query: " + s);
                    progressDialog.dismiss();
                    if (!TextUtils.isEmpty(s) && s.contains("SUCCESS")) {
                        //支付成功
                        buySuccess = true;
                        ToastHelper.show("支付成功!!!");
                        exit();
                    } else {
                        throw new Exception(s);
                    }
                }, throwable -> {
                    Log.e(TAG, "query: ", throwable);
                    throwable.printStackTrace();
                    progressDialog.dismiss();
                    ToastHelper.show("错误:" + throwable);
                });
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case ACTION_PAY_RESULT:
                    query();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(receiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void exit() {
        if (buySuccess) {
            PowerFactory.getInstance().addMoney(buyCount);
        }
        finish();
    }
}
