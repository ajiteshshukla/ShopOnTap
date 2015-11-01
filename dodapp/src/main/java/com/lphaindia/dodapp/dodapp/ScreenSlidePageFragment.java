package com.lphaindia.dodapp.dodapp;

import android.net.Uri;
import android.os.Bundle;
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
public class ScreenSlidePageFragment extends Fragment implements View.OnClickListener{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_slide_page, container, false);
        DraweeView draweeView = (SimpleDraweeView) rootView.findViewById(R.id.appsgif_holder);
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(Uri.parse("res:///" + R.drawable.firstfraggif))
                .setAutoPlayAnimations(true)
                .build();
        draweeView.setController(controller);
        draweeView.setMaxWidth(235);
        draweeView.setMaxHeight(380);
        Button nextButton = (Button) rootView.findViewById(R.id.next1);
        nextButton.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        ScreenSlidePagerActivity.mPager.setCurrentItem(ScreenSlidePagerActivity.mPager.getCurrentItem() + 1);
    }
}
