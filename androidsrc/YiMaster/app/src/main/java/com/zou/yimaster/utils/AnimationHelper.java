package com.zou.yimaster.utils;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

/**
 * Created by zougy on 03.23.023
 */
public class AnimationHelper {

    public static void translationY(View view, boolean up, long duration, int offset) {
        AnimationSet set = new AnimationSet(true);
        set.addAnimation(translationYAnimationY(up, (int) duration, offset));
        set.addAnimation(getAlphaAnimation(0, 1, (int) duration, offset));
        view.clearAnimation();
        view.startAnimation(set);
    }

    public static void translationX(View view, boolean left, long duration, int offset) {
        AnimationSet set = new AnimationSet(true);
        set.addAnimation(translationYAnimationX(left, (int) duration, offset));
        set.addAnimation(getAlphaAnimation(0, 1, (int) duration, offset));
        view.clearAnimation();
        view.startAnimation(set);
    }

    public static void alpha(View view, float start, float to, int duration) {
        if (view.getVisibility() != View.VISIBLE)
            view.setVisibility(View.VISIBLE);
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "alpha", start, to);
        objectAnimator.setDuration(duration);
        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        objectAnimator.start();
    }

    public static Animation getAlphaAnimation(float start, float to, int duration, int offset) {
        Animation animation = new AlphaAnimation(start, to);
        animation.setDuration(duration);
        animation.setStartOffset(offset);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        return animation;
    }

    public static Animation translationYAnimationY(boolean up, int duration, int offset) {
        TranslateAnimation tn = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT,
                0, Animation.RELATIVE_TO_SELF, up ? 1 : -1, Animation.RELATIVE_TO_SELF, 0);
        tn.setDuration(duration);
        tn.setStartOffset(offset);
        return tn;
    }

    public static Animation translationYAnimationX(boolean left, int duration, int offset) {
        TranslateAnimation tn = new TranslateAnimation(Animation.RELATIVE_TO_SELF, left ? -1 : 1, Animation
                .RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
        tn.setDuration(duration);
        tn.setStartOffset(offset);
        return tn;
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
