package com.zou.yimaster.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.zou.yimaster.R;
import com.zou.yimaster.common.YiApplication;
import com.zou.yimaster.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zougy on 05.29.029
 */
public class AboutActivity extends BaseActivity {

    @BindView(R.id.aboutTvVersion)
    TextView aboutTvVersion;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        aboutTvVersion.setText("V" + YiApplication.getApkVersionName());
    }

    @OnClick(R.id.aboutImBack)
    public void onBackClick() {
        finish();
    }

}
