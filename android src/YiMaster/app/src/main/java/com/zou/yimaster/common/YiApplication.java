package com.zou.yimaster.common;

import android.app.Application;

import org.xutils.x;

/**
 * Created by zougy on 03.25.025
 */
public class YiApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);

        /*
         * 初始化common库
         * 参数1:上下文，不能为空
         * 参数2:友盟 AppKey
         * 参数3:友盟 Channel
         * 参数4:设备类型，UMConfigure.DEVICE_TYPE_PHONE为手机、UMConfigure.DEVICE_TYPE_BOX为盒子，默认为手机
         * 参数5:Push推送业务的secret
         */
        /*UMConfigure.init(this, "58edcfeb310c93091c000be2", "umeng", UMConfigure.DEVICE_TYPE_PHONE,
                "5965ee00734be40b580001a0");
        UMConfigure.setLogEnabled(true);
        UMConfigure.setEncryptEnabled(true);
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_GAME);
        MobclickAgent.setSecret(this, "s10bacedtyz");
        UMShareAPI.init(this,"APP Key");
        PushAgent mPushAgent = PushAgent.getInstance(this);
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
            }
            @Override
            public void onFailure(String s, String s1) {
            }
        });*/
    }
}
