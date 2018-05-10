package com.zou.yimaster.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zou.yimaster.R;
import com.zou.yimaster.common.PowerFactory;
import com.zou.yimaster.ui.base.BaseActivity;
import com.zou.yimaster.utils.AnimationHelper;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zougaoyuan on 04.27.027
 * 游戏结果
 *
 * @author zougaoyuan
 */
public class GameResultActivity extends BaseActivity {

    public static final int GOON = 0xFF0001;

    @BindView(R.id.playResultTvResultTime)
    TextView playResultTvResultTime;
    @BindView(R.id.playResultTvResultCount)
    TextView playResultTvResultCount;
    @BindView(R.id.playResultImgResultTime)
    ImageView playResultImgResultTime;
    @BindView(R.id.playResultImgResultCount)
    ImageView playResultImgResultCount;
    @BindView(R.id.playResultLineTime)
    View playResultLineTime;
    @BindView(R.id.playResultLineCount)
    View playResultLineCount;
    @BindView(R.id.playResultIcHome)
    ImageView playResultIcHome;
    @BindView(R.id.playResultIcLoop)
    ImageView playResultIcLoop;
    @BindView(R.id.playResultIcNext)
    ImageView playResultIcNext;


    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_result);
        ButterKnife.bind(this);
        playResultTvResultTime.setTypeface(Typeface.createFromAsset(getAssets(), "font_2.otf"));
        playResultTvResultCount.setTypeface(Typeface.createFromAsset(getAssets(), "font_2.otf"));
        AnimationHelper.translationX(playResultLineTime, true, 500, 100);
        AnimationHelper.translationX(playResultLineCount, false, 500, 100);
        Flowable.timer(800, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    long useTime = getIntent().getExtras().getInt("time");
                    int count = getIntent().getExtras().getInt("count");
                    showTimeAnimation(useTime);
                    showCountAnimation(count);
                });
        AnimationHelper.translationY(playResultIcHome,true,300,1400);
        AnimationHelper.translationY(playResultIcLoop,true,300,1500);
        AnimationHelper.translationY(playResultIcNext,true,300,1600);
    }

    private void showTimeAnimation(long time) {
        AnimationHelper.alpha(playResultTvResultTime, 0, 1, 250);
        AnimationHelper.alpha(playResultImgResultTime, 0, 1, 250);
        AnimationHelper.showValueAnimation((int) time, 500, animation -> {
            int value = (int) animation.getAnimatedValue();
            playResultTvResultTime.setText(PlayActivity.dateFormat.format(value));
        });
    }

    private void showCountAnimation(int count) {
        AnimationHelper.alpha(playResultTvResultCount, 0, 1, 250);
        AnimationHelper.alpha(playResultImgResultCount, 0, 1, 250);
        AnimationHelper.showValueAnimation(count, 500, animation -> {
            int value = (int) animation.getAnimatedValue();
            playResultTvResultCount.setText(String.valueOf(value));
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
