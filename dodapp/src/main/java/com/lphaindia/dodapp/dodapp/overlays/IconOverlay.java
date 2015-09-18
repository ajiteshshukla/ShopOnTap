package com.lphaindia.dodapp.dodapp.overlays;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import com.lphaindia.dodapp.dodapp.Product.Product;
import com.lphaindia.dodapp.dodapp.R;
import com.lphaindia.dodapp.dodapp.accessibilityFeatures.TapAccessibilityService;
import com.lphaindia.dodapp.dodapp.stringToProductAdapter.KeywordsToProducts;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by aasha.medhi on 8/27/15.
 */
public class IconOverlay{
    private static IconOverlay mInstance = null;
    ImageView mOverlayView;
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
                wm.removeView(mOverlayView);
                mOverlayView = null;
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
        if (mOverlayView != null) {
            Log.w("TAG", "Cannot recreate overlay");
            return isSuccess;
        }
        //TapAnalytics.sendAnalyticsSwipeIconVisible(TapAccessibilityService.mTracker);
        // Create overlay video
        createOverlay(mOverlayView != null);
        return true;
    }
    public boolean isOverlayShown(){
        return (mOverlayView != null);
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
        mOverlayView = new ImageView(mContext);
        mOverlayView.setImageResource(R.drawable.icon);
        //Add close button
        final WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        wm.addView(mOverlayView, params);
        mOverlayView.setOnTouchListener(new View.OnTouchListener() {
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
                            paramsF.x = size.x - mOverlayView.getWidth();
                            mOverlayView.animate().translationX(paramsF.x);
                            wm.updateViewLayout(mOverlayView, paramsF);
                            //send to analytics - swipe event
                            //TapAnalytics.sendAnalyticsSwipeOnIcon(TapAccessibilityService.mTracker);
                            //Remove icon and fetch results from server
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
