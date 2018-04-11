package com.zou.yimaster.ui.view;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.zou.yimaster.R;
import com.zou.yimaster.ui.adapter.PlayAdapter;

/**
 * Created by zougaoyuan on 2018/3/30
 *
 * @author zougaoyuan
 */
public class QuestionGroupView extends View {

    private static final String TAG = "QuestionGroupView";

    private Paint paint;

    private int normalColor = Color.WHITE;

    private int selectColor = Color.RED;

    private int rightColor = Color.GREEN;

    private int errorColor = Color.RED;

    private PlayAdapter.IClickViewTouchCallback callback;

    private int paintColor = normalColor;

    /**
     * 保存每个item的中心坐标
     */
    private PointF[] points;

    /**
     * item的半径
     */
    private float radius = 100;

    /**
     * item的半径
     */
    private float drawRadius = radius;

    /**
     * 缩放大小
     */
    private float scaleValue = 20;

    /**
     * 列
     */
    private int column = 3;

    private int itemSize = 9;

    private int width, height;

    private int clickIndex = -1;

    /**
     * 动画执行时间
     */
    private static final int ANIMATION_DURATION = 100;

    /**
     * item间距
     */
    private int spacing = 50;

    public QuestionGroupView(Context context) {
        this(context, null);
    }

    public QuestionGroupView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public QuestionGroupView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        paint = new Paint();
        paint.setAntiAlias(true);
        if (attrs != null) {
            TypedArray typedValue = context.obtainStyledAttributes(attrs, R.styleable.QuestionGroupView);
            normalColor = typedValue.getColor(R.styleable.QuestionGroupView_group_click_normalColor, Color.WHITE);
            selectColor = typedValue.getColor(R.styleable.QuestionGroupView_group_click_selectColor, Color.RED);
            rightColor = typedValue.getColor(R.styleable.QuestionGroupView_group_click_rightColor, Color.GREEN);
            errorColor = typedValue.getColor(R.styleable.QuestionGroupView_group_click_errorColor, Color.RED);
            radius = typedValue.getFloat(R.styleable.QuestionGroupView_group_click_radius, 100);
            scaleValue = typedValue.getFloat(R.styleable.QuestionGroupView_group_click_scale, 20);
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
        colorAnimation.addUpdateListener(animation -> paintColor = (Integer) animation.getAnimatedValue());
        colorAnimation.start();
    }

    /**
     * 反转动画
     */
    private void reverse() {
        if (objectAnimator != null) {
            objectAnimator.reverse();
            colorAnimation.reverse();
        }
    }

    private void stopAnimation() {
        if (objectAnimator != null) {
            objectAnimator.cancel();
        }
        if (colorAnimation != null) {
            colorAnimation.cancel();
        }
    }

    public QuestionGroupView setCallback(PlayAdapter.IClickViewTouchCallback callback) {
        this.callback = callback;
        return this;
    }

    public QuestionGroupView setRadius(float radius) {
        this.drawRadius = radius;
        invalidate();
        return this;
    }

    public QuestionGroupView setScaleValue(float scaleValue) {
        this.scaleValue = scaleValue;
        return this;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        initPoint();
    }

    public int getColumn() {
        return column;
    }

    public QuestionGroupView setColumn(int column) {
        this.column = column;
        initPoint();
        return this;
    }

    public int getItemSize() {
        return itemSize;
    }

    public QuestionGroupView setItemSize(int itemSize) {
        this.itemSize = itemSize;
        initPoint();
        return this;
    }

    public QuestionGroupView setSelect(int position) {
        this.clickIndex = position;
        this.paintColor = selectColor;
        invalidate();
        return this;
    }

    private RectF rectF = new RectF();

