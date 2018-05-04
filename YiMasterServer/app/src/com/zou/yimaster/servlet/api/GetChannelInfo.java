package com.zou.yimaster.servlet.api;

import com.google.gson.Gson;
import com.zou.yimaster.servlet.common.ChannelInfo;
import com.zou.yimaster.servlet.dao.DBManager;

import java.io.IOException;
import java.util.Map;

import javax.servlet.annotation.WebServlet;

/**
 * 获取前面信息 参数channel，如果不传返回所有渠道信息
 */
@WebServlet(urlPatterns = "/api/getChannel", name = "getChannelInfo")
public class GetChannelInfo extends BaseServlet {
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse
            response) throws javax.servlet.ServletException, IOException {
        super.doPost(request, response);
        String channel = request.getParameter("channel");
        if (channel == null || channel.equals("")) {
            Map<String, ChannelInfo.InfoBean> channelInfos = DBManager.getInstance().getChannels();
            writer.print(new Gson().toJson(channelInfos));
        } else {
            ChannelInfo info = DBManager.getInstance().getChannelInfo(channel);
            System.out.println("getChannel:" + (new Gson().toJson(info)));
            writer.println(new Gson().toJson(info));
        }
        writer.close();
    }

}
