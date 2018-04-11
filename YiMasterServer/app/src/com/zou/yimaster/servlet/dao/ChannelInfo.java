package com.zou.yimaster.servlet.dao;

/**
 * Created by zougaoyuan on 2018/4/11
 *
 * @author zougaoyuan
 */
public class ChannelInfo {

    private String channel;

    private String appid;

    private String secrit;

    public ChannelInfo() {
    }

    public ChannelInfo(String channel, String appid, String secrit) {
        this.channel = channel;
        this.appid = appid;
        this.secrit = secrit;
    }

    public String getChannel() {
        return channel;
    }

    public ChannelInfo setChannel(String channel) {
        this.channel = channel;
        return this;
    }

    public String getAppid() {
        return appid;
    }

    public ChannelInfo setAppid(String appid) {
        this.appid = appid;
        return this;
    }

    public String getSecrit() {
        return secrit;
    }

    public ChannelInfo setSecrit(String secrit) {
        this.secrit = secrit;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChannelInfo that = (ChannelInfo) o;
        if (channel != null ? !channel.equals(that.channel) : that.channel != null) return false;
        if (appid != null ? !appid.equals(that.appid) : that.appid != null) return false;
        return secrit != null ? secrit.equals(that.secrit) : that.secrit == null;
    }

    @Override
    public int hashCode() {
        int result = channel != null ? channel.hashCode() : 0;
        result = 31 * result + (appid != null ? appid.hashCode() : 0);
        result = 31 * result + (secrit != null ? secrit.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ChannelInfo{" +
                "channel='" + channel + '\'' +
                ", appid='" + appid + '\'' +
                ", secrit='" + secrit + '\'' +
                '}';
    }
}
