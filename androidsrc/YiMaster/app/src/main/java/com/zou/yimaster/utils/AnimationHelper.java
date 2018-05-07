package com.zou.yimaster.utils;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;

/**
 * Created by zougy on 03.23.023
 */
public class AnimationHelper {

    public static void translationY(View view, int ty, int duration) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "translationY", ty);
        objectAnimator.setDuration(duration);
        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        objectAnimator.start();
    }

    public static void alpha(View view, float start, float to, int duration) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "alpha", start, to);
        objectAnimator.setDuration(duration);
        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        objectAnimator.start();
    }

    public static Animation getAlphaAnimation(float start, float to, int duration) {
        Animation animation = new AlphaAnimation(start, to);
        animation.setDuration(duration);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        return animation;
    }

    public static void bounceAnimation(View view) {
        view.clearAnimation();
        ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 1.2f, 1f, 1.2f);
        scaleAnimation.setFillBefore(true);
        scaleAnimation.setDuration(50);
        view.startAnimation(scaleAnimation);
    }

    public static ValueAnimator showValueAnimation(int from, int to, long duration, ValueAnimator.AnimatorUpdateListener
            updateListener) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(from, to);
        valueAnimator.setDuration(duration);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.addUpdateListener(updateListener);
        valueAnimator.start();
        return valueAnimator;
    }

    public static ValueAnimator showValueAnimation(int to, long duration, ValueAnimator.AnimatorUpdateListener
            updateListener) {
        return showValueAnimation(0, to, duration, updateListener);
    }


}
