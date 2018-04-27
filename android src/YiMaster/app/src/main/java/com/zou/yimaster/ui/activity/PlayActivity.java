package com.zou.yimaster.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zou.yimaster.R;
import com.zou.yimaster.common.PowerFactory;
import com.zou.yimaster.common.dao.UserGameRecord;
import com.zou.yimaster.ui.adapter.PlayAdapter;
import com.zou.yimaster.ui.base.BaseActivity;
import com.zou.yimaster.ui.view.QuestionGroupView;
import com.zou.yimaster.utils.SPTools;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
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
    @BindView(R.id.powerInfoPowerCnt)
    TextView powerInfoPowerCnt;
    @BindView(R.id.powerInfoTime)
    TextView powerInfoTime;
    @BindView(R.id.powerInfoMoney)
    TextView powerInfoMoney;

    private static int QUEST_CNT_INIT = 5;

    /**
     * 题目数量
     */
    private int dataSize = QUEST_CNT_INIT;

    /**
     * 网格个数
     */
    private int itemSize = 9;

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
     * 当前状态——完成
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
        PowerFactory.getInstance().setListener(powerProductionListener);
        initDataList();
    }

    /**
     * 初始化数据
     */
    private void initDataList() {
        dataList.clear();
        groupView.setEnabled(false);
        state = STATE_NORMAL;
        Random random = new Random();
        for (int i = 0; i < dataSize; i++) {
            dataList.add(random.nextInt(itemSize));
        }
        Log.d(TAG, "initDataList: " + dataList);
        playButton.setText("准备");
        showGuide0();
    }

    private void showGuide0() {
        if (!SPTools.getInstance(this).getBoolean(PLAY_SHOW_GUIDE_0)) {
            if (state < STATE_SHOW_QUESTION_END) {
                new AlertDialog.Builder(this)
                        .setMessage("点击“准备”按钮，开始显示题目")
                        .setPositiveButton(R.string.public_ok, (dialog, which) -> SPTools.getInstance(PlayActivity
                                .this).saveBoolean
                                (PLAY_SHOW_GUIDE_0, true))
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

    private PowerFactory.IPowerProductionListener powerProductionListener = new PowerFactory.IPowerProductionListener
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
    };

    private int clickCnt = 0;

    private int answerCnt = 0;

    private PlayAdapter.IClickViewTouchCallback callback = new PlayAdapter.IClickViewTouchCallback() {
        @Override
        public boolean onActionDown(int index) {
            Log.d(TAG, "callback: " + index);
            if (clickCnt < dataSize && dataList.get(clickCnt) == index) {
                clickCnt++;
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
        dataSize += 2;
        initDataList();
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
            return;
        }
    }

    private void startShowAnswer() {
        state = STATE_SHOW_QUESTION;
        clearDisposable = Flowable.interval(currentIndex, 2, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .take(dataSize + 1)
                .subscribe(aLong -> groupView.setSelect(-1), throwable -> currentIndex = -1, () -> {
                    currentIndex = -1;
                    playButton.setText("开始");
                    state = STATE_SHOW_QUESTION_END;
                });
        selectDisposable = Flowable.interval(currentIndex + 1, 2, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .take(dataSize)
                .subscribe(aLong -> {
                            Log.d(TAG, "startShowAnswer show: " + aLong);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return keyCode == KeyEvent.KEYCODE_BACK || super.onKeyDown(keyCode, event);
    }
}
