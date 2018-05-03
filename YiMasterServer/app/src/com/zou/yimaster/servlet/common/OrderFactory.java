package com.zou.yimaster.servlet.common;

import com.zou.yimaster.servlet.dao.DBManager;
import com.zou.yimaster.servlet.utils.XMLMapTools;

import java.security.MessageDigest;
import java.util.Map;
import java.util.Random;

/**
 * Created by zougaoyuan on 04.20.020
 * 订单工厂，用于产生一条本地订单信息
 *
 * @author zougaoyuan
 */
public class OrderFactory {

    public static final String SOURCES = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";

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
                    .setNonce_str(getNonce(32))
                    .setBody(body)
                    .setOut_trade_no(makeOrderNO())
                    .setTotal_fee(total_fee)
                    .setSpbill_create_ip(spbill_create_ip)
                    .setTime_start(String.valueOf(System.currentTimeMillis()))
                    .setSign(XMLMapTools.getSign(XMLMapTools.orderBenToMap(bean)));
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


}
