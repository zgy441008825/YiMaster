package com.zou.yimaster.ui.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import org.xutils.common.util.DensityUtil;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by 44100 on 05/11 011
 *
 * @author 44100
 */
public class RollViewGroup extends ViewGroup {
    /**
     * 滚动方向——向上
     */
    public static final boolean DIRECTION_UP = true;

    /**
     * 滚动方向——向下
     */
    public static final boolean DIRECTION_DOWN = true;

    /**
     * 滚动速度，值越小速度越快
     */
    private int rollSpeed = 1000;

    private List<View> dataList = new ArrayList<>();

    /**
     * 设置滚动方向
     */
    private boolean direction = DIRECTION_UP;

    private int space;

    public RollViewGroup(Context context) {
        super(context);
        init(context);
    }

    public RollViewGroup(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RollViewGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        space = DensityUtil.px2dip(30);
        for (int i = 0; i < 30; i++) {
            TextView textView = new TextView(context);
            textView.setText(String.valueOf(random.nextInt(10000)));
            textView.setTextSize(DensityUtil.dip2px(20));
            textView.setTextColor(Color.WHITE);
            dataList.add(textView);
        }
    }

    public List<View> getDataList() {
        return dataList;
    }

    public RollViewGroup setDataList(List<View> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        return this;
    }

    /**
     * @return {@link #DIRECTION_UP}{@link #DIRECTION_DOWN}
     */
    public boolean isDirection() {
        return direction;
    }

    /**
     * 设置滚动方向
     *
     * @param direction {@link #DIRECTION_UP}{@link #DIRECTION_DOWN}
     */
    public RollViewGroup setDirection(boolean direction) {
        this.direction = direction;
        invalidate();
        return this;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        removeAllViews();
        for (View view : dataList)
            addView(view);
        int measureWidth = measureWidth(widthMeasureSpec);
        // 计算自定义的ViewGroup中所有子控件的大小
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int height = 0;
        for (View view : dataList) {
            measureChild(view, widthMeasureSpec, heightMeasureSpec);
            height += view.getMeasuredHeight() + space;
        }
        setMeasuredDimension(measureWidth, height);
    }

    private Random random = new Random();

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // 记录总高度
        int mTotalHeight = space;
        // 遍历所有子视图
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            // 获取在onMeasure中计算的视图尺寸
            int measureHeight = childView.getMeasuredHeight();
            int measuredWidth = childView.getMeasuredWidth();
            int leftOffset = random.nextInt(getMeasuredWidth() - measuredWidth - 100);
            leftOffset = leftOffset < 100 ? leftOffset + 100 : leftOffset;
            childView.layout(l + leftOffset, mTotalHeight, measuredWidth + leftOffset, mTotalHeight + measureHeight);
            mTotalHeight += measureHeight + space;
        }
    }

    private int measureWidth(int pWidthMeasureSpec) {
        int result = 0;
        int widthMode = MeasureSpec.getMode(pWidthMeasureSpec);// 得到模式
        int widthSize = MeasureSpec.getSize(pWidthMeasureSpec);// 得到尺寸
        switch (widthMode) {
            /*
             * mode共有三种情况，取值分别为MeasureSpec.UNSPECIFIED, MeasureSpec.EXACTLY,
             * MeasureSpec.AT_MOST。
             *
             *
             * MeasureSpec.EXACTLY是精确尺寸，
             * 当我们将控件的layout_width或layout_height指定为具体数值时如andorid
             * :layout_width="50dip"，或者为FILL_PARENT是，都是控件大小已经确定的情况，都是精确尺寸。
             *
             *
             * MeasureSpec.AT_MOST是最大尺寸，
             * 当控件的layout_width或layout_height指定为WRAP_CONTENT时
             * ，控件大小一般随着控件的子空间或内容进行变化，此时控件尺寸只要不超过父控件允许的最大尺寸即可
             * 。因此，此时的mode是AT_MOST，size给出了父控件允许的最大尺寸。
             *
             *
             * MeasureSpec.UNSPECIFIED是未指定尺寸，这种情况不多，一般都是父控件是AdapterView，
             * 通过measure方法传入的模式。
             */
            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
                result = widthSize;
                break;
        }
        return result;
    }

    private int measureHeight(int pHeightMeasureSpec) {
        int result = 0;
        int heightMode = MeasureSpec.getMode(pHeightMeasureSpec);
        int heightSize = MeasureSpec.getSize(pHeightMeasureSpec);
        switch (heightMode) {
            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
                result = heightSize;
                break;
        }
        return result;
    }

    public void startRoll() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "translationY", getMeasuredHeight());
        animator.setDuration(dataList.size() * 500);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.start();
    }
}
