package com.zou.yimaster.servlet.dao;

import org.apache.commons.dbutils.BaseResultSetHandler;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zougaoyuan on 2018/4/11
 *
 * @author zougaoyuan
 */
public class DBManager {

    private static DBManager dbManager;

    //声明Connection对象
    private Connection conn;
    //驱动程序名
    private String driver = "com.mysql.jdbc.Driver";
    //URL指向要访问的数据库名mydata
    private String url = "jdbc:mysql://localhost:3306/";
    private static String character = "?useUnicode=true&characterEncoding=gbk";
    private String dbName = "yimaster";
    //MySQL配置时的用户名
    private String user = "root";
    //MySQL配置时的密码
    private String password = "root";

    private DBManager() {
        try {
            Class.forName(driver).newInstance();
            try {
                conn = DriverManager.getConnection(url + dbName + character, user, password);
                System.out.println("connect success");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static DBManager getInstance() {
        if (dbManager == null) {
            synchronized (DBManager.class) {
                if (dbManager == null) dbManager = new DBManager();
            }
        }
        return dbManager;
    }


    public void insertChannelInfo(ChannelInfo info) {
        try {
            if (!conn.isClosed()) {
                ChannelInfo info1 = getChannelInfo(info.getChannel());
                PreparedStatement statement;
                if (info1 == null) {
                    String sql = "INSERT INTO channelinfo(channel, appid, secret) VALUES (?, ?, ?)";
                    statement = conn.prepareStatement(sql);
                    statement.setString(1, info.getChannel());
                    statement.setString(2, info.getAppid());
                    statement.setString(3, info.getSecrit());
                } else {
                    String sql = "UPDATE channelinfo SET appid = ? , secret = ? WHERE channel = ?";
                    statement = conn.prepareStatement(sql);
                    statement.setString(1, info.getAppid());
                    statement.setString(2, info.getSecrit());
                    statement.setString(3, info.getChannel());
                }
                statement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ChannelInfo getChannelInfo(String channel) {
        try {
            if (!conn.isClosed()) {
                String sql = "SELECT * FROM channelinfo WHERE channel = ?";
                PreparedStatement statement = conn.prepareStatement(sql);
                statement.setString(1, channel);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.first()) {
                    ChannelInfo info = new ChannelInfo();
                    info.setChannel(channel)
                            .setAppid(resultSet.getString("appid"))
                            .setSecrit(resultSet.getString("secret"));
                    return info;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<ChannelInfo> getChannels() {
        try {
            if (!conn.isClosed()) {
                String sql = "SELECT * FROM channelinfo";
                PreparedStatement statement = conn.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.first()) {
                    List<ChannelInfo> channelInfos = new ArrayList<>();
                    do {
                        ChannelInfo info = new ChannelInfo();
                        info.setChannel(resultSet.getString("channel"))
                                .setAppid(resultSet.getString("appid"))
                                .setSecrit(resultSet.getString("secret"));
                        channelInfos.add(info);
                    } while (resultSet.next());
                    return channelInfos;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
