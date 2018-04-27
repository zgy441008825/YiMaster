package com.zou.yimaster.ui.dialog;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zou.yimaster.R;
import com.zou.yimaster.common.AppConfig;
import com.zou.yimaster.net.RetrofitHelper;
import com.zou.yimaster.ui.base.BaseActivity;

import org.json.JSONObject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zougaoyuan on 04.20.020
 *
 * @author zougaoyuan
 */
public class BuyPowerActivity extends BaseActivity {

    private IWXAPI api;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bug);
        api = WXAPIFactory.createWXAPI(this, AppConfig.APPConfigs.get("weChat").getAppid());
    }

    @SuppressLint("CheckResult")
    private void startBuy() {
        RetrofitHelper.getOrderInfo("power", "wechat")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .map(s -> {
                    Log.e("get server pay params:", s);
                    JSONObject json = new JSONObject(s);
                    if (!json.has("retcode")) {
                        PayReq req = new PayReq();
                        req.appId = json.getString("appid");
                        req.partnerId = json.getString("partnerid");
                        req.prepayId = json.getString("prepayid");
                        req.nonceStr = json.getString("noncestr");
                        req.timeStamp = json.getString("timestamp");
                        req.packageValue = json.getString("package");
                        req.sign = json.getString("sign");
                        req.extData = "app data"; // optional
                        return req;
                    } else {
                        throw new Exception("获取订单信息错误");
                    }
                })
                .subscribe(payReq -> api.sendReq(payReq), throwable -> {
                });
    }
}
