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
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.DraweeView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lphaindia.dodapp.dodapp.R;
import com.lphaindia.dodapp.dodapp.accessibilityFeatures.TapAccessibilityService;
import com.lphaindia.dodapp.dodapp.accessibilityFeatures.TapAnalytics;
import com.lphaindia.dodapp.dodapp.keywordSearchAdapter.KeywordsToProducts;

/**
 * Created by aasha.medhi on 8/27/15.
 */
public class IconOverlay {
    private static IconOverlay mInstance = null;
    //ImageView mOverlayView;
    DraweeView draweeView;
    private static Context mContext;

    public static IconOverlay getInstance(Context ctxt) {
        if (null == mInstance) {
            if (ctxt == null)
                return null;
            mContext = ctxt;
            mInstance = new IconOverlay();
        }
        return mInstance;
    }

    private IconOverlay() {

    }

    public boolean removeOverlay() {
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
        if (draweeView != null) {
            //Log.w("TAG", "Cannot recreate overlay");
            return isSuccess;
        }
        //TapAnalytics.sendAnalyticsSwipeIconVisible(TapAccessibilityService.mTracker);
        // Create overlay video
        //createOverlay(mOverlayView != null);
        createOverlay(draweeView != null);
        return true;
    }

    public boolean isOverlayShown() {
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
                90, 120,
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        params.x = 10;
        params.y = 300;
        draweeView = new SimpleDraweeView(mContext);
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(Uri.parse("res:///" + R.drawable.sot4))
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
            private boolean isMoved = false;
            private float initialY;
            private float initialTouchY;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialY = event.getY();
                        isMoved = false;
                        break;
                    case MotionEvent.ACTION_UP:
                        if(isMoved == false) {
                            TapAnalytics.sendAnalyticsSwipeOnIcon(TapAccessibilityService.mTracker);
                            removeOverlay();
                            try {
                                new KeywordsToProducts(mContext).execute("test");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        initialTouchY = event.getY();
                        float deltaY = initialTouchY - initialY;
                        if (Math.abs(deltaY) > 100) {
                            isMoved = true;
                        }
                        if(isMoved == true){
                            Point size = new Point();
                            wm.getDefaultDisplay().getSize(size);
                            paramsF.y = size.y - (int) event.getRawY();
                            wm.updateViewLayout(draweeView, paramsF);
                        }
                        break;
                }
                return false;
            }
        });
    }
}
