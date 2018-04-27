package com.zou.yimaster.net;

import com.zou.yimaster.common.AppConfig;
import com.zou.yimaster.common.dao.UserGameRecord;

import java.util.LongSummaryStatistics;
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

    private static Retrofit getWXBaseRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("https://api.weixin.qq.com")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

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

    public static Flowable<String> wxAccessLogin(Map<String, String> stringMap) {
        Retrofit retrofit = getWXBaseRetrofit();
        IWXAccessLoginRetrofit loginRetrofit = retrofit.create(IWXAccessLoginRetrofit.class);
        return loginRetrofit.getAccessToken(stringMap);
    }

    /**
     * 刷新AccessToken
     *
     * @param stringMap 必须包含appid,grant_type="refresh_token",refresh_token
     */
    public static Flowable<String> wxRefreshToken(Map<String, String> stringMap) {
        Retrofit retrofit = getWXBaseRetrofit();
        IWXAccessLoginRetrofit refreshRetrofit = retrofit.create(IWXAccessLoginRetrofit.class);
        return refreshRetrofit.refreshToken(stringMap);
    }

    public static Flowable<String> wxGetUserInfo(String access_token, String openid) {
        Retrofit retrofit = getWXBaseRetrofit();
        IWXAccessLoginRetrofit refreshRetrofit = retrofit.create(IWXAccessLoginRetrofit.class);
        return refreshRetrofit.getUserInfo(access_token, openid);
    }

    public static IYiServerRetrofit getYiServerRetrofit() {
        Retrofit retrofit = getYiServiceBaseRetrofit();
        return retrofit.create(IYiServerRetrofit.class);
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

    public static Flowable<String> saveRecord(UserGameRecord record) {
        return getYiServerRetrofit().saveRecord(record);
    }

    public static Flowable<String> getOrderInfo(String type,String channel) {
        return getYiServerRetrofit().getOrderInfo(type,channel);
    }

}
