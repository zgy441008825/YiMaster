package com.zou.yimaster.ui.view;


import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.zou.yimaster.R;

/**
 * Created by zougaoyuan on 2018/3/22
 *
 * @author zougaoyuan
 */
public class WidthScaleAnimation extends View {

    private Paint paint;

    private float circleWidth = 10f;

    private int circleColor = Color.WHITE;

    private int cx, cy;

    private float radius = 20;

    private ObjectAnimator animator;

    public WidthScaleAnimation(Context context) {
        super(context);
        init(context, null);
    }

    public WidthScaleAnimation(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public WidthScaleAnimation(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        paint = new Paint();
        paint.setAntiAlias(true);
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.WidthScaleAnimation);
            circleWidth = array.getFloat(R.styleable.WidthScaleAnimation_circleWidth, 10);
            circleColor = array.getColor(R.styleable.WidthScaleAnimation_normalColor, Color.WHITE);
            radius = array.getFloat(R.styleable.WidthScaleAnimation_width_radius, 30);
            array.recycle();
        }
        paint.setStrokeWidth(circleWidth);
        paint.setColor(circleColor);
        paint.setStyle(Paint.Style.STROKE);
        animator = ObjectAnimator.ofFloat(this, "circleWidth", 10, 150);
        animator.setDuration(800);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setRepeatCount(ObjectAnimator.INFINITE);
        animator.setRepeatMode(ObjectAnimator.REVERSE);
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
        paint.setStrokeWidth(circleWidth);
        canvas.drawCircle(cx, cy, radius, paint);
    }

    public float getCircleWidth() {
        return circleWidth;
    }

    public WidthScaleAnimation setCircleWidth(float circleWidth) {
        this.circleWidth = circleWidth;
        invalidate();
        return this;
    }

    public void startAnimation() {
        if (!animator.isStarted())
            animator.start();
    }

    public void stopAnimation() {
        if (animator.isRunning())
            animator.cancel();
    }
}
