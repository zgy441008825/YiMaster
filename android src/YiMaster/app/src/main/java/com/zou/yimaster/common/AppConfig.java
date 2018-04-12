package com.zou.yimaster.common;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zougaoyuan on 2018/4/4
 *
 * @author zougaoyuan
 */
public class AppConfig {

    public static Map<String, AppChannelInfo> appChannelInfoMap = new HashMap<>();

    public Map<String, AppChannelInfo> getAppChannelInfoMap() {
        return appChannelInfoMap;
    }

    public AppConfig setAppChannelInfoMap(Map<String, AppChannelInfo> appChannelInfoMap) {
        AppConfig.appChannelInfoMap = appChannelInfoMap;
        return this;
    }

    public static class AppChannelInfo {
        private String appID;
        private String appSecret;

        public String getAppID() {
            return appID;
        }

        public AppChannelInfo setAppID(String appID) {
            this.appID = appID;
            return this;
        }

        public String getAppSecret() {
            return appSecret;
        }

        public AppChannelInfo setAppSecret(String appSecret) {
            this.appSecret = appSecret;
            return this;
        }
    }
}
