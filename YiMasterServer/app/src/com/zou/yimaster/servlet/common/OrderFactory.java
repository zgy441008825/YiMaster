package com.zou.yimaster.servlet.common;

import com.sun.org.apache.xpath.internal.operations.Or;
import com.zou.yimaster.servlet.dao.DBManager;

import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
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
                    .setTime_start(String.valueOf(System.currentTimeMillis()));
            setSign(bean);
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

    public static void setSign(OrderBean bean) {
        if (bean == null) return;
        try {
            ChannelInfo info = DBManager.getInstance().getChannelInfo("wechat");
            String sign = getSign(orderBenToMap(bean)) + "&key=" + info.getInfo().getKey();
            bean.setSign(MD5(sign));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean checkoutSign(OrderBean bean) {
        try {
            ChannelInfo info = DBManager.getInstance().getChannelInfo("wechat");
            String sn = getSign(orderBenToMap(bean)) + "&key=" + info.getInfo().getKey();
            return MD5(sn).equals(bean.getSign());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取sign
     *
     * @param map orderBean参数
     */
    public static String getSign(Map<String, String> map) {
        List<String> keys = new ArrayList<>(map.keySet());
        Collections.sort(keys);
        StringBuilder sb = new StringBuilder();
        for (String key : keys) {
            sb.append(key)
                    .append("=")
                    .append(map.get(key))
                    .append("&");
        }
        String sign = sb.toString();
        return sign.substring(0, sign.length() - 1);
    }

    public static String MD5(String s) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(s.getBytes("utf-8"));
            return toHex(bytes).toUpperCase();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String toHex(byte[] bytes) {
        final char[] HEX_DIGITS = "0123456789ABCDEF".toCharArray();
        StringBuilder ret = new StringBuilder(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            ret.append(HEX_DIGITS[(bytes[i] >> 4) & 0x0f]);
            ret.append(HEX_DIGITS[bytes[i] & 0x0f]);
        }
        return ret.toString();
    }

    /**
     * 将OrderBean转为Map
     */
    public static Map<String, String> orderBenToMap(OrderBean bean) throws Exception {
        Map<String, String> orderMap = new LinkedHashMap<>();
        for (Field field : bean.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if (field.get(bean) != null && !String.valueOf(field.get(bean)).isEmpty()) {
                orderMap.put(field.getName(), String.valueOf(field.get(bean)));
                System.out.println(field.getName() + ":" + field.get(bean));
            }
        }
        return orderMap;
    }
}
