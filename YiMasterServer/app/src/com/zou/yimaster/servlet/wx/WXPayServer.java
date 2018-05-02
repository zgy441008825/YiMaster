package com.zou.yimaster.servlet.wx;

import io.reactivex.Flowable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by zougaoyuan on 04.27.027
 * 微信支付相关接口
 *
 * @author zougaoyuan
 */
interface WXPayServer {

    /**
     * 统一下单接口
     *
     * @see https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=9_1
     */
    @POST("/sandboxnew/pay/unifiedorder")
    Flowable<String> payUnifiedorder(@Body String param);

    /**
     * 查询订单
     *
     * @see https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=9_2&index=4
     */
    @POST("/sandboxnew/pay/orderquery")
    Flowable<String> payOrderquery(@Body String param);

}
