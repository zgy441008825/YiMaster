package com.zou.yimaster.servlet.common;

/**
 * Created by zougaoyuan on 2018/4/11
 *
 * @author zougaoyuan
 */
public class ChannelInfo {

    /**
     * channel : wechat
     * info : {"appid":"wechat_appid","secret":"wechat_secret"}
     */

    private String channel;
    /**
     * appid : wechat_appid
     * secret : wechat_secret
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
        private String secret;
        private String key;
        private String mchId;

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getSecret() {
            return secret;
        }

        public void setSecret(String secret) {
            this.secret = secret;
        }

        public String getKey() {
            return key;
        }

        public InfoBean setKey(String key) {
            this.key = key;
            return this;
        }

        public String getMchId() {
            return mchId;
        }

        public InfoBean setMchId(String mchId) {
            this.mchId = mchId;
            return this;
        }
    }
}
