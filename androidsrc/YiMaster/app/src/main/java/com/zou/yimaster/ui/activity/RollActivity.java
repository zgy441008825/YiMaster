package com.zou.yimaster.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.zou.yimaster.R;
import com.zou.yimaster.ui.view.RollViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RollActivity extends AppCompatActivity {

    @BindView(R.id.rollViewGroup)
    RollViewGroup rollViewGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        ButterKnife.bind(this);
        rollViewGroup.startRoll();
    }
}
