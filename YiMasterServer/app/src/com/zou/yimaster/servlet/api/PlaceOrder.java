package com.zou.yimaster.servlet.api;

import com.google.gson.Gson;
import com.zou.yimaster.servlet.common.OrderBean;
import com.zou.yimaster.servlet.common.OrderFactory;
import com.zou.yimaster.servlet.common.YiException;
import com.zou.yimaster.servlet.dao.DBManager;
import com.zou.yimaster.servlet.utils.AnalyseXML;
import com.zou.yimaster.servlet.utils.NetworkUtil;
import com.zou.yimaster.servlet.wx.WXPayUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by zougaoyuan on 04.25.025
 * 下单
 *
 * @author zougaoyuan
 */
@WebServlet(name = "PlaceOrder", urlPatterns = "/api/place")
public class PlaceOrder extends BaseServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        super.doPost(request, response);
        String body = request.getParameter("body");//产品描述
        String channel = request.getParameter("channel");//订单渠道
        int totalFee = Integer.valueOf(request.getParameter("fee"));//商品价格
        String ip = NetworkUtil.getIpAddress(request);
        OrderBean bean = OrderFactory.createOrder(body, totalFee, ip, "wechat");
        if (bean == null) {
            writer.println("生成商户订单失败");
            return;
        }
        try {
            WXPayUtils.payUnifiedorder(AnalyseXML.orderToXML(bean))
                    .subscribe(s -> {
                        System.out.println(s);
                        writer.println(getJson(s, bean));
                    }, throwable -> {
                        writer.println(WXPayUtils.RESULT_FAIL + "_" + throwable);
                        throwable.printStackTrace();
                    });
        } catch (Exception e) {
            e.printStackTrace();
            writer.println("failed");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        super.doGet(request, response);
    }


    /**
     * 使用服务完全返回的xml解析出prepay_id，并且重新签名返回json
     */
    private String getJson(String s, OrderBean bean) throws YiException {
        String return_code = AnalyseXML.analyseWXResultBean(s, "return_code");
        String result_code = AnalyseXML.analyseWXResultBean(s, "result_code");
        bean.setResult_code(result_code)
                .setReturn_code(return_code);
        if (WXPayUtils.RESULT_OK.equals(return_code)) {
            if (WXPayUtils.RESULT_OK.equals(result_code)) {
                bean.setPrepay_id(AnalyseXML.analyseWXResultBean(s, "prepay_id"));
                bean.setSign(AnalyseXML.analyseWXResultBean(s, "sign"));
                bean.setNonce_str(AnalyseXML.analyseWXResultBean(s, "nonce_str"));
                OrderFactory.setSign(bean);
                DBManager.getInstance().saveOrUpdateOrder(bean);
                return new Gson().toJson(bean);
            }
        }
        String return_msg = AnalyseXML.analyseWXResultBean(s, "return_msg");
        throw new YiException(return_msg);
    }
}
