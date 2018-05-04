package com.zou.yimaster.servlet.wx;

import com.zou.yimaster.servlet.api.BaseServlet;
import com.zou.yimaster.servlet.common.OrderBean;
import com.zou.yimaster.servlet.common.OrderFactory;
import com.zou.yimaster.servlet.utils.ParamStringTools;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by zougaoyuan on 04.20.020
 *
 * @author zougaoyuan
 */
@WebServlet(name = "WXOrderCallback", urlPatterns = "/wx/WXOrderCallback")
public class WXOrderCallback extends BaseServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        super.doPost(request, response);
        try {
            OrderBean bean = OrderFactory.updateOrderTradeState(getBody(request));
            result(bean.getTrade_state(), "");
        } catch (Exception e) {
            e.printStackTrace();
            result(WXPayUtils.RESULT_FAIL, "No");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        super.doGet(request, response);
    }

    private void result(String return_code, String return_msg) {
        System.out.println("WXOrderCallback:" + return_code + " " + return_msg);
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("return_code", return_code);
        resultMap.put("return_msg", return_msg);
        writer.println(ParamStringTools.mapToXml(resultMap));
    }

    private String getBody(HttpServletRequest request) throws IOException {
        BufferedReader br = request.getReader();
        String str;
        StringBuilder wholeStr = new StringBuilder();
        while ((str = br.readLine()) != null) {
            wholeStr.append(str);
        }
        return wholeStr.toString();
    }
}
