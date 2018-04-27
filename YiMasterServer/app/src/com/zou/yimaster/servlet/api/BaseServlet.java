package com.zou.yimaster.servlet.api;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by zougaoyuan on 04.25.025
 *
 * @author zougaoyuan
 */
public class BaseServlet extends javax.servlet.http.HttpServlet {

    protected PrintWriter writer;

    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse
            response) throws javax.servlet.ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        writer = response.getWriter();
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse
            response) throws javax.servlet.ServletException, IOException {
        doPost(request, response);
    }

}
