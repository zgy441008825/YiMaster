package com.zou.yimaster.servlet.api;

import com.zou.yimaster.servlet.common.OrderBean;
import com.zou.yimaster.servlet.common.TradeState;
import com.zou.yimaster.servlet.dao.DBManager;
import com.zou.yimaster.servlet.wx.WXPayUtils;

import java.io.IOException;

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
            switch (TradeState.valueOf(bean.getTrade_state())) {
                case SUCCESS:
                    writer.println(WXPayUtils.RESULT_OK);
                    break;
                case USERPAYING:
                    writer.println(TradeState.USERPAYING);
                    break;
                default:
                    writer.println(WXPayUtils.RESULT_FAIL);
                    break;
            }
        } catch (Exception e) {
            writer.println(WXPayUtils.RESULT_FAIL);
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        super.doGet(request, response);
    }
}
