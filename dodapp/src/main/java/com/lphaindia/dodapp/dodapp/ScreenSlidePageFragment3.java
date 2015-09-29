package com.lphaindia.dodapp.dodapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by ajitesh.shukla on 9/10/15.
 */
public class ScreenSlidePageFragment3 extends Fragment implements View.OnClickListener{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_slide_page3, container, false);
        Button nextButton = (Button) rootView.findViewById(R.id.cont);
        nextButton.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        ScreenSlidePagerActivity.AccessibilityExplored = true;
        Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
        Context context = v.getContext();
        context.startActivity(intent);
    }
}
