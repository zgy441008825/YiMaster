package com.zou.yimaster.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zou.yimaster.common.AppConfig;
import com.zou.yimaster.ui.base.BaseActivity;

import org.xutils.common.util.LogUtil;

/**
 * Created by zougaoyuan on 2018/4/3
 *
 * @author zougaoyuan
 */
public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {
    private static final String TAG = "WXEntryActivity";
    private IWXAPI api;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppConfig.AppChannelInfo info = AppConfig.appChannelInfoMap.get("wechat");
        if (info == null) {
            finish();
            return;
        }
        api = WXAPIFactory.createWXAPI(this, info.getAppID());
        api.registerApp(info.getAppID());
        //注意：
        //第三方开发者如果使用透明界面来实现WXEntryActivity，需要判断handleIntent的返回值，如果返回值为false，则说明入参不合法未被SDK处理，应finish
        // 当前透明界面，避免外部通过传递非法参数的Intent导致停留在透明界面，引起用户的疑惑
        try {
            boolean result = api.handleIntent(getIntent(), this);
            if (!result) {
                LogUtil.d("参数不合法，未被SDK处理，退出");
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        api.handleIntent(data, this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
        finish();
    }

    @Override
    public void onReq(BaseReq baseReq) {
        Log.d(TAG, "onReq: " + baseReq);
    }

    @Override
    public void onResp(BaseResp baseResp) {
        Log.d(TAG, "onResp: " + baseResp);
        Gson gson = new Gson();
        String jsonString = gson.toJson(baseResp);
        result(baseResp.errCode, jsonString);
    }

    private void result(int errCode, String json) {
        Intent intent = getIntent();
        intent.putExtra("errCode", errCode);
        intent.putExtra("json", json);
        setResult(RESULT_OK, intent);
        finish();
    }
}
