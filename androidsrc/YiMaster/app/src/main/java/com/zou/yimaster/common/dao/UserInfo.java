package com.zou.yimaster.common.dao;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by zougaoyuan on 2018/4/4
 * 保存当前用户登录信息
 *
 * @author zougaoyuan
 */
@Table(name = "userInfo")
public class UserInfo {

    public static final String COLUMN_NAME = "nickName";

    public static final String COLUMN_SEX = "sex";

    public static final String COLUMN_PROVINCE = "province";

    public static final String COLUMN_CITY = "city";

    public static final String COLUMN_COUNTRY = "country";

    public static final String COLUMN_HEADIMGURL = "headImgUrl";

    public static final String COLUMN_UNIONID = "unionid";

    public static final String COLUMN_OPENID = "openid";

    public static final String COLUMN_EXPIRES = "expires_in";

    public static final String COLUMN_ACCESS_TIME = "access_time";

    public static final String COLUMN_REFRESH_TIME = "refresh_time";

    public static final String COLUMN_ACCESS_TOKEN = "access_token";

    public static final String COLUMN_REFRESH_TOKEN = "refresh_token";

    public static final String COLUMN_SCOPE = "scope";

    /**
     * 有效期
     */
    @Column(name = COLUMN_EXPIRES)
    private long expires_in;

    /**
     * 正常登陆获取access_token time
     */
    @Column(name = COLUMN_ACCESS_TIME)
    private long accessTime;

    /**
     * 刷新refresh_token time
     */
    @Column(name = COLUMN_REFRESH_TIME)
    private long refreshTime;

    @Column(name = COLUMN_ACCESS_TOKEN)
    private String access_token;

    @Column(name = COLUMN_REFRESH_TOKEN)
    private String refresh_token;

    @Column(name = UserInfo.COLUMN_OPENID)
    private String openid;

    @Column(name = COLUMN_SCOPE)
    private String scope;

    @Column(name = COLUMN_NAME)
    private String nickname;

    @Column(name = COLUMN_SEX)
    private int sex;

    @Column(name = COLUMN_PROVINCE)
    private String province;

    @Column(name = COLUMN_CITY)
    private String city;

    @Column(name = COLUMN_COUNTRY)
    private String country;

    @Column(name = COLUMN_HEADIMGURL)
    private String headimgurl;

    @Column(name = COLUMN_UNIONID)
    private String unionid;

    public String getNickname() {
        return nickname;
    }

    public UserInfo setNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public int getSex() {
        return sex;
    }

    public UserInfo setSex(int sex) {
        this.sex = sex;
        return this;
    }

    public String getProvince() {
        return province;
    }

    public UserInfo setProvince(String province) {
        this.province = province;
        return this;
    }

    public String getCity() {
        return city;
    }

    public UserInfo setCity(String city) {
        this.city = city;
        return this;
    }

    public String getCountry() {
        return country;
    }

    public UserInfo setCountry(String country) {
        this.country = country;
        return this;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public UserInfo setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
        return this;
    }

    public String getUnionid() {
        return unionid;
    }

    public UserInfo setUnionid(String unionid) {
        this.unionid = unionid;
        return this;
    }

    public String getOpenid() {
        return openid;
    }

    public UserInfo setOpenid(String openid) {
        this.openid = openid;
        return this;
    }

    public long getExpires_in() {
        return expires_in;
    }

    public UserInfo setExpires_in(long expires_in) {
        this.expires_in = expires_in;
        return this;
    }

    public long getAccessTime() {
        return accessTime;
    }

    public UserInfo setAccessTime(long accessTime) {
        this.accessTime = accessTime;
        return this;
    }

    public long getRefreshTime() {
        return refreshTime;
    }

    public UserInfo setRefreshTime(long refreshTime) {
        this.refreshTime = refreshTime;
        return this;
    }

    public String getAccess_token() {
        return access_token;
    }

    public UserInfo setAccess_token(String access_token) {
        this.access_token = access_token;
        return this;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public UserInfo setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
        return this;
    }

    public String getScope() {
        return scope;
    }

    public UserInfo setScope(String scope) {
        this.scope = scope;
        return this;
    }
}
