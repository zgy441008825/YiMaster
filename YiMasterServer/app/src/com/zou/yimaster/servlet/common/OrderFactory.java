package com.zou.yimaster.servlet.common;

import com.zou.yimaster.servlet.dao.DBManager;
import com.zou.yimaster.servlet.utils.ParamStringTools;
import com.zou.yimaster.servlet.wx.WXPayUtils;

import java.util.Map;
import java.util.Random;

/**
 * Created by zougaoyuan on 04.20.020
 * 订单工厂，用于产生一条本地订单信息
 *
 * @author zougaoyuan
 */
public class OrderFactory {

    private static final String SOURCES = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";

    /**
     * 创建一个订单
     *
     * @param body             说明
     * @param total_fee        价格（单位：分）
     * @param spbill_create_ip 用户IP
     * @param ch               支付渠道
     */
    public static OrderBean createOrder(String body, int total_fee, String spbill_create_ip, String ch) {
        try {
            OrderBean bean = new OrderBean();
            ChannelInfo info = DBManager.getInstance().getChannelInfo(ch);
            bean.setAppid(info.getInfo().getAppid())
                    .setMch_id(info.getInfo().getMchId())
                    .setNonce_str(getNonce())
                    .setBody(body)
                    .setOut_trade_no(makeOrderNO())
                    .setTotal_fee(total_fee)
                    .setSpbill_create_ip(spbill_create_ip)
                    .setTime_start(String.valueOf(System.currentTimeMillis()))
                    .setSign(ParamStringTools.getSign(ParamStringTools.orderBenToMap(bean)));
            DBManager.getInstance().saveOrUpdateOrder(bean);
            return bean;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 生成订单号
     */
    private static synchronized String makeOrderNO() {
        return String.format("YI%1$s%2$s", System.currentTimeMillis(), new Random().nextInt(9));
    }

    /**
     * @return 返回一个随机数
     */
    public static String getNonce(int length) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(SOURCES.charAt(random.nextInt(SOURCES.length())));
        }
        return sb.toString();
    }

    /**
     * @return 返回一个32位随机数
     */
    public static String getNonce() {
        return getNonce(32);
    }

    private static final Object lock = new Object();

    /**
     * 解析查询或者微信回调的订单支付状态
     */
    public static OrderBean updateOrderTradeState(String xml) throws Exception {
        OrderBean bean;
        Map<String, String> resultMap = ParamStringTools.xml2Map(xml);
        if (resultMap == null) throw new YiException("查询返回数据错误");
        String return_code = resultMap.get("return_code");
        if (!ParamStringTools.checkoutSign(resultMap)) {
            throw new YiException("查询订单错误,签名异常");
        }
        if (!WXPayUtils.RESULT_OK.equals(return_code))
            throw new YiException(resultMap.get("return_msg"));
        String out_trade_no = resultMap.get("out_trade_no");
        bean = DBManager.getInstance().getOrderBean(out_trade_no);
        if (bean == null) {
            throw new YiException("订单号错误");
        }
        String result_code = resultMap.get("result_code");//业务结果
        if (!WXPayUtils.RESULT_OK.equals(result_code)) {
            bean.setTrade_state(result_code);
            bean.setErr_code(resultMap.get("err_code"));
            bean.setErr_code_des(resultMap.get("err_code_des"));
            DBManager.getInstance().saveOrUpdateOrder(bean);
            return bean;
        }
        synchronized (lock) {
            bean.setNonce_str(resultMap.get("nonce_str"));
            bean.setSign(resultMap.get("sign"));
            bean.setTrade_state(result_code);
            bean.setOpenid(resultMap.get("openid"));
            bean.setTrade_type(resultMap.get("trade_type"));
            bean.setBank_type(resultMap.get("bank_type"));
            bean.setTotal_fee(Integer.valueOf(resultMap.get("total_fee")));
            bean.setTransaction_id(resultMap.get("transaction_id"));
            bean.setTime_end(resultMap.get("time_end"));
            DBManager.getInstance().saveOrUpdateOrder(bean);
        }
        return bean;
    }

}
