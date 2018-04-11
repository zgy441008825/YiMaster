package com.zou.yimaster.utils;

import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

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

    public static Animation getAlphaAnimation(float start,float to,int duration){
        Animation animation = new AlphaAnimation(start,to);
        animation.setDuration(duration);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        return animation;
    }

}
