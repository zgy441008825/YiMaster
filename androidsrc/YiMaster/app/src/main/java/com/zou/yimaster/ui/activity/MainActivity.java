package com.zou.yimaster.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.umeng.message.inapp.InAppMessageManager;
import com.zou.yimaster.R;
import com.zou.yimaster.common.PowerFactory;
import com.zou.yimaster.ui.base.BaseActivity;
import com.zou.yimaster.utils.ToastHelper;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    private TextView tvMoneyCnt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvMoneyCnt = findViewById(R.id.powerInfoMoney);
        PowerFactory.getInstance().setListener(powerProductionListener);
        findViewById(R.id.BTLogin).setOnClickListener(onClickListener);
        findViewById(R.id.titleLayout).setOnClickListener(onClickListener);
        InAppMessageManager.getInstance(this).showCardMessage(this, "main",
                () -> Log.i(TAG, "card message close"));
        requestPermission();
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.CALL_PHONE,
                    Manifest.permission.READ_LOGS,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.SET_DEBUG_APP,
                    Manifest.permission.SYSTEM_ALERT_WINDOW,
                    Manifest.permission.GET_ACCOUNTS,
                    Manifest.permission.WRITE_APN_SETTINGS};
            requestPermissions(mPermissionList, 123);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        PowerFactory.getInstance().requestCallback();
    }

    private View.OnClickListener onClickListener = v -> {
        switch (v.getId()) {
            case R.id.BTLogin:
                Intent intent = new Intent(this, GameResultActivity.class);
                intent.putExtra("time", 180056);
                intent.putExtra("count", 6);
                startActivity(intent);
//                startActivity(new Intent(this, PlayActivity.class));
                break;
            case R.id.titleLayout:
                startActivity(new Intent(this, BuyActivity.class));
                break;
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private PowerFactory.IPowerProductionListener powerProductionListener = new PowerFactory.IPowerProductionListener
            () {
        @Override
        public void onStateChange(String time) {
        }

        @Override
        public void onStockChange(int stock) {
        }

        @Override
        public void onMoneyChange(int money) {
            tvMoneyCnt.setText(String.valueOf(money));
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
        PowerFactory.getInstance().turnToBackground();
        MobclickAgent.onKillProcess(this);
        Process.killProcess(Process.myPid());
        System.exit(0);
    }
}
