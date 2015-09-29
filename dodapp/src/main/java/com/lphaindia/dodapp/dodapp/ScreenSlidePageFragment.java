package com.lphaindia.dodapp.dodapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by ajitesh.shukla on 9/10/15.
 */
public class ScreenSlidePageFragment extends Fragment implements View.OnClickListener{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_slide_page, container, false);
        Button nextButton = (Button) rootView.findViewById(R.id.next1);
        nextButton.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        ScreenSlidePagerActivity.mPager.setCurrentItem(ScreenSlidePagerActivity.mPager.getCurrentItem() + 1);
    }
}
