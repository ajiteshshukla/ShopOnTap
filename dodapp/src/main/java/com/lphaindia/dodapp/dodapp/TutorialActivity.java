package com.lphaindia.dodapp.dodapp;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.DraweeView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lphaindia.dodapp.dodapp.animation.MyBounceAnimator;

/**
 * Created by ajitesh.shukla on 10/31/15.
 */
public class TutorialActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutorial_layout);
        MyBounceAnimator.stopAllAnimations();
        final ImageView mainbg = (ImageView) findViewById(R.id.intro_background);
        mainbg.setVisibility(View.VISIBLE);
        mainbg.bringToFront();
        final ImageView myntralogo = (ImageView) findViewById(R.id.myntralogo);
        final ImageView myntraprod = (ImageView) findViewById(R.id.app_background);
        final ImageView mylogo = (ImageView) findViewById(R.id.sot_small);
        final ImageView mysuggestions = (ImageView) findViewById(R.id.myntra_suggestion);

        MyBounceAnimator.AnimateSlide2(myntralogo, myntraprod, mylogo, mysuggestions);

        Button repeat = (Button) findViewById(R.id.next2);
        repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyBounceAnimator.stopAllAnimations();
                mainbg.setVisibility(View.VISIBLE);
                mainbg.bringToFront();
                MyBounceAnimator.AnimateSlide2(myntralogo, myntraprod, mylogo, mysuggestions);
            }
        });

    }
}
