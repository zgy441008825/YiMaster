package com.zou.yimaster.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zou.yimaster.R;
import com.zou.yimaster.common.PowerFactory;
import com.zou.yimaster.common.QuestionHelper;
import com.zou.yimaster.common.dao.UserGameRecord;
import com.zou.yimaster.ui.adapter.PlayAdapter;
import com.zou.yimaster.ui.base.BaseActivity;
import com.zou.yimaster.ui.view.QuestionGroupView;
import com.zou.yimaster.utils.AnimationHelper;
import com.zou.yimaster.utils.SPTools;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zougy on 03.25.025
 */
public class PlayActivity extends BaseActivity {

    private static final String PLAY_SHOW_GUIDE_0 = "GUIDE_0";

    private static final String TAG = "PlayActivity";

    @BindView(R.id.playButton)
    Button playButton;
    @BindView(R.id.questionGroupView)
    QuestionGroupView groupView;
/*    @BindView(R.id.powerInfoPowerCnt)
    TextView powerInfoPowerCnt;
    @BindView(R.id.powerInfoTime)
    TextView powerInfoTime;
    @BindView(R.id.powerInfoMoney)
    TextView powerInfoMoney;*/

    private static final int SHOW_SPEED = 2;

    /**
     * 初始值大小
     */
    private static int QUEST_CNT_INIT = 5;
    @BindView(R.id.playTVCount)
    TextView playTVCount;

    /**
     * 题目数量
     */
    private int dataSize = QUEST_CNT_INIT;

    /**
     * 网格个数
     */
    private static final int itemSize = 9;

    private static final int LEAVE_STEEP = 3;

    /**
     * 保存题目
     */
    private List<Integer> dataList = new ArrayList<>();

    /**
     * 记录耗时
     */
    private long answerUserTime = 0;

    /**
     * 单次耗时
     */
    private long oneUserTime = 0;

    /**
     * 显示的时间格式
     */
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss:SSS", Locale.getDefault());

    /**
     * 计时Disposable
     */
    private Disposable timeCountDisposable;

    private Disposable clearDisposable, selectDisposable;

    /**
     * 保存当前显示索引，以防
     */
    private int currentIndex = 0;

    /**
     * 当前状态——无状态
     */
    private static final int STATE_NORMAL = 0;

    /**
     * 当前状态——出题
     */
    private static final int STATE_SHOW_QUESTION = 1;

    /**
     * 当前状态——显示题目完成
     */
    private static final int STATE_SHOW_QUESTION_END = 2;

    /**
     * 当前状态——回答
     */
    private static final int STATE_ANSWER = 3;

