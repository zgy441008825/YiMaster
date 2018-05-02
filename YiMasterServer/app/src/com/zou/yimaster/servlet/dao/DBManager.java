package com.zou.yimaster.servlet.dao;

import com.zou.yimaster.servlet.common.ChannelInfo;
import com.zou.yimaster.servlet.common.OrderBean;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

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
    private String password = "zgy03031022WENLI";

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
                    String sql = "INSERT INTO channelinfo(channel, appid, secret,app_key,mch_id) VALUES (?, ?, ?,?,?)";
                    statement = conn.prepareStatement(sql);
                    statement.setString(1, info.getChannel());
                    statement.setString(2, info.getInfo().getAppid());
                    statement.setString(3, info.getInfo().getSecret());
                    statement.setString(4, info.getInfo().getApp_key());
                    statement.setString(5, info.getInfo().getMchId());
                } else {
                    String sql = "UPDATE channelinfo SET appid = ? , secret = ? , app_key = ? , mch_id= ? WHERE " +
                            "channel = ?";
                    statement = conn.prepareStatement(sql);
                    statement.setString(1, info.getInfo().getAppid());
                    statement.setString(2, info.getInfo().getSecret());
                    statement.setString(3, info.getInfo().getApp_key());
                    statement.setString(4, info.getInfo().getMchId());
                    statement.setString(5, info.getChannel());
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
                    ChannelInfo.InfoBean bean = new ChannelInfo.InfoBean();
                    bean.setAppid(resultSet.getString("appid"));
                    bean.setSecret(resultSet.getString("secret"));
                    bean.setApp_key(resultSet.getString("app_key"))
                            .setMchId(resultSet.getString("mch_id"));
                    info.setChannel(channel);
                    info.setInfo(bean);
                    return info;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Map<String, ChannelInfo.InfoBean> getChannels() {
        try {
            if (!conn.isClosed()) {
                String sql = "SELECT * FROM channelinfo";
                PreparedStatement statement = conn.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.first()) {
                    Map<String, ChannelInfo.InfoBean> infoBeanMap = new HashMap<>();
                    do {
                        ChannelInfo info = getChannelInfo(resultSet.getString("channel"));
                        infoBeanMap.put(info.getChannel(), info.getInfo());
                    } while (resultSet.next());
                    return infoBeanMap;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveOrUpdateOrder(OrderBean bean) {
        try {
            OrderBean infoBean = getOrderBean(bean.getOut_trade_no());
            PreparedStatement statement;
            String sql;
            if (infoBean == null) {//保存
                sql = "INSERT INTO yiorder(appid,mchid,nonce_str,sign,sign_type,out_trade_no,body,total_fee," +
                        "spbill_create_ip,time_start,time_expire,prepay_id,result_code,err_code,err_code_des,openid," +
                        "bank_type,transaction_id,time_end,trade_state) VALUES (?,?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                        "?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
            } else {//更新
                sql = "UPDATE yiorder SET appid=?,mchid=?,nonce_str=?,sign=?,sign_type=?,out_trade_no=?,body=?," +
                        "total_fee=?," +
                        "spbill_create_ip=?,time_start=?,time_expire=?,prepay_id=?,result_code=?,err_code=?," +
                        "err_code_des=?,openid=?,bank_type=?,transaction_id=?,trade_state=?";
            }
            statement = conn.prepareStatement(sql);
            statement.setString(1, bean.getAppid());
            statement.setString(2, bean.getMch_id());
            statement.setString(3, bean.getNonce_str());
            statement.setString(4, bean.getSign());
            statement.setString(5, bean.getSign_type());
            statement.setString(6, bean.getOut_trade_no());
            statement.setString(7, bean.getBody());
            statement.setInt(8, bean.getTotal_fee());
            statement.setString(9, bean.getSpbill_create_ip());
            statement.setString(10, bean.getTime_start());
            statement.setString(11, bean.getTime_expire());
            statement.setString(12, bean.getPrepay_id());
            statement.setString(13, bean.getResult_code());
            statement.setString(14, bean.getErr_code());
            statement.setString(15, bean.getErr_code_des());
            statement.setString(16, bean.getOpenid());
            statement.setString(17, bean.getBank_type());
            statement.setString(18, bean.getTransaction_id());
            statement.setString(19, bean.getTime_end());
            statement.setString(20, bean.getTrade_state());
            statement.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取一个订单记录
     *
     * @param out_trade_no 订单号
     */
    public OrderBean getOrderBean(String out_trade_no) {
        try {
            if (!conn.isClosed()) {
                String sql = "SELECT * FROM yiorder WHERE out_trade_no= ?";
                PreparedStatement statement = conn.prepareStatement(sql);
                statement.setString(1, out_trade_no);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.first()) {
                    OrderBean info = new OrderBean();
                    info.setAppid(resultSet.getString("appid"))
                            .setMch_id(resultSet.getString("mchid"))
                            .setNonce_str(resultSet.getString("nonce_str"))
                            .setSign(resultSet.getString("sign"))
                            .setSign_type(resultSet.getString("sign_type"))
                            .setBody(resultSet.getString("body"))
                            .setOut_trade_no(resultSet.getString("out_trade_no"))
                            .setTotal_fee(resultSet.getInt("total_fee"))
                            .setSpbill_create_ip(resultSet.getString("spbill_create_ip"))
                            .setTime_start(resultSet.getString("time_start"))
                            .setTime_expire(resultSet.getString("time_expire"))
                            .setPrepay_id(resultSet.getString("prepay_id"))
                            .setResult_code(resultSet.getString("result_code"))
                            .setErr_code(resultSet.getString("err_code"))
                            .setErr_code_des(resultSet.getString("err_code_des"))
                            .setOpenid(resultSet.getString("openid"))
                            .setBank_type(resultSet.getString("bank_type"))
                            .setTransaction_id(resultSet.getString("transaction_id"))
                            .setTime_end(resultSet.getString("time_end"))
                            .setTrade_state(resultSet.getString("trade_state"));
                    return info;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
