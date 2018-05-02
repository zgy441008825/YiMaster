package com.zou.yimaster.servlet.common;

/**
 * Created by zougaoyuan on 05.02.002
 *
 * @author zougaoyuan
 */
public class WXResultBean {
    private String return_code;
    private String return_msg;
    private String appid;
    private String mch_id;
    private String nonce_str;
    private String sign;
    private String result_code;
    private String prepay_id;
    private String trade_type;

    public String getReturn_code() {
        return return_code;
    }

    public WXResultBean setReturn_code(String return_code) {
        this.return_code = return_code;
        return this;
    }

    public String getReturn_msg() {
        return return_msg;
    }

    public WXResultBean setReturn_msg(String return_msg) {
        this.return_msg = return_msg;
        return this;
    }

    public String getAppid() {
        return appid;
    }

    public WXResultBean setAppid(String appid) {
        this.appid = appid;
        return this;
    }

    public String getMch_id() {
        return mch_id;
    }

    public WXResultBean setMch_id(String mch_id) {
        this.mch_id = mch_id;
        return this;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public WXResultBean setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
        return this;
    }

    public String getSign() {
        return sign;
    }

    public WXResultBean setSign(String sign) {
        this.sign = sign;
        return this;
    }

    public String getResult_code() {
        return result_code;
    }

    public WXResultBean setResult_code(String result_code) {
        this.result_code = result_code;
        return this;
    }

    public String getPrepay_id() {
        return prepay_id;
    }

    public WXResultBean setPrepay_id(String prepay_id) {
        this.prepay_id = prepay_id;
        return this;
    }

    public String getTrade_type() {
        return trade_type;
    }

    public WXResultBean setTrade_type(String trade_type) {
        this.trade_type = trade_type;
        return this;
    }
}
