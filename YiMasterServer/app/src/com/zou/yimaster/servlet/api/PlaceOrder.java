package com.zou.yimaster.servlet.api;

import com.google.gson.Gson;
import com.zou.yimaster.servlet.common.OrderBean;
import com.zou.yimaster.servlet.common.OrderFactory;
import com.zou.yimaster.servlet.common.YiException;
import com.zou.yimaster.servlet.dao.DBManager;
import com.zou.yimaster.servlet.utils.XMLMapTools;
import com.zou.yimaster.servlet.utils.NetworkUtil;
import com.zou.yimaster.servlet.wx.WXPayUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by zougaoyuan on 04.25.025
 * 统一下单接口
 * <p>正常情况下 通过访问微信统一下单接口返回的xml获取prepay_id,使用get2APPString 将数据重新签名发送到APP</p>
 * <p>错误时返回错误信息{@link WXPayUtils#RESULT_FAIL}开头的错误信息</p>
 *
 * @author zougaoyuan
 */
@WebServlet(name = "PlaceOrder", urlPatterns = "/api/place")
public class PlaceOrder extends BaseServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        super.doPost(request, response);
        String body = request.getParameter("body");//产品描述
        String channel = request.getParameter("channel");//订单渠道
        int totalFee = Integer.valueOf(request.getParameter("fee"));//商品价格
        String ip = NetworkUtil.getIpAddress(request);
        OrderBean bean = OrderFactory.createOrder(body, totalFee, ip, "wechat");
        if (bean == null) {
            writer.println(WXPayUtils.RESULT_FAIL + "_生成商户订单失败");
            return;
        }
        try {
            WXPayUtils.payUnifiedorder(XMLMapTools.orderToXML(bean))
                    .subscribe(s -> {
                        System.out.println(s);
                        writer.println(getJson(s, bean));
                    }, throwable -> {
                        writer.println(WXPayUtils.RESULT_FAIL + "_" + throwable);
                        throwable.printStackTrace();
                    });
        } catch (Exception e) {
            e.printStackTrace();
            writer.println(WXPayUtils.RESULT_FAIL + "_" + e);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        super.doGet(request, response);
    }


    /**
     * 使用服务完全返回的xml解析出prepay_id，并且重新签名返回json
     */
    private String getJson(String s, OrderBean bean) throws Exception {
        Map<String, String> map = XMLMapTools.xml2Map(s);
        if (map == null || map.containsKey("return_code"))
            throw new YiException("获取微信下单信息错误:" + s);
        if (!XMLMapTools.checkoutSign(map)) {
            throw new YiException("签名异常:" + s);
        }
        String return_code = map.get("return_code");
        String result_code = map.get("result_code");
        bean.setResult_code(result_code)
                .setReturn_code(return_code);
        if (WXPayUtils.RESULT_OK.equals(return_code)) {
            if (WXPayUtils.RESULT_OK.equals(result_code)) {
                bean.setPrepay_id(map.get("prepay_id"));
                DBManager.getInstance().saveOrUpdateOrder(bean);
                return new Gson().toJson(get2APPString(bean));
            }
        }
        String return_msg = XMLMapTools.analyseWXResultBean(s, "return_msg");
        throw new YiException(return_msg);
    }

    /**
     * 获取返回给APP的JSON数据(获取到prepay_id后将参数再次签名传输给APP发起支付)
     */
    private Map<String, String> get2APPString(OrderBean bean) {
        Map<String, String> map = new HashMap<>();
        map.put("appid", bean.getAppid());
        map.put("partnerid", bean.getMch_id());
        map.put("prepayid", bean.getPrepay_id());
        map.put("package", "Sign=WXPay");
        map.put("noncestr", bean.getNonce_str());
        map.put("timestamp", bean.getTime_start());
        String sign = XMLMapTools.getSign(map);
        map.put("sign", sign);
        return map;
    }
}
