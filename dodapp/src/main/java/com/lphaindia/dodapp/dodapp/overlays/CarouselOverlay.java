package com.lphaindia.dodapp.dodapp.overlays;

import android.content.Context;
import android.graphics.PixelFormat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.lphaindia.dodapp.dodapp.R;
import com.lphaindia.dodapp.dodapp.accessibilityFeatures.TapAccessibilityService;
import com.lphaindia.dodapp.dodapp.accessibilityFeatures.TapAnalytics;
import com.lphaindia.dodapp.dodapp.data.Product;
import com.lphaindia.dodapp.dodapp.uiAdapters.CarouselAdapter;
import com.lphaindia.dodapp.dodapp.uiAdapters.FullScreenCardAdapter;
import com.lphaindia.dodapp.dodapp.uiAdapters.ProductCardAdapter;

import java.util.List;

/**
 * Created by aasha.medhi on 8/27/15.
 */
public class CarouselOverlay implements AdapterView.OnItemClickListener{
    private static CarouselOverlay mInstance = null;
    RelativeLayout mOverlayView;
    private RecyclerView mList;
    private CarouselAdapter mAdapter;
    private FullScreenCardAdapter mFullScreenAdapter;
    private List<Product> mItems;
    private static Context mContext;
    private ImageButton cView;
    private ImageButton minView;
    private ViewPager mPager;
    public static CarouselOverlay getInstance(Context ctxt){
        if(null == mInstance) {
            if(ctxt == null)
                return null;
            mContext = ctxt;
            mInstance = new CarouselOverlay();
        }
        return mInstance;
    }

    private CarouselOverlay(){

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

    public boolean showOverlay(List<Product> items) {
        boolean isSuccess = false;
        if (mOverlayView != null) {
            //Log.w("TAG", "Cannot recreate overlay");
            return isSuccess;
        }
        mItems = items;
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
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.BOTTOM;

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mOverlayView = (RelativeLayout) inflater.inflate(R.layout.overlay, null);
        //Add close button
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        cView = (ImageButton)mOverlayView.findViewById(R.id.close_btn);
        minView = (ImageButton)mOverlayView.findViewById(R.id.min_btn);
        mPager = (ViewPager)mOverlayView.findViewById(R.id.viewpager);
        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mList.scrollToPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        cView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                removeOverlay();
                IconOverlay.getInstance(mContext).showOverlay();
            }
        });
        minView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minView.setVisibility(View.GONE);
                mPager.setVisibility(View.GONE);
                cView.setVisibility(View.VISIBLE);

            }
        });
        wm.addView(mOverlayView, params);
        if(null == mItems || mItems.size() == 0){
            //mNoItemsTextView = (TextView)mOverlayView.findViewById(R.id.no_items);
            //mNoItemsTextView.setVisibility(View.VISIBLE);
            removeOverlay();
            Toast.makeText(mContext, "Sorry we could'nt find anything matching the product title !!!",
                    Toast.LENGTH_LONG).show();
            IconOverlay.getInstance(mContext).showOverlay();
            TapAnalytics.sendAnalyticsNullList(TapAccessibilityService.mTracker);
           return;
        }
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
                    e.printStackTrace();
                }
            }
        }
        mAdapter.setOnItemClickListener(this);
        mList.setAdapter(mAdapter);
        mFullScreenAdapter = new FullScreenCardAdapter(mContext, mItems);
        mPager.setAdapter(mFullScreenAdapter);


    }

    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
    }


    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new InsetDecoration(mContext);
    }


    protected CarouselAdapter getAdapter() {
        return new CarouselAdapter(mContext);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        showFullScreenView(position, id);
    }

    private void showFullScreenView(int position, long id){
        minView.setVisibility(View.VISIBLE);
        mPager.setVisibility(View.VISIBLE);
        mPager.setCurrentItem(position);
        cView.setVisibility(View.GONE);
    }
}
