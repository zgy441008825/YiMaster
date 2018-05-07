package com.zou.yimaster.common.dao;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by zougaoyuan on 2018/4/4
 *
 * @author zougaoyuan
 */
@Table(name = "gameRecord")
public class UserGameRecord {

    private static final String COLUMN_USE_TIME = "useTime";
    private static final String COLUMN_ANSWER_CNT = "count";
    private static final String COLUMN_RECORDE_TIME = "recode_time";
    private static final String COLUMN_SEND_TO_SERVER = "sendToServer";

    @Column(name = "id", isId = true)
    private int id;

    @Column(name = COLUMN_SEND_TO_SERVER)
    private boolean sendToServer;

    @Column(name = UserInfo.COLUMN_OPENID)
    private String openID;

    @Column(name = COLUMN_USE_TIME)
    private long useTime;

    @Column(name = COLUMN_ANSWER_CNT)
    private int answerCnt;

    @Column(name = COLUMN_RECORDE_TIME)
    private long recordTime;

    @Column(name = UserInfo.COLUMN_NAME)
    private String name;

    @Column(name = UserInfo.COLUMN_HEADIMGURL)
    private String headImgUrl;

    public String getOpenID() {
        return openID;
    }

    public UserGameRecord setOpenID(String openID) {
        this.openID = openID;
        return this;
    }

    public long getUseTime() {
        return useTime;
    }

    public UserGameRecord setUseTime(long useTime) {
        this.useTime = useTime;
        return this;
    }

    public int getAnswerCnt() {
        return answerCnt;
    }

    public UserGameRecord setAnswerCnt(int answerCnt) {
        this.answerCnt = answerCnt;
        return this;
    }

    public long getRecordTime() {
        return recordTime;
    }

    public UserGameRecord setRecordTime(long recordTime) {
        this.recordTime = recordTime;
        return this;
    }

    public String getName() {
        return name;
    }

    public UserGameRecord setName(String name) {
        this.name = name;
        return this;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public UserGameRecord setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
        return this;
    }

    public int getId() {
        return id;
    }

    public UserGameRecord setId(int id) {
        this.id = id;
        return this;
    }

    public boolean isSendToServer() {
        return sendToServer;
    }

    public UserGameRecord setSendToServer(boolean sendToServer) {
        this.sendToServer = sendToServer;
        return this;
    }
}
