package com.lphaindia.dodapp.dodapp;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.DraweeView;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by ajitesh.shukla on 10/31/15.
 */
public class TutorialActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutorial_layout);
        DraweeView draweeView = (SimpleDraweeView) findViewById(R.id.tutorialintrogif_holder);
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(Uri.parse("res:///" + R.drawable.secondfraggif))
                .setAutoPlayAnimations(true)
                .build();
        draweeView.setController(controller);
        draweeView.setMaxWidth(200);
        draweeView.setMaxHeight(320);
    }
}
