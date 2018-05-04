package com.zou.yimaster.servlet.utils;

import com.google.gson.internal.LinkedTreeMap;
import com.zou.yimaster.servlet.common.OrderBean;
import com.zou.yimaster.servlet.common.OrderFactory;
import com.zou.yimaster.servlet.common.YiException;
import com.zou.yimaster.servlet.dao.DBManager;
import com.zou.yimaster.servlet.wx.WXPayUtils;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.StringReader;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zougaoyuan on 05.02.002
 * 和微信服务器交互的参数帮助类
 *
 * @author zougaoyuan
 */
public class ParamStringTools {

    private static String sandbox_signkey;

    public static boolean sandboxMode = false;

    /**
     * 将XML参数转为Map
     */
    public static Map<String, String> xml2Map(String xml) {
        if (isEmpty(xml)) return null;
        try {
            Map<String, String> map = new LinkedTreeMap<>();
            SAXReader reader = new SAXReader();
            Document document = reader.read(new StringReader(xml));
            Element bookStore = document.getRootElement();
            // 通过element对象的elementIterator方法获取迭代器
            Iterator it = bookStore.elementIterator();
            while (it.hasNext()) {
                Element element = (Element) it.next();
                map.put(element.getName().trim(), element.getStringValue().trim());
            }
            return map;
        } catch (DocumentException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将订单信息中的参数打包为微信需要的XML
     */
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

    public static String getSandboxKey(Map<String, String> map) {
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
        String sign = sb.toString();
        sign = sign.substring(0, sign.length() - 1);
        return MD5(sign);
    }

    /**
     * 获取签名,自动跳过sign字段,在末尾加Key
     *
     * @param map orderBean参数
     */
    public static String getSign(Map<String, String> map) {
        if (sandboxMode && isEmpty(sandbox_signkey)) {
            getSandboxNewSign();
        }
        List<String> keys = new ArrayList<>(map.keySet());
        Collections.sort(keys);
        StringBuilder sb = new StringBuilder();
        for (String key : keys) {
            if (isEmpty(key) || key.equals("sign") || key.equals("key")) continue;
            if (isEmpty(map.get(key))) continue;
            sb.append(key)
                    .append("=")
                    .append(map.get(key))
                    .append("&");
        }
        if (sandboxMode) {
            sb.append("key=").append(sandbox_signkey);
        } else {
            sb.append("key=")
                    .append(DBManager.getInstance().getChannelInfo("wechat").getInfo().getApp_key());
        }
        return MD5(sb.toString());
    }

    /**
     * 获取沙盒测试用的sign
     */
    private static void getSandboxNewSign() {
        Map<String, String> map = new HashMap<>();
        map.put("mch_id", DBManager.getInstance().getChannelInfo("wechat").getInfo().getMchId());
        map.put("nonce_str", OrderFactory.getNonce());
        String sign = getSandboxKey(map);
        map.put("sign", sign);
        WXPayUtils.getSignKey(mapToXml(map))
                .subscribe(s -> {
                    Map<String, String> resultMap = xml2Map(s);
                    if (resultMap == null) throw new YiException("获取沙盒sign错误:" + s);
                    if (resultMap.containsKey("return_code") && resultMap.get("return_code").equals(WXPayUtils
                            .RESULT_OK)) {
                        sandbox_signkey = resultMap.get("sandbox_signkey");
                    } else {
                        throw new YiException(resultMap.get("return_msg"));
                    }
                }, Throwable::printStackTrace);
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
            String sn = ParamStringTools.getSign(map);
            return map.get("sign").equals(sn);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
