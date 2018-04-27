package com.zou.yimaster.servlet.wx;

import java.util.Map;

import io.reactivex.Flowable;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * Created by zougaoyuan on 04.27.027
 *
 * @author zougaoyuan
 */
public class WXPayUtils {

    public static final String RESULT_OK = "SUCCESS";
    public static final String RESULT_FAIL = "FAIL";

    private static String WXBaseUrl = "https://api.mch.weixin.qq.com/";

    public static Retrofit getWXPayBaseRetrofit() {
        OkHttpClient client = new OkHttpClient();
        return new Retrofit.Builder()
                .client(client)
//                .addConverterFactory(SimpleXmlConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(WXBaseUrl)
                .build();
    }

    /**
     * 获取微信支付相关接口
     */
    private static WXPayServer getWXPayServer() {
        Retrofit retrofit = getWXPayBaseRetrofit();
        return retrofit.create(WXPayServer.class);
    }

    /**
     * 微信统一下单接口
     */
    public static Flowable<String> payUnifiedorder(String param) {
        return getWXPayServer().payUnifiedorder(param);
    }

    /**
     * 查询
     */
    public static Flowable<String> payOrderquery(String param) {
        return getWXPayServer().payOrderquery(param);
    }

}
