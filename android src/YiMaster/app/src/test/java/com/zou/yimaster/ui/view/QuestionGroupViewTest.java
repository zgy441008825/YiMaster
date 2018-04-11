package com.zou.yimaster.ui.view;

import android.content.Context;
import android.test.mock.MockContext;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by zougaoyuan on 2018/3/30
 *
 * @author zougaoyuan
 */
public class QuestionGroupViewTest {

    public QuestionGroupViewTest() {
        Context context = new MockContext();
        QuestionGroupView view = new QuestionGroupView(context);
        view.setColumn(3)
                .setItemSize(9);
    }

    @Test
    public void setColumn() {
    }

    @Test
    public void getItemSize() {
    }

    @Test
    public void setItemSize() {
    }
}