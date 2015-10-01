package com.lphaindia.dodapp.dodapp.overlays;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import com.facebook.common.util.UriUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.DraweeView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.lphaindia.dodapp.dodapp.Product.Product;
import com.lphaindia.dodapp.dodapp.R;
import com.lphaindia.dodapp.dodapp.accessibilityFeatures.TapAccessibilityService;
import com.lphaindia.dodapp.dodapp.accessibilityFeatures.TapAnalytics;
import com.lphaindia.dodapp.dodapp.stringToProductAdapter.KeywordsToProducts;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by aasha.medhi on 8/27/15.
 */
public class IconOverlay{
    private static IconOverlay mInstance = null;
    //ImageView mOverlayView;
    DraweeView draweeView;
    private static Context mContext;
    public static IconOverlay getInstance(Context ctxt){
        if(null == mInstance) {
            if(ctxt == null)
                return null;
            mContext = ctxt;
            mInstance = new IconOverlay();
        }
        return mInstance;
    }
    private IconOverlay(){

    }
    public boolean removeOverlay(){
        // Remove view from WindowManager
        if (isOverlayShown()) {
            try {
                WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
                //wm.removeView(mOverlayView);
                wm.removeView(draweeView);
                //mOverlayView = null;
                draweeView = null;
                mInstance = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }
    public boolean showOverlay() {
        boolean isSuccess = false;
        //if (mOverlayView != null) {
        if(draweeView != null) {
            Log.w("TAG", "Cannot recreate overlay");
            return isSuccess;
        }
        //TapAnalytics.sendAnalyticsSwipeIconVisible(TapAccessibilityService.mTracker);
        // Create overlay video
        //createOverlay(mOverlayView != null);
        createOverlay(draweeView != null);
        return true;
    }
    public boolean isOverlayShown(){
        //return (mOverlayView != null);
        return (draweeView != null);
    }
    /**
     * Create video overlay
     *
     * @param isCreated
     */
    private void createOverlay(boolean isCreated) {
        if (isCreated) return;

        // Create System overlay video
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                150, 150,
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        params.x = 0;
        params.y = 300;
        //mOverlayView = new ImageView(mContext);
        draweeView = new SimpleDraweeView(mContext);
        //draweeView.setImageResource(R.drawable.icon);
        //mOverlayView.setImageResource(R.drawable.icon);
        /*Uri uri = new Uri.Builder()
                .scheme(UriUtil.LOCAL_RESOURCE_SCHEME) // "res"
                .path(String.valueOf(R.drawable.icon))
                .build();
        ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(uri).build();*/
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(Uri.parse("res:///" + R.drawable.icon))
                .setAutoPlayAnimations(true)
                .build();
        draweeView.setController(controller);
        //Add close button
        final WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        //wm.addView(mOverlayView, params);
        wm.addView(draweeView, params);
        //mOverlayView.setOnTouchListener(new View.OnTouchListener() {
        draweeView.setOnTouchListener(new View.OnTouchListener() {
            private WindowManager.LayoutParams paramsF = params;
            private float initialX;
            private float initialTouchX;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = event.getX();
                        break;
                    case MotionEvent.ACTION_UP:
                        initialTouchX = event.getX();
                        float deltaX = initialTouchX - initialX;
                        if (Math.abs(deltaX) > 30)
                        {
                            Point size = new Point();
                            wm.getDefaultDisplay().getSize(size);
                            //paramsF.x = size.x - mOverlayView.getWidth();
                            paramsF.x = size.x - draweeView.getWidth();
                            //mOverlayView.animate().translationX(paramsF.x);
                            draweeView.animate().translationX(paramsF.x);
                            //wm.updateViewLayout(mOverlayView, paramsF);
                            wm.updateViewLayout(draweeView, paramsF);
                            //send to analytics - swipe event
                            //TapAnalytics.sendAnalyticsSwipeOnIcon(TapAccessibilityService.mTracker);
                            //Remove icon and fetch results from server
                            TapAnalytics.sendAnalyticsSwipeOnIcon(TapAccessibilityService.mTracker);
                            removeOverlay();
                            try {
                                new KeywordsToProducts(mContext).execute("test");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            //TapAccessibilityService.generateKeywords();
                            //TapAccessibilityService.sendKeywords();
                        }
                        break;
                }
                return false;
            }
        });
    }
}
