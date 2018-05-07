package com.zou.yimaster.common.dao;

/**
 * Created by zougaoyuan on 04.20.020
 * 订单信息
 *
 * @author zougaoyuan
 */
public class OrderBean {

    /**
     * APPID
     */
    private String appid;

    /**
     * 商户ID
     */
    private String mch_id;

    /**
     * 随机数
     */
    private String nonce_str;

    /**
     * 签名
     */
    private String sign;

    /**
     * 签名类型——默认MD5
     */
    private String sign_type = "MD5";

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
    private String spbill_create_ip;

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
    private String notify_url = "http://localhost:8080/wx/WXOrderCallback";

    /**
     * 交易类型 APP
     */
    private String trade_type = "APP";

    /**
     * ***重要 为请求微信统一下单地址成功后返回的预支付回话标识,用于后续接口调用中使用，该值有效期为2小时
     */
    private String prepay_id;

    private String return_code;

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

    public String getAppid() {
        return appid;
    }

    public OrderBean setAppid(String appid) {
        this.appid = appid;
        return this;
    }

    public String getMch_id() {
        return mch_id;
    }

    public OrderBean setMch_id(String mch_id) {
        this.mch_id = mch_id;
        return this;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public OrderBean setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
        return this;
    }

    public String getSign() {
        return sign;
    }

    public OrderBean setSign(String sign) {
        this.sign = sign;
        return this;
    }

    public String getSign_type() {
        return sign_type;
    }

    public OrderBean setSign_type(String sign_type) {
        this.sign_type = sign_type;
        return this;
    }

    public String getBody() {
        return body;
    }

    public OrderBean setBody(String body) {
        this.body = body;
        return this;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public OrderBean setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
        return this;
    }

    public int getTotal_fee() {
        return total_fee;
    }

    public OrderBean setTotal_fee(int total_fee) {
        this.total_fee = total_fee;
        return this;
    }

    public String getSpbill_create_ip() {
        return spbill_create_ip;
    }

    public OrderBean setSpbill_create_ip(String spbill_create_ip) {
        this.spbill_create_ip = spbill_create_ip;
        return this;
    }

    public String getTime_start() {
        return time_start;
    }

    public OrderBean setTime_start(String time_start) {
        this.time_start = time_start;
        return this;
    }

    public String getTime_expire() {
        return time_expire;
    }

    public OrderBean setTime_expire(String time_expire) {
        this.time_expire = time_expire;
        return this;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public OrderBean setNotify_url(String notify_url) {
        this.notify_url = notify_url;
        return this;
    }

    public String getTrade_type() {
        return trade_type;
    }

    public OrderBean setTrade_type(String trade_type) {
        this.trade_type = trade_type;
        return this;
    }

    public String getPrepay_id() {
        return prepay_id;
    }

    public OrderBean setPrepay_id(String prepay_id) {
        this.prepay_id = prepay_id;
        return this;
    }

    public String getReturn_code() {
        return return_code;
    }

    public OrderBean setReturn_code(String return_code) {
        this.return_code = return_code;
        return this;
    }

    public String getResult_code() {
        return result_code;
    }

    public OrderBean setResult_code(String result_code) {
        this.result_code = result_code;
        return this;
    }

    public String getErr_code() {
        return err_code;
    }

    public OrderBean setErr_code(String err_code) {
        this.err_code = err_code;
        return this;
    }

    public String getErr_code_des() {
        return err_code_des;
    }

    public OrderBean setErr_code_des(String err_code_des) {
        this.err_code_des = err_code_des;
        return this;
    }

    public String getOpenid() {
        return openid;
    }

    public OrderBean setOpenid(String openid) {
        this.openid = openid;
        return this;
    }

    public String getBank_type() {
        return bank_type;
    }

    public OrderBean setBank_type(String bank_type) {
        this.bank_type = bank_type;
        return this;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public OrderBean setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
        return this;
    }

    public String getTime_end() {
        return time_end;
    }

    public OrderBean setTime_end(String time_end) {
        this.time_end = time_end;
        return this;
    }
}
