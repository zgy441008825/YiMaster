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
public interface YiServer {

    /**
     * 获取channel平台的相关信息
     *
     * @return json 包含APPID secret等
     */
    @POST("/api/getChannel")
    Flowable<Map<String, AppConfig.InfoBean>> getAllChannelInfo();

    @POST("/api/getChannel")
    Flowable<AppConfig> getChannelInfo(@Query("channel") String channel);

    @POST("/api/SaveRecord")
    Flowable<String> saveRecord(@Body UserGameRecord record);

    /**
     * 查询订单
     *
     * @param orderNo 订单编号
     * @param channel 支付渠道
     */
    @POST("/api/QueryOrder")
    Flowable<String> QueryOrder(@Query("orderNo") String orderNo, @Query("channel") String channel);

    /**
     * 统一下单接口
     *
     * @param body 说明
     * @param fee  价格（单位 分）
     */
    @POST("/api/place")
    Flowable<String> PlaceOrder(@Query("body") String body, @Query("fee") int fee, @Query("channel") String channel);

    @POST("/wx/WXOrderCallback")
    Flowable<String> WXOrderCallback(@Body String body);
}
