package com.zou.yimaster.servlet.api;

import com.zou.yimaster.servlet.common.OrderBean;
import com.zou.yimaster.servlet.common.OrderFactory;
import com.zou.yimaster.servlet.dao.DBManager;
import com.zou.yimaster.servlet.utils.ParamStringTools;
import com.zou.yimaster.servlet.wx.WXPayUtils;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by zougaoyuan on 04.20.020
 * 订单查询
 *
 * @author zougaoyuan
 */
@WebServlet(name = "QueryOrder", urlPatterns = "/api/QueryOrder")
public class QueryOrder extends BaseServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        super.doPost(request, response);
        try {
            String orderNo = request.getParameter("orderNo");
            OrderBean bean = DBManager.getInstance().getOrderBean(orderNo);
            if (bean.getTrade_state() == null) {
                WXPayUtils.payOrderquery(getQueryParam(bean))
                        .subscribe(s -> {
                            OrderBean orderBean = OrderFactory.updateOrderTradeState(s);
                            resultState(orderBean);
                        }, throwable -> writer.println(WXPayUtils.RESULT_FAIL + "_" + throwable.getMessage()));
            } else {
                resultState(bean);
            }
        } catch (Exception e) {
            writer.println(WXPayUtils.RESULT_FAIL + "_" + e.getMessage());
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        super.doGet(request, response);
    }

    private String getQueryParam(OrderBean bean) {
        if (bean == null) return null;
        Map<String, String> param = new LinkedHashMap<>();
        param.put("appid", bean.getAppid());
        param.put("mch_id", bean.getMch_id());
        param.put("out_trade_no", bean.getOut_trade_no());
        param.put("nonce_str", OrderFactory.getNonce());
        String sign = ParamStringTools.getSign(param);
        param.put("sign", sign);
        return ParamStringTools.mapToXml(param);
    }

    private void resultState(OrderBean bean) {
        writer.println(bean.getTrade_state());
    }
}
