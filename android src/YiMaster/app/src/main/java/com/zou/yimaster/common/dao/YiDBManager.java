package com.zou.yimaster.common.dao;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

/**
 * Created by zougaoyuan on 2018/4/3
 *
 * @author zougaoyuan
 */
public class YiDBManager {

    private static YiDBManager instance;
    private DbManager dbManager;

    private static final String dbName = "YiMaster.db";
    private static final int dbVersion = 1;

    private YiDBManager() {
        DbManager.DaoConfig config = new DbManager.DaoConfig();
        config.setDbName(dbName)
                .setDbVersion(dbVersion);
        dbManager = x.getDb(config);
    }

    public static YiDBManager getInstance() {
        if (instance == null) {
            synchronized (YiDBManager.class) {
                if (instance == null) {
                    instance = new YiDBManager();
                }
            }
        }
        return instance;
    }

    /**
     * 正常调用微信登录后使用此方法保存信息
     */
    public void saveLoginInfo(String json) {
        try {
            if (TextUtils.isEmpty(json) || json.contains("errcode")) return;
            JSONObject jsonObject = new JSONObject(json);
            UserInfo info = getUserInfo();
            info = info == null ? new UserInfo() : info;
            info.setAccess_token(jsonObject.getString("access_token"))
                    .setExpires_in(jsonObject.getInt("expires_in"))
                    .setRefresh_token(jsonObject.getString("refresh_token"))
                    .setOpenid(jsonObject.getString("openid"))
                    .setScope(jsonObject.getString("scope"))
                    .setUnionid(jsonObject.getString("unionid"))
                    .setAccessTime(System.currentTimeMillis())
                    .setRefreshTime(System.currentTimeMillis());
            dbManager.saveOrUpdate(info);
        } catch (JSONException | DbException e) {
            e.printStackTrace();
        }
    }

    public void saveUserInfo(String json) {
        UserInfo userInfo = getUserInfo();
        if (userInfo == null || TextUtils.isEmpty(json) || json.contains("errcode")) return;
        try {
            JSONObject jsonObject = new JSONObject(json);
            userInfo.setNickname(jsonObject.getString("nickname"))
                    .setSex(jsonObject.getInt("sex"))
                    .setProvince(jsonObject.getString("province"))
                    .setCity(jsonObject.getString("city"))
                    .setCountry(jsonObject.getString("country"))
                    .setHeadimgurl(jsonObject.getString("headimgurl"))
                    .setUnionid(jsonObject.getString("unionid"));
            dbManager.saveOrUpdate(userInfo);
        } catch (JSONException | DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用refreshToken刷新后调用此方法保存数据
     */
    public void refresh(String json) {
        UserInfo info = getUserInfo();
        if (info == null || TextUtils.isEmpty(json) || json.contains("errcode"))
            return;
        try {
            JSONObject jsonObject = new JSONObject(json);
            info.setAccess_token(jsonObject.getString("access_token"))
                    .setExpires_in(jsonObject.getInt("expires_in"))
                    .setRefresh_token(jsonObject.getString("refresh_token"))
                    .setOpenid(jsonObject.getString("openid"))
                    .setScope(jsonObject.getString("scope"))
                    .setAccessTime(System.currentTimeMillis());
            dbManager.saveOrUpdate(info);
        } catch (JSONException | DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断当前是否能刷新
     *
     * @return TRUE 可以刷新，FALSE 不能刷新，需要重新拉起微信授权
     */
    public boolean canRefresh() {
        UserInfo info = getUserInfo();
        if (info == null) return false;
        long time = 25 * 24 * 60 * 60 * 1000;//定义最大刷新时间 25天，即超过25天需要重新授权
        return (System.currentTimeMillis() - info.getRefreshTime()) < time;
    }

    /**
     * accessToken是否在有效期内
     *
     * @return TRUE：在有效期内，可以直接使用accessToken
     * <br>FALSE：accessToken失效，需要刷新。
     */
    public boolean accessTokenExpires() {
        UserInfo loginInfo = getUserInfo();
        if (loginInfo == null) return false;
        return (System.currentTimeMillis() - loginInfo.getAccessTime()) < 7200 * 1000;
    }

    public UserInfo getUserInfo() {
        try {
            return dbManager.selector(UserInfo.class).findFirst();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public UserInfo getUserInfo(String openID) {
        try {
            return dbManager.selector(UserInfo.class).and(UserInfo.COLUMN_OPENID, "=", openID).findFirst();
        } catch (DbException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void saveRecord(UserGameRecord record){
        try {
            dbManager.saveOrUpdate(record);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
}
