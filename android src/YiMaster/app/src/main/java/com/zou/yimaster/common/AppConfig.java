package com.zou.yimaster.common;

import java.util.Map;

/**
 * Created by zougaoyuan on 2018/4/4
 *
 * @author zougaoyuan
 */
public class AppConfig {

    public static Map<String, InfoBean> APPConfigs;

    /**
     * channel : wechat
     * info : {"appid":"wechat_appid","secrit":"wechat_secret"}
     */
    private String channel;

    /**
     * appid : wechat_appid
     * secrit : wechat_secret
     */
    private InfoBean info;

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public static class InfoBean {
        private String appid;
        private String secrit;

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getSecrit() {
            return secrit;
        }

        public void setSecrit(String secrit) {
            this.secrit = secrit;
        }
    }
}
