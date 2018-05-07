package com.zou.yimaster.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by zougaoyuan on 05.02.002
 *
 * @author zougaoyuan
 */
public class QuestionHelper {

    public static List<Integer> productionQuestion(int dataSize, int max) {
        List<Integer> dataList = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < dataSize; i++) {
            dataList.add(random.nextInt(max));
        }
        return dataList;
    }

}
