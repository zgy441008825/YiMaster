package com.zou.focus;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private List<String> dataList = new ArrayList<>();

    private int dataSize = 25;

    private TextView textView;
    private RecyclerView recyclerView;
    private MyRecyclerViewAdapter adapter;
    private boolean isStart;
    private Button button;
    private Timer timeKeepTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_0);
        textView = findViewById(R.id.timeKeep);
        recyclerView = findViewById(R.id.recyclerView);
        button = findViewById(R.id.button);
        adapter = new MyRecyclerViewAdapter(dataList, this);
        adapter.setOnClickListener(onClickListener);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 5));
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyMediaHelper.loadClick(this);
        MyMediaHelper.playGameBg(this);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        MyMediaHelper.destory();
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (int) v.getTag(R.id.textView);
            MyMediaHelper.playClick(adapter.answerOne(position) ? MyMediaHelper.CLICK_RIGHT : MyMediaHelper
                    .CLICK_ERROR);
            if (adapter.isOver()) {
                stopTimerKeep();
                showDownDialog();
            }
        }
    };

    private void showDownDialog() {
        new AlertDialog.Builder(this)
                .setTitle("恭喜恭喜")
                .setMessage("用时:" + textView.getText())
                .setPositiveButton("继续", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dataList.clear();
                        adapter.notifyDataSetChanged();
                    }
                })
                .show();
    }

    public void clear(View view) {
        time = 0;
        adapter.clear();
        init();
        if (!isStart) {
            startTimerKeep();
        } else {
            stopTimerKeep();
        }
        isStart = !isStart;
        button.setText(isStart ? "清零" : "开始");
    }

    private long time = 0;

    private void startTimerKeep() {
        stopTimerKeep();
        timeKeepTimer = new Timer();
        timeKeepTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SimpleDateFormat format = new SimpleDateFormat("mm:ss:SS", Locale.getDefault());
                        textView.setText(format.format(time));
                        time += 10;
                    }
                });
            }
        }, 0, 10);
    }

    private void stopTimerKeep() {
        if (timeKeepTimer != null) {
            timeKeepTimer.cancel();
            timeKeepTimer = null;
        }
    }

    private void init() {
        dataList.clear();
        for (int i = 0; i < dataSize; i++) {
            dataList.add(String.valueOf(i + 1));
        }
        Collections.shuffle(dataList);
        adapter.notifyDataSetChanged();
    }
}
