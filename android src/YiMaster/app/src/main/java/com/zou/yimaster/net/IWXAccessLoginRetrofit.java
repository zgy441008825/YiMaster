package com.zou.yimaster.net;

import java.util.Map;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by zougaoyuan on 2018/4/4
 *
 * @author zougaoyuan
 */
public interface IWXAccessLoginRetrofit {

    @GET("/sns/oauth2/access_token")
    Flowable<String> getAccessToken(@QueryMap Map<String, String> stringMap);

    @GET("/sns/oauth2/refresh_token")
    Flowable<String> refreshToken(@QueryMap Map<String, String> stringMap);

    @GET("/sns/userinfo")
    Flowable<String> getUserInfo(@Query("access_token") String access_token, @Query("openid") String openid);

}