    private void initPoint() {
        if (column <= 0 || itemSize <= 0) return;
        int row = itemSize % column != 0 ? (itemSize / column) + 1 : itemSize / column;
        points = new PointF[itemSize];
        //X坐标
        int start = column % 2 == 0 ? (column / 2) - 1 : column / 2;
        for (int i = start, k = 0; i >= 0; i--, k++) {//左边
            for (int j = 0; j < row; j++) {
                int index = i + j * column;
                if (index > itemSize) break;
                if (points[index] == null) points[index] = new PointF();
                if (column % 2 == 0) {//偶数
                    points[index].x = (width + spacing + radius * 2) / 2 - k * (radius * 2 + spacing);
                } else {
                    points[index].x = width / 2 - (spacing + radius * 2) * k;
                }
            }
        }
        start = column % 2 == 0 ? (column / 2) + 1 : column / 2;
        for (int i = start, k = 0; i < column; i++, k++) {//右边
            for (int j = 0; j < row; j++) {
                int index = i + j * column;
                if (index > itemSize) break;
                if (points[index] == null) points[index] = new PointF();
                if (points[index].x != 0) break;
                if (column % 2 == 0) {//偶数
                    points[index].x = (width + spacing + radius * 2) / 2 + k * (radius * 2 + spacing);
                } else {
                    points[index].x = width / 2 + (spacing + radius * 2) * k;
                }
            }
        }
        //Y坐标
        start = row % 2 == 0 ? (row / 2) - 1 : row / 2;
        for (int i = start, k = 0; i >= 0; i--, k++) {
            for (int j = 0; j < column; j++) {
                int index = i * column + j;
                if (index > itemSize) break;
                if (row % 2 == 0) {//偶数
                    points[index].y = (height + spacing + radius * 2) / 2 - k * (radius * 2 + spacing);
                } else {
                    points[index].y = height / 2 - k * (spacing + radius * 2);
                }
            }
        }
        start = row % 2 == 0 ? (row / 2) + 1 : row / 2;
        for (int i = start, k = 0; i < row; i++, k++) {
            for (int j = 0; j < column; j++) {
                int index = i * column + j;
                if (index > itemSize) break;
                if (points[index].y != 0) break;
                if (row % 2 == 0) {//偶数
                    points[index].y = (height + spacing + radius * 2) / 2 + k * (radius * 2 + spacing);
                } else {
                    points[index].y = height / 2 + k * (spacing + radius * 2);
                }
            }
        }
        rectF.left = points[0].x - spacing / 2 - radius;
        rectF.top = points[0].y - spacing / 2 - radius;
        rectF.right = points[itemSize - 1].x + spacing / 2 + radius;
        rectF.bottom = points[itemSize - 1].y + spacing / 2 + radius;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (points == null || points.length == 0) return;
        for (int i = 0; i < itemSize; i++) {
            if (i == clickIndex) {
                paint.setColor(paintColor);
                canvas.drawCircle(points[i].x, points[i].y, drawRadius, paint);
            } else {
                paint.setColor(normalColor);
                canvas.drawCircle(points[i].x, points[i].y, radius, paint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) return true;
        int action = event.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                clickIndex = computeClickPosition(event);
                if (clickIndex > -1) {
                    setPaintColor();
                    startAnimation(radius, radius - scaleValue, normalColor, paintColor);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (event.getPointerCount() > 1) return true;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                if (clickIndex != computeClickPosition(event)) return true;
                if (computeClickPosition(event) > -1) {
                    reverse();
                }
                break;
            case MotionEvent.ACTION_UP:
                reverse();
                if (clickIndex != computeClickPosition(event)) return true;
                if (callback != null) {
                    callback.onActionUp(clickIndex);
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 计算点击坐标
     */
    private int computeClickPosition(MotionEvent event) {
        float x = event.getX(event.getActionIndex());
        float y = event.getY(event.getActionIndex());
        if (!rectF.contains(x, y)) return -1;
        x -= rectF.left;
        y -= rectF.top;
        int xIndex = (int) (x / (2 * radius + spacing));
        int yIndex = (int) (y / (2 * radius + spacing));
        int index = xIndex + yIndex * column;
        if (index >= itemSize) return -1;
        if (getDistance(x + rectF.left, y + rectF.top, points[index].x, points[index].y) > radius) return -1;
        return index;
    }

    private void setPaintColor() {
        if (callback != null && callback.onActionDown(clickIndex)) {
            paintColor = rightColor;
        } else {
            paintColor = errorColor;
        }
    }

    private float getDistance(float x1, float y1, float x2, float y2) {
        float x = Math.abs(x1 - x2);
        float y = Math.abs(y1 - y2);
        return (float) Math.sqrt(x * x + y * 2);
    }
}
