package com.zou.yimaster.servlet.common;

/**
 * Created by zougaoyuan on 05.02.002
 *
 * @author zougaoyuan
 */
public enum TradeState {
    SUCCESS("SUCCESS"),//—支付成功
    REFUND("REFUND"),//—转入退款
    NOTPAY("NOTPAY"),//—未支付
    CLOSED("CLOSED"),//—已关闭
    REVOKED("REVOKED"),//—已撤销（刷卡支付）
    SYSTEMERROR("SYSTEMERROR"),//系统错误
    USERPAYING("USERPAYING");//--用户支付中

    TradeState(String state) {
    }

    public TradeState getValue(String state) {
        switch (state) {
            case "SUCCESS":
                return SUCCESS;
            case "REFUND":
                return REFUND;
            case "NOTPAY":
                return NOTPAY;
            case "CLOSED":
                return CLOSED;
            case "REVOKED":
                return REVOKED;
            case "USERPAYING":
                return USERPAYING;
            default:
                return SYSTEMERROR;
        }
    }
}
