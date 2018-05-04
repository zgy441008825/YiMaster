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

    private String value;

    TradeState(String state) {
        setValue(state);
    }

    public String getValue() {
        return value;
    }

    private void setValue(String state) {
        switch (state) {
            case "SUCCESS":
                value = "SUCCESS";
            case "REFUND":
                value = "REFUND";
            case "NOTPAY":
                value = "NOTPAY";
            case "CLOSED":
                value = "CLOSED";
            case "REVOKED":
                value = "REVOKED";
            case "USERPAYING":
                value = "USERPAYING";
            default:
                value = "SYSTEMERROR";
        }
    }
}
