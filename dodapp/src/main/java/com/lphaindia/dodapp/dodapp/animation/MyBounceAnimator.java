package com.lphaindia.dodapp.dodapp.animation;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import com.lphaindia.dodapp.dodapp.AppConstants;

/**
 * Created by ajitesh.shukla on 1/19/16.
 */
public final class MyBounceAnimator {

    public static AnimatorSet finalAnimator = new AnimatorSet();
    public static AnimatorSet finalAnimator2 = new AnimatorSet();
    public static AnimatorSet finalAnimator3 = new AnimatorSet();
    public static AnimatorSet finalAnimator4 = new AnimatorSet();
    public static AnimatorSet scalesuggestions = new AnimatorSet();
    public static AnimatorSet scaleprodpage = new AnimatorSet();

    public static void stopAllAnimations() {
        finalAnimator.end();
        finalAnimator2.end();
        finalAnimator3.end();
        finalAnimator4.end();
        scaleprodpage.end();
        scalesuggestions.end();
    }

    public static void Animate(View view) {
        Log.d(AppConstants.TAG, "animation started");
        view.setVisibility(View.VISIBLE);
        final AnimatorSet bouncer1 = new AnimatorSet();
        final AnimatorSet bouncer2 = new AnimatorSet();
        final AnimatorSet bouncer3 = new AnimatorSet();
        final AnimatorSet bouncer4 = new AnimatorSet();
        final AnimatorSet bouncer5 = new AnimatorSet();

        final ObjectAnimator animY0 = ObjectAnimator.ofFloat(view, "scaleY", 0.0f, 1f);
        final ObjectAnimator animX0 = ObjectAnimator.ofFloat(view, "scaleX", 0.0f, 1f);

        final ObjectAnimator animY1 = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0.6f);
        final ObjectAnimator animX1 = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.6f);

        final ObjectAnimator animY2 = ObjectAnimator.ofFloat(view, "scaleY", 0.6f, 1f);
        final ObjectAnimator animX2 = ObjectAnimator.ofFloat(view, "scaleX", 0.6f, 1f);

        final ObjectAnimator animY3 = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0.8f);
        final ObjectAnimator animX3 = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.8f);

        final ObjectAnimator animY4 = ObjectAnimator.ofFloat(view, "scaleY", 0.8f, 0.9f);
        final ObjectAnimator animX4 = ObjectAnimator.ofFloat(view, "scaleX", 0.8f, 0.9f);

        //bouncer.playTogether(animX, animY);
        bouncer1.playTogether(animX0, animY0);
        bouncer1.setDuration(350);
        bouncer1.setInterpolator(new DecelerateInterpolator());

        bouncer2.playTogether(animX1, animY1);
        bouncer2.setDuration(340);
        bouncer2.setInterpolator(new DecelerateInterpolator());

        bouncer3.playTogether(animX2, animY2);
        bouncer3.setDuration(330);
        bouncer3.setInterpolator(new DecelerateInterpolator());

        bouncer4.playTogether(animX3, animY3);
        bouncer4.setDuration(320);
        bouncer4.setInterpolator(new DecelerateInterpolator());

        bouncer5.playTogether(animX4, animY4);
        bouncer5.setDuration(310);
        bouncer5.setInterpolator(new DecelerateInterpolator());

        finalAnimator.removeAllListeners();
        finalAnimator.playSequentially(bouncer1, bouncer2, bouncer3, bouncer4, bouncer5);
        finalAnimator.start();

    }

    public static void AnimateSlide2pt2(final View view1, final View view2) {
        view1.setVisibility(View.VISIBLE);
        final AnimatorSet bouncer1 = new AnimatorSet();
        final AnimatorSet bouncer2 = new AnimatorSet();
        final AnimatorSet bouncer3 = new AnimatorSet();
        final AnimatorSet bouncer4 = new AnimatorSet();
        final AnimatorSet bouncer5 = new AnimatorSet();

        ObjectAnimator scalesuggestionsX = ObjectAnimator.ofFloat(view2, "scaleX", 0.0f, 1.0f).setDuration(500);
        ObjectAnimator scalesuggestionsY = ObjectAnimator.ofFloat(view2, "scaleY", 0.0f, 1.0f).setDuration(500);
        scalesuggestions.playTogether(scalesuggestionsX, scalesuggestionsY);

        final AnimatorSet scaleDown = new AnimatorSet();
        ObjectAnimator noopX = ObjectAnimator.ofFloat(view1, "scaleX", 1.0f, 0.0f).setDuration(300);
        ObjectAnimator noopY = ObjectAnimator.ofFloat(view1, "scaleY", 1.0f, 0.0f).setDuration(300);
        scaleDown.playTogether(noopX, noopY);

        final AnimatorSet noop = new AnimatorSet();
        ObjectAnimator noopX1 = ObjectAnimator.ofFloat(view1, "scaleX", 0.0f, 0.0f).setDuration(400);
        ObjectAnimator noopY1 = ObjectAnimator.ofFloat(view1, "scaleY", 0.0f, 0.0f).setDuration(400);
        noop.playTogether(noopX1, noopY1);

        final ObjectAnimator animY0 = ObjectAnimator.ofFloat(view1, "scaleY", 0.0f, 1f);
        final ObjectAnimator animX0 = ObjectAnimator.ofFloat(view1, "scaleX", 0.0f, 1f);

        final ObjectAnimator animY1 = ObjectAnimator.ofFloat(view1, "scaleY", 1f, 0.6f);
        final ObjectAnimator animX1 = ObjectAnimator.ofFloat(view1, "scaleX", 1f, 0.6f);

        final ObjectAnimator animY2 = ObjectAnimator.ofFloat(view1, "scaleY", 0.6f, 1f);
        final ObjectAnimator animX2 = ObjectAnimator.ofFloat(view1, "scaleX", 0.6f, 1f);

        final ObjectAnimator animY3 = ObjectAnimator.ofFloat(view1, "scaleY", 1f, 0.8f);
        final ObjectAnimator animX3 = ObjectAnimator.ofFloat(view1, "scaleX", 1f, 0.8f);

        final ObjectAnimator animY4 = ObjectAnimator.ofFloat(view1, "scaleY", 0.8f, 0.9f);
        final ObjectAnimator animX4 = ObjectAnimator.ofFloat(view1, "scaleX", 0.8f, 0.9f);

        //bouncer.playTogether(animX, animY);
        bouncer1.playTogether(animX0, animY0);
        bouncer1.setDuration(350);
        bouncer1.setInterpolator(new DecelerateInterpolator());

        bouncer2.playTogether(animX1, animY1);
        bouncer2.setDuration(340);
        bouncer2.setInterpolator(new DecelerateInterpolator());

        bouncer3.playTogether(animX2, animY2);
        bouncer3.setDuration(330);
        bouncer3.setInterpolator(new DecelerateInterpolator());

        bouncer4.playTogether(animX3, animY3);
        bouncer4.setDuration(320);
        bouncer4.setInterpolator(new DecelerateInterpolator());

        bouncer5.playTogether(animX4, animY4);
        bouncer5.setDuration(310);
        bouncer5.setInterpolator(new DecelerateInterpolator());

        finalAnimator2.removeAllListeners();
        finalAnimator2.playSequentially(bouncer1, bouncer2, bouncer3, bouncer4, bouncer5, scaleDown, noop);
        finalAnimator2.start();
        finalAnimator2.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                view2.setVisibility(View.VISIBLE);
                view2.bringToFront();
                scalesuggestions.removeAllListeners();
                scalesuggestions.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    public static void AnimateSlide1(final View view, final View view2) {
        view.setVisibility(View.VISIBLE);
        view.bringToFront();
        final AnimatorSet bouncer1 = new AnimatorSet();
        final AnimatorSet bouncer2 = new AnimatorSet();
        final AnimatorSet bouncer3 = new AnimatorSet();
        final AnimatorSet bouncer4 = new AnimatorSet();
        final AnimatorSet bouncer5 = new AnimatorSet();
        final AnimatorSet scaleDown = new AnimatorSet();

        final ObjectAnimator animY0 = ObjectAnimator.ofFloat(view, "scaleY", 0.0f, 1f);
        final ObjectAnimator animX0 = ObjectAnimator.ofFloat(view, "scaleX", 0.0f, 1f);

        final ObjectAnimator animY1 = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0.6f);
        final ObjectAnimator animX1 = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.6f);

        final ObjectAnimator animY2 = ObjectAnimator.ofFloat(view, "scaleY", 0.6f, 1f);
        final ObjectAnimator animX2 = ObjectAnimator.ofFloat(view, "scaleX", 0.6f, 1f);

        final ObjectAnimator animY3 = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0.8f);
        final ObjectAnimator animX3 = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.8f);

        final ObjectAnimator animY4 = ObjectAnimator.ofFloat(view, "scaleY", 0.8f, 1.0f);
        final ObjectAnimator animX4 = ObjectAnimator.ofFloat(view, "scaleX", 0.8f, 1.0f);

        final ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(view, "scaleX", 1.0f, 0.0f).setDuration(2000);
        final ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(view, "scaleX", 1.0f, 0.0f).setDuration(2000);

        //bouncer.playTogether(animX, animY);
        bouncer1.playTogether(animX0, animY0);
        bouncer1.setDuration(300);
        bouncer1.setInterpolator(new DecelerateInterpolator());

        bouncer2.playTogether(animX1, animY1);
        bouncer2.setDuration(340);
        bouncer2.setInterpolator(new DecelerateInterpolator());

        bouncer3.playTogether(animX2, animY2);
        bouncer3.setDuration(400);
        bouncer3.setInterpolator(new DecelerateInterpolator());

        bouncer4.playTogether(animX3, animY3);
        bouncer4.setDuration(500);
        bouncer4.setInterpolator(new DecelerateInterpolator());

        bouncer5.playTogether(animX4, animY4);
        bouncer5.setDuration(500);
        bouncer5.setInterpolator(new DecelerateInterpolator());

        scaleDown.playTogether(scaleDownX, scaleDownY);

        finalAnimator3.removeAllListeners();
        finalAnimator3.playSequentially(bouncer1, bouncer2, bouncer3, bouncer4, bouncer5, scaleDown);
        finalAnimator3.start();
        finalAnimator3.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.INVISIBLE);
                view2.setVisibility(View.VISIBLE);
                view2.bringToFront();
                Animate(view2);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    public static void AnimateSlide2(final View view1, final View view2, final View view3, final View view4) {
        final AnimatorSet downanddim  = new AnimatorSet();
        final AnimatorSet upandglow = new AnimatorSet();

        ObjectAnimator scalesuggestionsX = ObjectAnimator.ofFloat(view4, "scaleX", 0.0f, 1.0f).setDuration(200);
        ObjectAnimator scalesuggestionsY = ObjectAnimator.ofFloat(view4, "scaleY", 0.0f, 1.0f).setDuration(200);
        scalesuggestions.playTogether(scalesuggestionsX, scalesuggestionsY);

        final AnimatorSet noopanimator = new AnimatorSet();
        ObjectAnimator noop = ObjectAnimator.ofFloat(view1, "scaleX", 0.0f, 0.0f).setDuration(800);
        noopanimator.playSequentially(noop);

        view1.setVisibility(View.VISIBLE);

        final ObjectAnimator downX = ObjectAnimator.ofFloat(view1, "scaleY", 1f, 0.6f).setDuration(200);
        final ObjectAnimator downY = ObjectAnimator.ofFloat(view1, "scaleX", 1f, 0.6f).setDuration(200);

        final ObjectAnimator alphadim = ObjectAnimator.ofFloat(view1, "alpha", 1.0f, 0.7f).setDuration(200);

        downanddim.playTogether(downX, downY, alphadim);

        final ObjectAnimator upX = ObjectAnimator.ofFloat(view1, "scaleY", 0.6f, 1.0f).setDuration(200);
        final ObjectAnimator upY = ObjectAnimator.ofFloat(view1, "scaleX", 0.6f, 1.0f).setDuration(200);

        final ObjectAnimator alphaglow = ObjectAnimator.ofFloat(view1, "alpha", 0.7f, 1.0f).setDuration(200);

        upandglow.playTogether(upX, upY, alphaglow);

        final ObjectAnimator scaleX = ObjectAnimator.ofFloat(view2, "scaleY", 0.0f, 1.0f).setDuration(800);
        final ObjectAnimator scaleY = ObjectAnimator.ofFloat(view2, "scaleX", 0.0f, 1.0f).setDuration(800);

        scaleprodpage.playTogether(scaleX, scaleY);
        scaleprodpage.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                view3.setVisibility(View.VISIBLE);
                view3.bringToFront();
                AnimateSlide2pt2(view3, view4);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        finalAnimator4.removeAllListeners();
        finalAnimator4.playSequentially(downanddim, upandglow);
        view1.bringToFront();
        finalAnimator4.start();
        finalAnimator4.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //view1.setVisibility(View.INVISIBLE);
                view2.setVisibility(View.VISIBLE);
                view2.bringToFront();
                scaleprodpage.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }
}
