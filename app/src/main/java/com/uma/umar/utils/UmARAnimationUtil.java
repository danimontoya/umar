package com.uma.umar.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.widget.ImageView;

/**
 * Created by danieh on 9/4/17.
 */

public class UmARAnimationUtil {

    private UmARAnimationUtil() {
    }

    public static void animateButtonIcon(final ImageView v, final int newIconResId) {
        v.animate().scaleX(0).setDuration(FirebaseConstants.DEFAULT_HALF_ANIM_DURATION).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                v.clearAnimation();
                v.setImageResource(newIconResId);
                v.animate().scaleX(1).setDuration(FirebaseConstants.DEFAULT_HALF_ANIM_DURATION).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationCancel(Animator animation) {
                        v.clearAnimation();
                    }
                }).start();
            }
        }).start();
    }
}
