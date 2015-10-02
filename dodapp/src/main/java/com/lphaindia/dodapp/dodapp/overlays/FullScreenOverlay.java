package com.lphaindia.dodapp.dodapp.overlays;

import android.content.Context;
import android.graphics.PixelFormat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.lphaindia.dodapp.dodapp.Product.Product;
import com.lphaindia.dodapp.dodapp.R;
import com.lphaindia.dodapp.dodapp.uiAdapters.FullscreenAdapter;

import java.util.List;

/**
 * Created by aasha.medhi on 8/27/15.
 */
public class FullScreenOverlay{

    private static FullScreenOverlay mInstance = null;
    RelativeLayout mOverlayView;
    private RecyclerView mList;
    private RecyclerView mFullScreenList;
    private TextView mNoItemsTextView;
    private FullscreenAdapter mAdapter;
    private List<Product> mItems;
    private static Context mContext;
    private ImageButton cView;
    private int mPosition = 0;
    public static FullScreenOverlay getInstance(Context ctxt){
        if(null == mInstance) {
            if(ctxt == null)
                return null;
            mContext = ctxt;
            mInstance = new FullScreenOverlay();
        }
        return mInstance;
    }
    private FullScreenOverlay(){

    }
    public boolean removeOverlay(){
        // Remove view from WindowManager
        if (isOverlayShown()) {
            try {
                WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
                wm.removeView(mOverlayView);
                mOverlayView = null;
                mContext = null;
                mInstance = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }
    public boolean showOverlay(List<Product> items, int position) {
        boolean isSuccess = false;
        if (mOverlayView != null) {
            //Log.w("TAG", "Cannot recreate overlay");
            return isSuccess;
        }
        mItems = items;
        // Create overlay video
        mPosition = position;
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
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.BOTTOM;

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mOverlayView = (RelativeLayout) inflater.inflate(R.layout.fullscreen_overlay, null);
        //Add close button
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        cView = (ImageButton)mOverlayView.findViewById(R.id.close_btn);
        cView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                removeOverlay();
            }
        });
        wm.addView(mOverlayView, params);
        mList = (RecyclerView) mOverlayView.findViewById(R.id.section_list);
        mList.setLayoutManager(getLayoutManager());
        mList.addItemDecoration(getItemDecoration());

        mList.getItemAnimator().setAddDuration(1000);
        mList.getItemAnimator().setChangeDuration(1000);
        mList.getItemAnimator().setMoveDuration(1000);
        mList.getItemAnimator().setRemoveDuration(1000);

        mAdapter = getAdapter();
        mAdapter.setItemCount(mItems.size());
        int productListIndex = 0;
        //Add the items to adapter
        for(int index = 0; index < mItems.size(); index++){
            Product product = mItems.get(index);
            if(product != null) {
                try {
                    mAdapter.addItem(productListIndex, product);
                    productListIndex++;
                }catch ( NullPointerException e){
                    //e.printStackTrace();
                }
            }
        }
        //mAdapter.setOnItemClickListener(this);
        mList.setAdapter(mAdapter);
        mList.scrollToPosition(mPosition);
    }
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
    }


    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new InsetDecoration(mContext);
    }


    protected int getDefaultItemCount() {
        return 40;
    }


    protected FullscreenAdapter getAdapter() {
        return new FullscreenAdapter(mContext);
    }

}
