package com.zou.yimaster.net;

import com.zou.yimaster.common.AppConfig;

import io.reactivex.Flowable;
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
    @POST("/GetChannelInfo")
    Flowable<AppConfig> getChannelInfo(@Query("channel") String channel);

}
