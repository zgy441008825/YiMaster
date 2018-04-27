package com.zou.yimaster.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.zou.yimaster.R;
import com.zou.yimaster.ui.base.BaseActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zougaoyuan on 04.27.027
 *
 * @author zougaoyuan
 */
public class BuyActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bug);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.BuyClose, R.id.Buy_1, R.id.Buy_2, R.id.Buy_3, R.id.Buy_4})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.BuyClose:
                finish();
                break;
        }
    }
}
