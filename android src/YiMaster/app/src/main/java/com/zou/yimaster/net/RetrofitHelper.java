package com.zou.yimaster.net;

import com.zou.yimaster.common.AppConfig;
import com.zou.yimaster.common.dao.UserGameRecord;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by zougaoyuan on 2018/4/4
 *
 * @author zougaoyuan
 */
public class RetrofitHelper {

    private static Retrofit getYiServiceBaseRetrofit() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .readTimeout(5, TimeUnit.SECONDS)
                .connectTimeout(3, TimeUnit.SECONDS)
                .build();
        return new Retrofit.Builder()
                .baseUrl("http://192.168.3.148:8080")
                .client(client)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    private static YiServer getYiServerRetrofit() {
        Retrofit retrofit = getYiServiceBaseRetrofit();
        return retrofit.create(YiServer.class);
    }

    /**
     * 获取平台相关信息
     */
    public static Flowable<Map<String, AppConfig.InfoBean>> getAllChannelInfo() {
        return getYiServerRetrofit().getAllChannelInfo();
    }

    public static Flowable<AppConfig> getAllChannelInfo(String channel) {
        return getYiServerRetrofit().getChannelInfo(channel);
    }

    /**
     * 保存一条用户成绩
     */
    public static Flowable<String> saveRecord(UserGameRecord record) {
        return getYiServerRetrofit().saveRecord(record);
    }

    public static Flowable<String> getOrderInfo(String type, String channel) {
        return getYiServerRetrofit().QueryOrder(type, channel);
    }

    public static Flowable<String> PlaceOrder(int fee, String channel) {
        String body = "购买:" + fee;
        return getYiServerRetrofit().PlaceOrder(body, fee, channel);
    }

}
