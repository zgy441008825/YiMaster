package com.zou.yimaster.servlet.api;

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
@WebServlet(name = "QueryOrder",urlPatterns = "/api/QueryOrder")
public class QueryOrder extends BaseServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        super.doPost(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        super.doGet(request, response);
    }
}
