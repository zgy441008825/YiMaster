package com.zou.yimaster.common;

import com.zou.yimaster.utils.SPTools;

import org.xutils.x;

/**
 * Created by zougaoyuan on 2018/4/11
 *
 * @author zougaoyuan
 */
public class YIMoneyHelper {

    private static final String MONEY_SP_KEY = "money";

    /**
     * 获取剩余币，默认5
     */
    public int getMoney() {
        return SPTools.getInstance(x.app()).getInt(MONEY_SP_KEY, 5);
    }

    /**
     * 增加，结果为 剩余+value
     */
    public void addMoney(int value) {
        SPTools.getInstance(x.app()).saveInt(MONEY_SP_KEY, getMoney() + value);
    }

    /**
     * 话费
     *
     * @return 余额不足返回FALSE，成功返回TRUE
     */
    public boolean userMoney(int value) {
        if (getMoney() < value) return false;
        SPTools.getInstance(x.app()).saveInt(MONEY_SP_KEY, getMoney() - value);
        return true;
    }

}
