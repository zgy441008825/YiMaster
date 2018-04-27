package com.zou.yimaster.servlet.wx;

import com.zou.yimaster.servlet.api.BaseServlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by zougaoyuan on 04.20.020
 *
 * @author zougaoyuan
 */
@WebServlet(name = "WXOrderCallback",urlPatterns = "/wx/WXOrderCallback")
public class WXOrderCallback extends BaseServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        super.doPost(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        super.doGet(request, response);
    }
}
