package com.zou.yimaster.servlet.common;

import com.zou.yimaster.servlet.dao.ChannelInfo;
import com.zou.yimaster.servlet.dao.DBManager;

import java.util.Random;

/**
 * Created by zougaoyuan on 04.20.020
 * 订单工厂，用于产生一条本地订单信息
 *
 * @author zougaoyuan
 */
public class OrderFactroy {

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
        OrderBean bean = new OrderBean();
        ChannelInfo info = DBManager.getInstance().getChannelInfo(ch);
        bean.setAppid(info.getInfo().getAppid())
                .setBody(body)
                .setMchid("")
                .setNonce_str(getNonce(32))
                .setOut_trade_no(makeOrderNO())
                .setSign("")
                .setSign_type("MD5")
                .setTotal_fee(total_fee)
                .setTime_start(String.valueOf(System.currentTimeMillis()))
                .setSpbill_create_ip(spbill_create_ip);
        DBManager.getInstance().saveOrUpdateOrder(bean);
        return bean;
    }

    /**
     * 生成订单号
     *
     * @return
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
