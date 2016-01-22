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
public class ScreenSlidePageFragment2 extends Fragment implements View.OnClickListener{
    private static ViewGroup rootView = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_slide_page2, container, false);

        Button nextButton = (Button) rootView.findViewById(R.id.next2);
        nextButton.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser && rootView != null) {
            MyBounceAnimator.stopAllAnimations();
            ScreenSlidePageFragment.removeViews();
            ImageView mainbg = (ImageView) rootView.findViewById(R.id.intro_background);
            mainbg.setVisibility(View.VISIBLE);
            mainbg.bringToFront();
            ImageView myntralogo = (ImageView) rootView.findViewById(R.id.myntralogo);
            ImageView myntraprod = (ImageView) rootView.findViewById(R.id.app_background);
            ImageView mylogo = (ImageView) rootView.findViewById(R.id.sot_small);
            ImageView mysuggestions = (ImageView) rootView.findViewById(R.id.myntra_suggestion);

            MyBounceAnimator.AnimateSlide2(myntralogo, myntraprod, mylogo, mysuggestions);
        } else if (!isVisibleToUser && rootView != null) {
            ImageView mainbg = (ImageView) rootView.findViewById(R.id.intro_background);
            mainbg.setVisibility(View.VISIBLE);
            ImageView myntralogo = (ImageView) rootView.findViewById(R.id.myntralogo);
            ImageView myntraprod = (ImageView) rootView.findViewById(R.id.app_background);
            ImageView mylogo = (ImageView) rootView.findViewById(R.id.sot_small);
            ImageView mysuggestions = (ImageView) rootView.findViewById(R.id.myntra_suggestion);
            myntralogo.setVisibility(View.INVISIBLE);
            myntraprod.setVisibility(View.INVISIBLE);
            mylogo.setVisibility(View.INVISIBLE);
            mysuggestions.setVisibility(View.INVISIBLE);
        }
    }

    public static void removeViews() {
        if (rootView != null) {
            ImageView mainbg = (ImageView) rootView.findViewById(R.id.intro_background);
            mainbg.setVisibility(View.VISIBLE);
            ImageView myntralogo = (ImageView) rootView.findViewById(R.id.myntralogo);
            ImageView myntraprod = (ImageView) rootView.findViewById(R.id.app_background);
            ImageView mylogo = (ImageView) rootView.findViewById(R.id.sot_small);
            ImageView mysuggestions = (ImageView) rootView.findViewById(R.id.myntra_suggestion);
            myntralogo.setVisibility(View.INVISIBLE);
            myntraprod.setVisibility(View.INVISIBLE);
            mylogo.setVisibility(View.INVISIBLE);
            mysuggestions.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        ScreenSlidePagerActivity.mPager.setCurrentItem(ScreenSlidePagerActivity.mPager.getCurrentItem() + 1);
    }
}
