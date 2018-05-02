package com.zou.yimaster.servlet.utils;

import com.zou.yimaster.servlet.common.OrderBean;
import com.zou.yimaster.servlet.common.OrderFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zougaoyuan on 05.02.002
 *
 * @author zougaoyuan
 */
public class AnalyseXML {

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

    public static String orderToXML(OrderBean bean) throws Exception {
        return mapToXml(OrderFactory.orderBenToMap(bean));
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
        System.out.println("mapToXml:" + sb);
        return sb.toString();
    }


}
