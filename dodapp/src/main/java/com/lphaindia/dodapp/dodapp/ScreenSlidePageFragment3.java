package com.lphaindia.dodapp.dodapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.DraweeView;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by ajitesh.shukla on 9/10/15.
 */
public class ScreenSlidePageFragment3 extends Fragment implements View.OnClickListener{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_slide_page3, container, false);
        DraweeView draweeView = (SimpleDraweeView) rootView.findViewById(R.id.accessibilitygif_holder);
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(Uri.parse("res:///" + R.drawable.enable_accessibility_demo))
                .setAutoPlayAnimations(true)
                .build();
        draweeView.setController(controller);
        draweeView.setMaxWidth(200);
        draweeView.setMaxHeight(320);
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
