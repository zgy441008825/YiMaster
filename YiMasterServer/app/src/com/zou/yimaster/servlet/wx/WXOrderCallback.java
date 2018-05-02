package com.zou.yimaster.servlet.wx;

import com.zou.yimaster.servlet.api.BaseServlet;
import com.zou.yimaster.servlet.common.OrderBean;
import com.zou.yimaster.servlet.dao.DBManager;
import com.zou.yimaster.servlet.utils.XMLMapTools;

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

    private static final Object lock = new Object();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        super.doPost(request, response);
        String s;
        try {
            s = getBody(request);
            System.out.println("WXOrderCallback:" + s);
        } catch (IOException e) {
            e.printStackTrace();
            result(WXPayUtils.RESULT_FAIL, "No");
            return;
        }
        String returnCode = XMLMapTools.analyseWXResultBean(s, "return_code");
        if (WXPayUtils.RESULT_OK.equals(returnCode)) {
            String OrderNo = XMLMapTools.analyseWXResultBean(s, "out_trade_no");
            OrderBean bean = DBManager.getInstance().getOrderBean(OrderNo);
            if (bean == null) {
                result(WXPayUtils.RESULT_FAIL, "");
                return;
            }
            if (bean.getTrade_state() == null || !bean.getTrade_state().equals(WXPayUtils.RESULT_OK)) {
                synchronized (lock) {
                    bean.setNonce_str(XMLMapTools.analyseWXResultBean(s, "nonce_str"));
                    bean.setSign(XMLMapTools.analyseWXResultBean(s, "sign"));
                    bean.setTrade_state(returnCode);
                    bean.setBank_type(XMLMapTools.analyseWXResultBean(s, "bank_type"));
                    bean.setTransaction_id(XMLMapTools.analyseWXResultBean(s, "transaction_id"));
                    bean.setTime_end(XMLMapTools.analyseWXResultBean(s, "time_end"));
                    DBManager.getInstance().saveOrUpdateOrder(bean);
                    result(bean.getTrade_state(), XMLMapTools.analyseWXResultBean(s, "return_msg"));
                    return;
                }
            }
        }
        result(WXPayUtils.RESULT_FAIL, "No");
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
        writer.println(XMLMapTools.mapToXml(resultMap));
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
