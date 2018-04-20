package com.zou.yimaster.servlet.common;

/**
 * Created by zougaoyuan on 04.20.020
 * 订单信息
 *
 * @author zougaoyuan
 */
public class OrderInfoBean {

    /**
     * APPID
     */
    private String appid;

    /**
     * 商户ID
     */
    private String mchid;

    /**
     * 随机数
     */
    private String nonce_str;

    /**
     * 签名
     */
    private String sign;

    /**
     * 签名类型
     */
    private String sign_type;

    /**
     * 商品描述
     */
    private String body;

    /**
     * 商户订单号
     */
    private String out_trade_no;


    /**
     * 总金额(单位：分)
     */
    private int total_fee;

    /**
     * 终端IP 用户端实际ip
     */
    private int spbill_create_ip;

    /**
     * 交易起始时间 yyyyMMddHHmmss
     */
    private String time_start;

    /**
     * 交易结束时间 yyyyMMddHHmmss
     */
    private String time_expire;

    /**
     * 接收微信支付异步通知回调地址，通知url必须为直接可访问的url，不能携带参数。
     */
    private String notify_url;

    /**
     * 交易类型 APP
     */
    private String trade_type = "APP";

    /**
     * ***重要 为请求微信统一下单地址成功后返回的预支付回话标识,用于后续接口调用中使用，该值有效期为2小时
     */
    private String prepay_id;

    /**
     * 业务结果 SUCCESS/FAIL
     */
    private String result_code;

    /**
     * 错误代码
     */
    private String err_code;

    /**
     * 错误代码描述
     */
    private String err_code_des;

    /**
     * 用户标识
     */
    private String openid;

    /**
     * 付款银行
     */
    private String bank_type;

    /**
     * 微信支付订单号
     */
    private String transaction_id;

    /**
     * 支付完成时间
     */
    private String time_end;
}
