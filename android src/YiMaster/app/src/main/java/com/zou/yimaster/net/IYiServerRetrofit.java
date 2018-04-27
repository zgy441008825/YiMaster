package com.zou.yimaster.net;

import com.zou.yimaster.common.AppConfig;
import com.zou.yimaster.common.dao.UserGameRecord;

import java.util.Map;

import io.reactivex.Flowable;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by zougaoyuan on 2018/4/4
 *
 * @author zougaoyuan
 */
public interface IYiServerRetrofit {

    /**
     * 获取channel平台的相关信息
     *
     * @param channel 平台：如微信
     *
     * @return json 包含APPID secret等
     */
    @POST("/yimaster_war/GetChannelInfo")
    Flowable<Map<String, AppConfig.InfoBean>> getAllChannelInfo();

    @POST("/yimaster_war/GetChannelInfo")
    Flowable<AppConfig> getChannelInfo(@Query("channel") String channel);


    @POST("/yimaster_war/SaveRecord")
    Flowable<String> saveRecord(@Body UserGameRecord record);

    @POST("/yimaster_war/GetOrderInfo")
    Flowable<String> getOrderInfo(@Query("type") String type, @Query("channel") String channel);

}
