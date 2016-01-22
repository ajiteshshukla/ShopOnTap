package com.lphaindia.dodapp.dodapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import com.lphaindia.dodapp.dodapp.animation.MyBounceAnimator;

/**
 * Created by ajitesh.shukla on 9/10/15.
 */
public class ScreenSlidePageFragment extends Fragment implements View.OnClickListener{
    private static ViewGroup rootView = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_slide_page, container, false);
        MyBounceAnimator.stopAllAnimations();
        ImageView imageViewAllLogo = (ImageView) rootView.findViewById(R.id.introgif_logos);
        ImageView imageViewMyLogo = (ImageView) rootView.findViewById(R.id.introgif_mylogo);
        MyBounceAnimator.AnimateSlide1(imageViewAllLogo, imageViewMyLogo);
        Button nextButton = (Button) rootView.findViewById(R.id.next1);
        nextButton.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && rootView != null) {
            MyBounceAnimator.stopAllAnimations();
            ScreenSlidePageFragment2.removeViews();
            ImageView main_bg = (ImageView) rootView.findViewById(R.id.introgif_holder);
            main_bg.bringToFront();
            ImageView imageViewAllLogo = (ImageView) rootView.findViewById(R.id.introgif_logos);
            ImageView imageViewMyLogo = (ImageView) rootView.findViewById(R.id.introgif_mylogo);
            MyBounceAnimator.AnimateSlide1(imageViewAllLogo, imageViewMyLogo);
        } else if (!isVisibleToUser && rootView != null) {
            ImageView imageViewAllLogo = (ImageView) rootView.findViewById(R.id.introgif_logos);
            ImageView imageViewMyLogo = (ImageView) rootView.findViewById(R.id.introgif_mylogo);
            imageViewAllLogo.clearAnimation();
            imageViewMyLogo.clearAnimation();
            imageViewAllLogo.setVisibility(View.INVISIBLE);
            imageViewMyLogo.setVisibility(View.INVISIBLE);
        }
    }

    public static void removeViews() {
        if (rootView != null) {
            ImageView imageViewAllLogo = (ImageView) rootView.findViewById(R.id.introgif_logos);
            ImageView imageViewMyLogo = (ImageView) rootView.findViewById(R.id.introgif_mylogo);
            imageViewAllLogo.clearAnimation();
            imageViewMyLogo.clearAnimation();
            imageViewAllLogo.setVisibility(View.INVISIBLE);
            imageViewMyLogo.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        ScreenSlidePagerActivity.mPager.setCurrentItem(ScreenSlidePagerActivity.mPager.getCurrentItem() + 1);
    }
}
