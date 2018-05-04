package com.zou.yimaster.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.zou.yimaster.R;
import com.zou.yimaster.common.PowerFactory;
import com.zou.yimaster.ui.base.BaseActivity;
import com.zou.yimaster.utils.AnimationHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zougaoyuan on 04.27.027
 * 游戏结果
 *
 * @author zougaoyuan
 */
public class GameResultActivity extends BaseActivity {

    @BindView(R.id.playResultTvResult)
    TextView playResultTvResult;

    public static final int GOON = 0xFF0001;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_result);
        ButterKnife.bind(this);
        long useTime = getIntent().getExtras().getLong("time");
        int count = getIntent().getExtras().getInt("count");
        showTimeAnimation(useTime);
        showCountAnimation(count);
    }

    private void showTimeAnimation(long time) {
        AnimationHelper.showValueAnimation(time, 500, animation -> {
            int value = (int) animation.getAnimatedValue();
            playResultTvResult.setText(PlayActivity.dateFormat.format(value));
        });
    }

    private void showCountAnimation(int count) {
        AnimationHelper.showValueAnimation(count, 500, animation -> {
            int value = (int) animation.getAnimatedValue();
            playResultTvResult.setText(String.valueOf(value));
        });
    }

    @OnClick({R.id.playResultIcHome, R.id.playResultIcLoop})
    public void onHomeClick(View view) {
        Intent intent = getIntent();
        intent.putExtra("result", view.getId());
        setResult(RESULT_OK, intent);
        finish();
    }


    @OnClick(R.id.playResultIcNext)
    public void onNextClick() {
        if (PowerFactory.getInstance().userMoney(PowerFactory.MONEY_STEP)) {
            Intent intent = getIntent();
            intent.putExtra("result", GOON);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            Intent intent = new Intent(this, BuyActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected boolean isBack() {
        return true;
    }
}
