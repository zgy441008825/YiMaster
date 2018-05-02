package com.zou.yimaster.servlet.api;

import com.zou.yimaster.servlet.common.OrderBean;
import com.zou.yimaster.servlet.common.OrderFactroy;
import com.zou.yimaster.servlet.utils.NetworkUtil;
import com.zou.yimaster.servlet.wx.WXPayUtils;

import java.io.IOException;

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
        OrderBean bean = OrderFactroy.createOrder(body, totalFee, ip, "wechat");
        WXPayUtils.payUnifiedorder(orderToXML(bean))
                .subscribe(s -> {
                    System.out.println(s);
                    writer.println("成功:" + s);
                }, throwable -> {
                    throwable.printStackTrace(writer);
                    throwable.printStackTrace();
                });
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        super.doGet(request, response);
    }

    private String orderToXML(OrderBean bean) {
        StringBuilder sb = new StringBuilder();
        sb.append("<xml>");
        sb.append(String.format("<%1$s>%2$s</%1$s>", "appid", bean.getAppid()));
        sb.append(String.format("<%1$s>%2$s</%1$s>", "mch_id", bean.getMchid()));
        sb.append(String.format("<%1$s>%2$s</%1$s>", "nonce_str", bean.getNonce_str()));
        sb.append(String.format("<%1$s>%2$s</%1$s>", "sign", bean.getSign()));
        sb.append(String.format("<%1$s>%2$s</%1$s>", "body", bean.getBody()));
        sb.append(String.format("<%1$s>%2$s</%1$s>", "out_trade_no", bean.getOut_trade_no()));
        sb.append(String.format("<%1$s>%2$s</%1$s>", "total_fee", bean.getTotal_fee()));
        sb.append(String.format("<%1$s>%2$s</%1$s>", "spbill_create_ip", bean.getSpbill_create_ip()));
        sb.append(String.format("<%1$s>%2$s</%1$s>", "notify_url", bean.getNotify_url()));
        sb.append(String.format("<%1$s>%2$s</%1$s>", "trade_type", bean.getTrade_type()));
        sb.append("</xml>");
        return sb.toString();
    }
}
