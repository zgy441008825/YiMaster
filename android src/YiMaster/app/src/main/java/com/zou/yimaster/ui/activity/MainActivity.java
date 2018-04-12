package com.zou.yimaster.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.umeng.message.inapp.IUmengInAppMsgCloseCallback;
import com.umeng.message.inapp.InAppMessageManager;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.editorpage.ShareActivity;
import com.zou.yimaster.R;
import com.zou.yimaster.common.PowerFactory;
import com.zou.yimaster.ui.base.BaseActivity;
import com.zou.yimaster.utils.ToastHelper;

import java.util.Locale;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    private TextView tvPowerCnt;
    private TextView tvPowerTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvPowerCnt = findViewById(R.id.powerInfoPowerCnt);
        tvPowerTime = findViewById(R.id.powerInfoTime);
        PowerFactory.getInstance().setListener(powerProductionListener);
        findViewById(R.id.BTLogin).setOnClickListener(onClickListener);
        InAppMessageManager.getInstance(this).showCardMessage(this, "main",
                () -> Log.i(TAG, "card message close"));
    }

    private View.OnClickListener onClickListener = v -> {
        if (PowerFactory.getInstance().canPlay()) {
            startActivity(new Intent(this, PlayActivity.class));
        } else {
            ToastHelper.show("。。。。。。。。。。。。。。");
        }
    };

    private PowerFactory.IPowerProductionListener powerProductionListener = new PowerFactory.IPowerProductionListener
            () {
        @Override
        public void onStateChange(String time) {
            tvPowerTime.setText(time);
        }

        @Override
        public void onStockChange(int stock) {
            tvPowerCnt.setText(String.format(Locale.getDefault(), "%1$d/%2$d", stock, PowerFactory.POWER_MAX));
            if (stock >= PowerFactory.POWER_MAX) {
                tvPowerTime.setVisibility(View.GONE);
            } else {
                tvPowerTime.setVisibility(View.VISIBLE);
            }
        }
    };

    private long lastDownBackTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long time = System.currentTimeMillis();
            if ((time - lastDownBackTime) > 1000) {
                ToastHelper.show("双击退出");
                lastDownBackTime = time;
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PowerFactory.getInstance().trunToBackground();
        MobclickAgent.onKillProcess(this);
        Process.killProcess(Process.myPid());
        System.exit(0);
    }
}
