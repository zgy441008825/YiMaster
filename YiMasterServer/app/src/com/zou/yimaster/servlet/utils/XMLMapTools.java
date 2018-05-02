package com.zou.yimaster.servlet.utils;

import com.zou.yimaster.servlet.common.OrderBean;
import com.zou.yimaster.servlet.common.OrderFactory;
import com.zou.yimaster.servlet.dao.DBManager;

import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zougaoyuan on 05.02.002
 *
 * @author zougaoyuan
 */
public class XMLMapTools {

    /**
     * 在xml中查找key对应的值
     */
    public static String analyseWXResultBean(String s, String key) {
        Pattern pattern = Pattern.compile("<\\/.*?>");
        Pattern pattern1 = Pattern.compile("CDATA\\[.*?\\]");
        Matcher matcher = pattern.matcher(s);
        Matcher matcher1 = pattern1.matcher(s);
        while (matcher.find() && matcher1.find()) {
            String keyV = matcher.group().replace("</", "").replace(">", "");
            String value = matcher1.group().replace("CDATA[", "").replace("]", "");
            if (key.equals(keyV))
                return value;
        }
        return null;
    }

    public static Map<String, String> xml2Map(String xml) {
        if (isEmpty(xml)) return null;
        try {
            Pattern pattern = Pattern.compile("<\\/.*?>");
            Pattern pattern1 = Pattern.compile("CDATA\\[.*?\\]");
            Matcher matcher = pattern.matcher(xml);
            Matcher matcher1 = pattern1.matcher(xml);
            Map<String, String> map = new HashMap<>();
            while (matcher.find() && matcher1.find()) {
                String key = matcher.group().replace("</", "").replace(">", "");
                String value = matcher1.group().replace("CDATA[", "").replace("]", "");
                map.put(key, value);
            }
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String orderToXML(OrderBean bean) throws Exception {
        return mapToXml(orderBenToMap(bean));
    }

    public static String mapToXml(Map<String, String> map) {
        StringBuilder sb = new StringBuilder();
        List<String> keys = new ArrayList<>(map.keySet());
        Collections.sort(keys);
        sb.append("<xml>");
        for (String key : keys) {
            sb.append(String.format("<%1$s><![CDATA[%2$s]]></%1$s>", key, map.get(key)));
        }
        sb.append("</xml>");
        return sb.toString();
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
            }
        }
        return orderMap;
    }

    public static void setOrderSign(OrderBean bean) throws Exception {
        if (bean == null) return;
        bean.setSign(getSign(orderBenToMap(bean)));
    }

    /**
     * 获取签名,自动跳过sign字段,在末尾加Key
     *
     * @param map orderBean参数
     */
    public static String getSign(Map<String, String> map) {
        List<String> keys = new ArrayList<>(map.keySet());
        Collections.sort(keys);
        StringBuilder sb = new StringBuilder();
        for (String key : keys) {
            if (key.equals("sign") || key.equals("key")) continue;
            sb.append(key)
                    .append("=")
                    .append(map.get(key))
                    .append("&");
        }
        sb.append("key=")
                .append(DBManager.getInstance().getChannelInfo("wechat").getInfo().getApp_key());
        String sign = sb.toString();
        return MD5(sign);
    }

    public static boolean isEmpty(String s) {
        return s == null || s.length() == 0;
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
     * 校验Map中的sign知否正确
     */
    public static boolean checkoutSign(Map<String, String> map) {
        try {
            if (!map.containsKey("sign")) return false;
            String sn = XMLMapTools.getSign(map);
            return map.get("sign").equals(sn);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
