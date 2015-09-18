package com.lphaindia.dodapp.dodapp.overlays;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.lphaindia.dodapp.dodapp.R;

/**
 * Created by aasha.medhi on 9/7/15.
 */
public class LoadingOverlay {
    RelativeLayout mLoadingView;
    ImageView mLoadingAnim;
    private static LoadingOverlay mInstance = null;
    private static Context mContext;
    public static LoadingOverlay getInstance(Context ctxt){
        if(null == mInstance) {
            if(ctxt == null)
                return null;
            mContext = ctxt;
            mInstance = new LoadingOverlay();
        }
        return mInstance;
    }
    private LoadingOverlay(){

    }
    public boolean isOverlayShown(){
        return (mLoadingView != null);
    }
    public void showOverlay(){
        if(isOverlayShown()){
            return;
        }
        WindowManager.LayoutParams loadingParams = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 400,
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT);
        loadingParams.gravity = Gravity.BOTTOM;
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        try {
            mLoadingView = (RelativeLayout) inflater.inflate(R.layout.loading, null);
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
        wm.addView(mLoadingView, loadingParams);
        mLoadingAnim = (ImageView)mLoadingView.findViewById(R.id.myanimation);
        final AnimationDrawable myAnimationDrawable
                = (AnimationDrawable)mLoadingAnim.getDrawable();
        myAnimationDrawable.start();
    }
    public boolean removeOverlay(){
        if(isOverlayShown()) {
            WindowManager wm = (WindowManager) mContext.getSystemService(mContext.WINDOW_SERVICE);
            wm.removeView(mLoadingView);
            mLoadingView = null;
            return true;
        }
        return false;
    }
}
