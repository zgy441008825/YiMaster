/*
package com.zou.yimaster.ui.view;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.zou.yimaster.R;
import com.zou.yimaster.ui.adapter.PlayAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

*/
/**
 * Created by zougaoyuan on 2018/3/28
 *
 * @author zougaoyuan
 *//*

public class ClickView extends View {

    private static final String TAG = "ClickView";

    */
/**
     * 全局的触摸事件
     *//*

    public static Map<String, Integer> CLICK_VIEW = new HashMap<>();

    private Paint paint;

    private int normalColor = Color.WHITE;

    private int selectColor = Color.RED;

    private int cx, cy;

    private float radius = 100;

    private float drawRadius = radius;

    private float scaleValue = 20;

    private int paintColor = Color.WHITE;

    private int rightColor = Color.GREEN;

    private int errorColor = Color.RED;

    private int value = -1;

    private PlayAdapter.IClickViewTouchCallback callback;

    */
/**
     * 动画执行时间
     *//*

    private static final int ANIMATION_DURATION = 200;

    public ClickView(Context context) {
        super(context);
        init(context, null);
    }

    public ClickView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ClickView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        paint = new Paint();
        paint.setAntiAlias(true);
        if (attrs != null) {
            TypedArray typedValue = context.obtainStyledAttributes(attrs, R.styleable.ClickView);
            normalColor = typedValue.getColor(R.styleable.ClickView_click_normalColor, Color.WHITE);
            selectColor = typedValue.getColor(R.styleable.ClickView_click_selectColor, Color.RED);
            rightColor = typedValue.getColor(R.styleable.ClickView_click_rightColor, Color.GREEN);
            errorColor = typedValue.getColor(R.styleable.ClickView_click_errorColor, Color.RED);
            radius = typedValue.getFloat(R.styleable.ClickView_click_radius, 100);
            scaleValue = typedValue.getFloat(R.styleable.ClickView_click_scale, 20);
            typedValue.recycle();
        } else {
            normalColor = Color.WHITE;
            selectColor = Color.RED;
            radius = 100;
            scaleValue = 20;
        }
        paintColor = normalColor;
        paint.setColor(paintColor);
    }

    private ObjectAnimator objectAnimator;

    private ValueAnimator colorAnimation;

    private void startAnimation(float from, float to, int fromC, int toC) {
        Log.d(TAG, "startAnimation: " + value);
        stopAnimation();
        objectAnimator = ObjectAnimator.ofFloat(this, "radius", from, to);
        objectAnimator.setDuration(ANIMATION_DURATION);
        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        objectAnimator.addUpdateListener(animation -> setRadius((Float) animation.getAnimatedValue()));
        objectAnimator.start();
        colorAnimation = ValueAnimator.ofInt(fromC, toC);
        colorAnimation.setEvaluator(new ArgbEvaluator());
        colorAnimation.setDuration(ANIMATION_DURATION);
        colorAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        colorAnimation.addUpdateListener(animation -> {
            paint.setColor((Integer) animation.getAnimatedValue());
            invalidate();
        });
        colorAnimation.start();
    }

    public int getValue() {
        return value;
    }

    public ClickView setValue(int value) {
        this.value = value;
        return this;
    }

    public void setCallback(PlayAdapter.IClickViewTouchCallback callback) {
        this.callback = callback;
    }

    private void stopAnimation() {
        if (objectAnimator != null) {
            objectAnimator.cancel();
        }
        if (colorAnimation != null) {
            colorAnimation.cancel();
        }
    }

    public void show(boolean isShow) {
        paintColor = isShow ? selectColor : normalColor;
        paint.setColor(paintColor);
        invalidate();
    }

    public void setRadius(float radius) {
        this.drawRadius = radius;
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        cx = w / 2;
        cy = h / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(cx, cy, drawRadius, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        */
/*Log.d(TAG, "onTouchEvent action=");
        if (action == MotionEvent.ACTION_DOWN) {
            CLICK_VIEW.put(String.valueOf(action), value);
        } else if (action == MotionEvent.ACTION_UP) {
            CLICK_VIEW.remove(String.valueOf(value));
        }*//*

        if (getDistance(event.getX(), event.getY()) > radius || !isEnabled()) return true;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                computeValue();
                startAnimation(radius, radius - scaleValue, normalColor, paintColor);
                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "onTouchEvent: UP" + value);
                startAnimation(radius - scaleValue, radius, paintColor, normalColor);
                if (callback != null) {
                    callback.onActionUp(this);
                }
                break;
        }
        return true;
    }

    private void computeValue() {
        if (callback != null && callback.onActionDown(this)) {
            Log.d(TAG, "computeValue right:" + value);
            paintColor = rightColor;
        } else {
            Log.d(TAG, "computeValue error:" + value);
            paintColor = errorColor;
        }
    }

    private float getDistance(float x, float y) {
        float x1 = Math.abs(cx - x);
        float y1 = Math.abs(cy - y);
        return (float) Math.sqrt(x1 * x1 + y1 * y1);
    }
}
*/
