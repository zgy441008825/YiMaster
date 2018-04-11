package com.zou.yimaster.servlet;

import com.google.gson.Gson;
import com.zou.yimaster.servlet.dao.ChannelInfo;
import com.zou.yimaster.servlet.dao.DBManager;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * 获取前面信息 参数channel，如果不传返回所有渠道信息
 */
public class GetChannelInfo extends javax.servlet.http.HttpServlet {
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse
            response) throws javax.servlet.ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        PrintWriter writer = response.getWriter();
        String channel = request.getParameter("channel");
        System.out.println("post:" + channel);
        if (channel == null || channel.equals("")) {
            List<ChannelInfo> channelInfos = DBManager.getInstance().getChannels();
            System.out.println("getChannels:" + (new Gson().toJson(channelInfos)));
            writer.print(new Gson().toJson(channelInfos));
        } else {
            ChannelInfo info = DBManager.getInstance().getChannelInfo(channel);
            System.out.println("getChannel:" + (new Gson().toJson(info)));
            writer.println(new Gson().toJson(info));
        }
        writer.close();
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse
            response) throws javax.servlet.ServletException, IOException {
        doPost(request, response);
    }
}
