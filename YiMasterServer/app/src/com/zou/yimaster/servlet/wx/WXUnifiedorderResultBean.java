package com.zou.yimaster.servlet.wx;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * Created by zougaoyuan on 04.27.027
 *
 * @author zougaoyuan
 */
@Root(name = "xml")
public class WXUnifiedorderResultBean {

    @Attribute(name = "return_code")
    public String return_code;

    @Attribute(name = "result_code")
    public String result_code;

    @Attribute(name = "return_msg")
    public String return_msg;

    @Attribute(name = "appid")
    public String appid;

    @Attribute(name = "mch_id")
    public String mch_id;

    @Attribute(name = "nonce_str")
    public String nonce_str;

    @Attribute(name = "sign")
    public String sign;

    @Attribute(name = "prepay_id")
    public String prepay_id;

    @Attribute(name = "trade_type")
    public String trade_type;

    @Attribute(name = "device_info")
    public String device_info;

    @Attribute(name = "err_code")
    public String err_code;

    @Attribute(name = "err_code_des")
    public String err_code_des;

    @Override
    public String toString() {
        return "WXUnifiedorderResultBean{" +
                "return_code='" + return_code + '\'' +
                ", result_code='" + result_code + '\'' +
                ", return_msg='" + return_msg + '\'' +
                ", appid='" + appid + '\'' +
                ", mch_id='" + mch_id + '\'' +
                ", nonce_str='" + nonce_str + '\'' +
                ", sign='" + sign + '\'' +
                ", prepay_id='" + prepay_id + '\'' +
                ", trade_type='" + trade_type + '\'' +
                ", device_info='" + device_info + '\'' +
                ", err_code='" + err_code + '\'' +
                ", err_code_des='" + err_code_des + '\'' +
                '}';
    }
}
