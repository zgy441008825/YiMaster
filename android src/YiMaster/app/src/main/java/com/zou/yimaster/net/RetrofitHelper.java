package com.zou.yimaster.net;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import okhttp3.OkHttpClient;
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
        OkHttpClient client = new OkHttpClient.Builder()
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

    /**
     * 获取平台相关信息
     */
    public static Flowable<String> getChannelInfo(String channel) {
        Retrofit retrofit = getYiServiceBaseRetrofit();
        IYiServerRetrofit yiServerRetrofit = retrofit.create(IYiServerRetrofit.class);
        return yiServerRetrofit.getChannelInfo(channel);
    }

}