    /**
     * 当前状态
     */
    private int state = 0;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        ButterKnife.bind(this);
        groupView.setCallback(callback);
//        playTVCount.setTypeface(Typeface.createFromAsset(getAssets(), "font_comm.TTF"));
//        PowerFactory.getInstance().setListener(powerProductionListener);
        initDataList();
    }

    /**
     * 初始化数据
     */
    private void initDataList() {
        clickCnt = 0;
        dataList.clear();
        groupView.setEnabled(false);
        state = STATE_NORMAL;
        dataList.addAll(QuestionHelper.productionQuestion(dataSize, itemSize));
        Log.d(TAG, "initDataList: " + dataList);
        playButton.setText("准备");
        showFirstTips();
    }

    /**
     * 显示初次提示
     */
    private void showFirstTips() {
        if (!SPTools.getInstance(this).getBoolean(PLAY_SHOW_GUIDE_0)) {
            if (state < STATE_SHOW_QUESTION_END) {
                new AlertDialog.Builder(this)
                        .setMessage("点击“准备”按钮，开始显示题目")
                        .setPositiveButton(R.string.public_ok, null)
                        .show();
            } else {
                new AlertDialog.Builder(this)
                        .setMessage("点击“开始”按钮，开始回答题目")
                        .setPositiveButton(R.string.public_ok,
                                (dialog, which) -> SPTools.getInstance(PlayActivity.this).saveBoolean
                                        (PLAY_SHOW_GUIDE_0, true))
                        .show();
            }
        }
    }

    /*private PowerFactory.IPowerProductionListener powerProductionListener = new PowerFactory.IPowerProductionListener
            () {
        @Override
        public void onStateChange(String time) {
            powerInfoPowerCnt.setText(time);
        }

        @Override
        public void onStockChange(int stock) {
            powerInfoTime.setText(String.format(Locale.getDefault(), "%1$d/%2$d", stock, PowerFactory.POWER_MAX));
            if (stock >= PowerFactory.POWER_MAX) {
                powerInfoTime.setVisibility(View.GONE);
            } else {
                powerInfoTime.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onMoneyChange(int money) {
            powerInfoMoney.setText(String.valueOf(money));
        }
    };*/

    /**
     * 当前一局点击正确的计数
     */
    private int clickCnt = 0;

    private int answerCnt = 0;

    /**
     * 总计
     */
    private int rightCnt = 0;

    private PlayAdapter.IClickViewTouchCallback callback = new PlayAdapter.IClickViewTouchCallback() {
        @Override
        public boolean onActionDown(int index) {
            Log.d(TAG, "callback: " + index);
            if (clickCnt < dataSize && dataList.get(clickCnt) == index) {
                clickCnt++;
                rightCnt++;
                AnimationHelper.bounceAnimation(playTVCount);
                playTVCount.setText(String.valueOf(rightCnt));
                return true;
            }
            disposeFlowable();
            showFailedDialog();
            return false;
        }

        @Override
        public void onActionUp(int index) {
            Log.d(TAG, "onActionUp: " + index);
            if (clickCnt >= dataSize) {//完成
                answerCnt += dataSize;
                disposeFlowable();
                showNextLevelDialog();
            }
        }
    };

    private void showNextLevelDialog() {
        dataSize += LEAVE_STEEP;
        initDataList();
    }

    @Override
    public void onResume() {
        super.onResume();
        PowerFactory.getInstance().requestCallback();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTIVITY_REQUEST_CODE) {
            try {
                int id = data.getIntExtra("result", -1);
                switch (id) {
                    case R.id.playResultIcHome:
                        finish();
                        break;
                    case R.id.playResultIcLoop:
                        dataSize = QUEST_CNT_INIT;
                        answerUserTime = 0;
                        currentIndex = 0;
                        rightCnt = 0;
                        initDataList();
                        break;
                    case GameResultActivity.GOON:
                        currentIndex = 0;
                        rightCnt -= clickCnt;
                        initDataList();
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                finish();
            }
        }
    }

    private static final int ACTIVITY_REQUEST_CODE = 0x1100;

    private void showFailedDialog() {
        Intent intent = new Intent(this, GameResultActivity.class);
        intent.putExtra("time", answerUserTime);
        intent.putExtra("count", rightCnt);
        startActivityForResult(intent, ACTIVITY_REQUEST_CODE);
    }

    private UserGameRecord getRecord() {
        UserGameRecord record = new UserGameRecord();
        record.setAnswerCnt(answerCnt)
                .setUseTime(answerUserTime)
                .setRecordTime(System.currentTimeMillis());
        return record;
    }

    @OnClick(R.id.playButton)
    public void onPlayButtonClick(View view) {
        if (state == STATE_NORMAL) {
            startShowAnswer();
            return;
        }
        if (state == STATE_SHOW_QUESTION_END) {
            startAnswer();
        }
    }

    private void startShowAnswer() {
        state = STATE_SHOW_QUESTION;
        playButton.setEnabled(false);
        disposeFlowable();
        clearDisposable = Flowable.interval(currentIndex, SHOW_SPEED, TimeUnit.SECONDS)
                .onBackpressureBuffer()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .take(dataSize + 1)
                .subscribe(aLong -> {
                    groupView.setSelect(-1);
                }, throwable -> currentIndex = -1, () -> {
                    currentIndex = -1;
                    playButton.setEnabled(true);
                    playButton.setText("开始");
                    showFirstTips();
                    state = STATE_SHOW_QUESTION_END;
                });
        selectDisposable = Flowable.interval(currentIndex + 1, SHOW_SPEED, TimeUnit.SECONDS)
                .onBackpressureBuffer()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .take(dataSize)
                .subscribe(aLong -> {
                            currentIndex = aLong.intValue();
                            groupView.setSelect(dataList.get(aLong.intValue()));
                        },
                        throwable -> {
                            currentIndex = -1;
                            state = STATE_NORMAL;
                        },
                        () -> {
                            currentIndex = -1;
                            state = STATE_SHOW_QUESTION_END;
                        });
    }

    /**
     * 进入回答状态
     */
    private void startAnswer() {
        state = STATE_ANSWER;
        groupView.setEnabled(true);
        oneUserTime = 0;
        timeCountDisposable = Flowable.interval(0, 1, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    answerUserTime += 1;
                    oneUserTime += 1;
                    playButton.setText(dateFormat.format(answerUserTime));
                });
    }

    /**
     * 取消所有的状态
     */
    private void disposeFlowable() {
        if (clearDisposable != null) {
            clearDisposable.dispose();
            clearDisposable = null;
        }
        if (selectDisposable != null) {
            selectDisposable.dispose();
            selectDisposable = null;
        }
        if (timeCountDisposable != null) {
            timeCountDisposable.dispose();
            timeCountDisposable = null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        disposeFlowable();
    }
}
